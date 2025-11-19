package com.neptune2.couchbase_api.model;

import java.util.List;
import java.util.Map;

public class TicketReponse {

    private List<Ticket> tickets;
    private Ticket ticket;
    private double totalPriceDiscountedWithTaxIncluded;

    // ✔ Getter pour récupérer tous les tickets
    public List<Ticket> getTickets() {
        return tickets;
    }

    // ✔ Setter
    public void setTickets(List<Ticket> tickets) {
        this.tickets = tickets;
    }

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) {
        this.ticket = ticket;
    }

    public double getTotalPriceDiscountedWithTaxIncluded() {
        return totalPriceDiscountedWithTaxIncluded;
    }

    private Map<String, Double> totalByPayment;

    public Map<String, Double> getTotalByPayment() {
        return totalByPayment;
    }

    public void setTotalByPayment(Map<String, Double> totalByPayment) {
        this.totalByPayment = totalByPayment;
    }

    // Constructeur depuis Ticket
    public TicketReponse(List<Ticket> tickets, double totalPriceDiscountedWithTaxIncluded,
            Map<String, Double> totalByPayment) {
        this.tickets = tickets; // ✔ tu avais oublié ça !
        this.totalPriceDiscountedWithTaxIncluded = totalPriceDiscountedWithTaxIncluded;
        this.totalByPayment = totalByPayment;
    }

    public void setTotalPriceDiscountedWithTaxIncluded(double totalPriceDiscountedWithTaxIncluded) {
        this.totalPriceDiscountedWithTaxIncluded = totalPriceDiscountedWithTaxIncluded;
    }

    public TicketReponse() {
    }
}
