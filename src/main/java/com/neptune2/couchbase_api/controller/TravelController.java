package com.neptune2.couchbase_api.controller;

import com.neptune2.couchbase_api.model.Travel;
import com.neptune2.couchbase_api.model.TravelOUT;
import com.neptune2.couchbase_api.service.TravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class TravelController {

    @Autowired
    private TravelService travelService;

    public TravelController(TravelService travelService) {
        this.travelService = travelService;
    }

    // GET /api/travels → liste tous les voyages
    @RequestMapping("/travels")
    public List<TravelOUT> getTravels(@RequestBody(required = false) Map<String, Object> request) {
        int codNavi = 0;
        String dateDebut = null;
        String dateFin = null;
        // Vérifier si "cod_navi", "date_debut", ou "date_fin" sont présents dans la
        // requête
        if (request != null) {
            codNavi = (int) request.get("cod_navi");
            dateDebut = (String) request.get("date_debut");
            dateFin = (String) request.get("date_fin");

            if (request.containsKey("cod_navi") && request.get("cod_navi") != null) {
                codNavi = (int) request.get("cod_navi"); // Assure-toi que la valeur est bien un int
            }

            if (request.containsKey("date_debut") && request.get("date_debut") != null) {
                dateDebut = (String) request.get("date_debut"); // Assure-toi que la valeur est une chaîne
            }

            if (request.containsKey("date_fin") && request.get("date_fin") != null) {
                dateFin = (String) request.get("date_fin"); // Assure-toi que la valeur est une chaîne
            }
        } else {
            codNavi = 0;
            dateDebut = null;
            dateFin = null;
        }
        return travelService.findAllTravels(codNavi, dateDebut, dateFin);

    }

    @GetMapping("/ship/{name}")
    public List<Travel> findByShip(@PathVariable String name) {
        return travelService.getTravelsByShip(name);
    }
}
