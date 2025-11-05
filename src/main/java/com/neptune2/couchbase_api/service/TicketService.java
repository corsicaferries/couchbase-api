package com.neptune2.couchbase_api.service;

import com.neptune2.couchbase_api.model.Product;
import com.neptune2.couchbase_api.model.Ticket;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

@Service
public class TicketService {

    @Value("${innovorder.api.url}") // configurable dans application.properties
    String externalApiUrl;

    // ✅ GET : lire tous les produits
    @GetMapping
    public List<Ticket> getRepositoryTickets(LocalDate dateVoyage, String codeLigne, LocalTime heureDepart,
            String caisse) {

        final RestTemplate restTemplate = new RestTemplate();

        // Construction de l’URL avec les paramètres
        String url = String.format("%s?dateVoyage=%s", externalApiUrl, dateVoyage);
        if (codeLigne != null && !codeLigne.isEmpty())
            url += "&codeLigne=" + codeLigne;
        if (heureDepart != null)
            url += "&heureDepart=" + heureDepart;
        if (caisse != null && !caisse.isEmpty())
            url += "&caisse=" + caisse;

        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(List.of(MediaType.APPLICATION_JSON));
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Ticket[]> response = restTemplate.exchange(
                url, HttpMethod.GET, request, Ticket[].class);

        return Arrays.asList(Objects.requireNonNull(response.getBody()));
    }
}