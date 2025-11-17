package com.neptune2.couchbase_api.service;

import com.neptune2.couchbase_api.model.Ticket;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
public class TicketService {

    @Value("${innovorder.api.url}") // configurable dans application.properties
    String externalApiUrl;

    @Value("${innovorder.api.auth.url}")
    private String authUrl; // ex: https://api-dev.innovorder.fr/oauth/login

    @Value("${innovorder.api.username}")
    private String username;

    @Value("${innovorder.api.password}")
    private String password;

    @Value("${innovorder.api.rememberMe}")
    private String rememberMe;

    @Value("${innovorder.api.grant_type}")
    private String grant_type;

    final RestTemplate restTemplate = new RestTemplate();

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

    // ✅ GET : lire tous les tickets
    @GetMapping
    public List<Ticket> getRepositoryTickets(String dateVoyage, String codeLigne, String heureDepart,
            String caisse) {

        String token = getAccessToken();

        // Construction de l’URL avec les paramètres
        // String url = String.format("%s?dateVoyage=%s", externalApiUrl, dateVoyage);
        String url = String.format("%s?restaurantIds=1286", externalApiUrl);
        /*
         * if (codeLigne != null && !codeLigne.isEmpty())
         * url += "&codeLigne=" + codeLigne;
         * if (heureDepart != null)
         * url += "&heureDepart=" + heureDepart;
         * 
         * if (caisse != null && !caisse.isEmpty())
         * url += "&caisse=" + caisse;
         */
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(token);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Ticket[]> response = restTemplate.exchange(
                url, HttpMethod.GET, request, Ticket[].class);

        return Arrays.asList(Objects.requireNonNull(response.getBody()));
    }
}