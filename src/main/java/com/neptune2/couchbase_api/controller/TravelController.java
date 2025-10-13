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
    public List<TravelOUT> getTravels(@RequestBody Map<String, Object> request) {
        int codNavi = (int) request.get("cod_navi");
        String dateDebut = (String) request.get("date_debut");
        String dateFin = (String) request.get("date_fin");

        return travelService.findAllTravels(codNavi, dateDebut, dateFin);

    }

    @GetMapping("/ship/{name}")
    public List<Travel> findByShip(@PathVariable String name) {
        return travelService.getTravelsByShip(name);
    }
}
