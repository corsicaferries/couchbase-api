package com.neptune2.couchbase_api.controller;

import com.neptune2.couchbase_api.model.Travel;
import com.neptune2.couchbase_api.model.TravelOUT;
import com.neptune2.couchbase_api.service.TravelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/travels")
public class TravelController {

    @Autowired
    private TravelService travelService;

    public TravelController(TravelService travelService) {
        this.travelService = travelService;
    }
    // GET /api/travels → liste tous les voyages
    @GetMapping
    public List<TravelOUT> getAllTravels() {
        return travelService.findAllTravels();
    }

    @GetMapping("/ship/{name}")
    public List<Travel> findByShip(@PathVariable String name) {
        return travelService.getTravelsByShip(name);
    }
}
