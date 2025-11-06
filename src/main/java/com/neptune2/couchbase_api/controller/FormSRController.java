package com.neptune2.couchbase_api.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

@Controller
public class FormSRController {

    private final RestTemplate restTemplate = new RestTemplate();
    @Value("${api.key}")
    private String apiKey;

    @GetMapping("/formSR")
    public String showForm() {
        return "formSR"; // ton formulaire
    }

    @PostMapping("/submit")
    public String submitForm(@RequestParam String code, Model model) {
        String apiUrl = "http://localhost:9090/api/products/" + code;

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-API-KEY", apiKey); // <-- ta variable
                                          // d’authentification
        HttpEntity<String> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    apiUrl, // URL
                    HttpMethod.GET, // Méthode HTTP
                    entity, // Contient les headers
                    String.class);

            String body = response.getBody();
            System.out.println("✅ Réponse JSON : " + body);

            ObjectMapper mapper = new ObjectMapper();

            JsonNode root = mapper.readTree(body);

            Integer id = root.path("id").asInt();
            String name = root.path("name").asText();
            double price = root.path("priceIncludingTax").asDouble();

            model.addAttribute("id", id);
            model.addAttribute("name", name);
            model.addAttribute("price", price);
        }

        catch (Exception e) {
            e.printStackTrace();
            model.addAttribute("error", "Erreur lors de l'appel à l'API : " + e.getMessage());

        }
        return "resultSR";

    }
}
