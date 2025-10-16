package com.neptune2.couchbase_api.service;

import com.neptune2.couchbase_api.model.Travel;
import com.neptune2.couchbase_api.model.TravelOUT;
import com.neptune2.couchbase_api.repository.TravelRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

import com.couchbase.client.java.kv.GetResult;
import com.couchbase.client.java.Cluster;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.couchbase.client.java.Collection;
import org.springframework.web.client.RestTemplate;
import org.springframework.http.HttpMethod;

// Add import for Tax class
import com.neptune2.couchbase_api.model.Tax;

// Import JsonObject from Couchbase SDK
@SuppressWarnings("unused")
@Service
public class TravelService {
    @Autowired
    private TravelRepository travelRepository;
    @Autowired
    private CouchbaseScopeService couchbaseScopeService;
    private final Cluster cluster;

    private final RestTemplate restTemplate = new RestTemplate();

    public TravelService(TravelRepository travelRepository, Cluster cluster) {
        this.travelRepository = travelRepository;
        this.cluster = cluster;
    }

    // Récupérer tous les voyages
    public List<TravelOUT> findAllTravels(int codNavi, String dateDebut, String dateFin) {
        String apiUrl = "https://putty.corsicaferries.com/api/consultation-api/list_voyages_neptune"; // API externe

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("X-API-KEY", "teNQ1wqyuVEgXwxxbLryTxzcyf9NP3BhN9YKe25XUB3rx8FJK4phvxGjk6TXvdrD"); // <-- ta variable
                                                                                                      // d’authentification
        List<Map<String, Object>> requestBody = List.of(
                Map.of(
                        "cod_navi", codNavi,
                        "date_debut", dateDebut,
                        "date_fin", dateFin));

        HttpEntity<List<Map<String, Object>>> request = new HttpEntity<>(requestBody, headers);

        try {
            ResponseEntity<Map> response = restTemplate.exchange(apiUrl, HttpMethod.POST,
                    request, Map.class);
            List<Map<String, Object>> data = (List<Map<String, Object>>) response.getBody().get("data");

            ObjectMapper mapper = new ObjectMapper();
            return data.stream()
                    .map(d -> {
                        Travel travel = mapper.convertValue(d, Travel.class);
                        TravelOUT dto = new TravelOUT();
                        dto.setLine(travel.getLine());
                        dto.setShip(travel.getShip());
                        dto.setDeparture(travel.getDeparture());
                        // dto.setTaxId(travel.getTaxId());

                        // Récupérer le tax depuis Couchbase
                        try {

                            Collection taxCollection = couchbaseScopeService.getRepositoryCollectionTax();
                            System.out.println("taxCollection récupérée : " + taxCollection.scopeName());

                            GetResult taxResult = taxCollection.get(travel.getTaxId());
                            System.out.println("taxResult récupéré : " + taxResult.contentAsObject());
                            ObjectMapper innerMapper = new ObjectMapper();
                            innerMapper.configure(
                                    com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES,
                                    false);

                            Tax tax = innerMapper.convertValue(taxResult.contentAsObject().toMap(), Tax.class);
                            dto.setTax(tax);

                        } catch (Exception e) {
                            System.out.println("Erreur lors de la récupération du document : " + e.getMessage());
                            dto.setTax(null);
                        }
                        return dto;
                    })
                    .toList();

        } catch (

        Exception e) {
            throw new RuntimeException("Erreur lors de l'appel à l'API externe : " + e.getMessage());
        }
    }

    // Récupérer un voyage par son ID
    public Travel findTravelById(String id) {
        return travelRepository.findById(id).orElse(null);
    }

    public List<Travel> getTravelsByShip(String shipName) {
        return travelRepository.findByShipName(shipName);
    }

}
