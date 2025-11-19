package com.neptune2.couchbase_api.controller;

import com.neptune2.couchbase_api.model.Ticket;
import com.neptune2.couchbase_api.model.TicketReponse;
import com.neptune2.couchbase_api.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/tickets")
@CrossOrigin(origins = "*")
public class TicketController {

    @Autowired
    private TicketService ticketService;
     @GetMapping
    public TicketReponse getTickets(
            @RequestParam(required = false) String dateVoyage,
            @RequestParam(required = false) String codeLigne,
            @RequestParam(required = false) String heureDepart,
            @RequestParam(required = false) String caisse,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {

        TicketReponse ticketResponse = ticketService.getRepositoryTickets(dateVoyage, codeLigne, heureDepart, caisse,
                startDate, endDate);

        // EXTRAIRE la liste de tickets
    List<Ticket> tickets = ticketResponse.getTickets();

        // CALCULER à partir de la liste et non TicketReponse
        double total = ticketService.calculategetTotalPriceDiscountedWithTaxIncluded(tickets);
        Map<String, Double> totalByPayment = ticketService.calculateTotalsByPayment(tickets);

        // RECONSTRUIRE la réponse propre
        return new TicketReponse(tickets, total, totalByPayment);
        // return ticketService.getRepositoryTickets(dateVoyage, codeLigne, heureDepart,
        // caisse);
    }
}
