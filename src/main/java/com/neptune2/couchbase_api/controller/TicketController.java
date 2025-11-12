package com.neptune2.couchbase_api.controller;

import com.neptune2.couchbase_api.model.Ticket;
import com.neptune2.couchbase_api.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "*")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @GetMapping
    public List<Ticket> getTickets(
            @RequestParam String dateVoyage,
            @RequestParam(required = false) String codeLigne,
            @RequestParam(required = false) String heureDepart,
            @RequestParam(required = false) String caisse) {

        return ticketService.getRepositoryTickets(dateVoyage, codeLigne, heureDepart, caisse);
    }
}
