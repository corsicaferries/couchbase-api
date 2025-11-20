package com.neptune2.couchbase_api.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neptune2.couchbase_api.model.Payment;
import com.neptune2.couchbase_api.model.Ticket;
import com.neptune2.couchbase_api.model.TicketReponse;

import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.*;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.HashMap;

@Service
public class TicketService {

    private static final Logger log = LoggerFactory.getLogger(TicketService.class);

    @Value("${innovorder.api.url}")
    String externalApiUrl;

    @Value("${innovorder.api.auth.url}")
    private String authUrl;

    @Value("${innovorder.api.username}")
    private String username;

    @Value("${innovorder.api.password}")
    private String password;

    @Value("${innovorder.api.rememberMe}")
    private String rememberMe;

    @Value("${innovorder.api.grant_type}")
    private String grant_type;

    final RestTemplate restTemplate = new RestTemplate();

    // ---------------- TOKEN ----------------
    private String getAccessToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> body = new HashMap<>();
        body.put("username", username);
        body.put("password", password);
        body.put("rememberMe", rememberMe);
        body.put("grant_type", grant_type);

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity(authUrl, request, Map.class);
        if (response.getStatusCode().is2xxSuccessful() && response.getBody() != null) {
            return (String) response.getBody().get("access_token");
        }
        throw new RuntimeException("Impossible de récupérer le token OAuth : " + response.getStatusCode());
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    private static class ApiResponse {
        public Data data;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Data {
            public List<Ticket> omnichannelOrders;
        }
    }

    public TicketReponse getRepositoryTickets(String dateVoyage, String codeLigne, String heureDepart,
            String caisse,
            String startDate, String endDate) {

        String token = getAccessToken();
        String url = String.format("%s?restaurantIds=1286", externalApiUrl);
        if (startDate != null && endDate != null) {
            url += String.format("&startDate=%s&endDate=%s", startDate, endDate);
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(token);

        HttpEntity<Void> request = new HttpEntity<>(headers);

        log.info("➡ Appel API Innovorder");
        log.info("URL : {}", url);

        try {
            ResponseEntity<String> rawResponse = restTemplate.exchange(url, HttpMethod.GET, request, String.class);

            //log.info(" Réponse brute JSON = {}", rawResponse.getBody());

            ObjectMapper mapper = new ObjectMapper();
            ApiResponse response = mapper.readValue(rawResponse.getBody(), ApiResponse.class);

            List<Ticket> tickets = (response != null && response.data != null)
                    ? response.data.omnichannelOrders
                    : List.of();

            for (Ticket t : tickets) {
                if (t.getRestaurant() != null && t.getRestaurant().getName() != null) {
                    t.setRestaurantName(t.getRestaurant().getName());
                }
            }

            double total = calculategetTotalPriceDiscountedWithTaxIncluded(tickets);

            Map<String, Double> totalByPayment = calculateTotalsByPayment(tickets);

            
            return new TicketReponse(tickets, total, totalByPayment);

        } catch (Exception e) {
            log.error("❌ Erreur REST: {}", e.getMessage(), e);
          return new TicketReponse(List.of(), 0, Map.of());
        }
    }

    public double calculategetTotalPriceDiscountedWithTaxIncluded(List<Ticket> tickets) {
        return tickets.stream()
                .mapToDouble(t -> t.getTotalPriceDiscountedWithTaxIncluded() != null
                        ? t.getTotalPriceDiscountedWithTaxIncluded()
                        : 0)
                .sum();
    }

    public Map<String, Double> calculateTotalsByPayment(List<Ticket> tickets) {
        Map<String, Double> totals = new HashMap<>();

        for (Ticket ticket : tickets) {
            if (ticket.getPayments() == null)
                continue;

            for (Payment p : ticket.getPayments()) {
                double value = p.getValue() != null ? p.getValue() : 0;
                totals.merge(p.getType(), value, Double::sum);
            }
        }

        return totals;
    }

}
