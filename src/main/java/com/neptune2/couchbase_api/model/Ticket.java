package com.neptune2.couchbase_api.model;

import org.springframework.data.annotation.Id;

import com.couchbase.client.core.deps.com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.neptune2.couchbase_api.model.Ticket.RestaurantInfo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Ticket {

    @Id
    private Long id;

    private LocalDate dateVoyage;
    private String codeLigne;
    private LocalTime heureDepart;
    private String caisse;
    private Double montant;
    private Double totalPriceDiscountedWithTaxIncluded;
    private Double totalPriceWithTaxIncluded;
    private Double totalPriceWithTaxExcluded;
    private Double totalDiscount;
    private String currency;
    private Double totalTax;
    private String ticketNumber;
    private List<Payment> payments;
    private Map<String, Double> totalByPayment;
    private String restaurantName; 
    private String restaurantId;     
    @JsonIgnore
    private RestaurantInfo restaurant;
   




    public String getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(String restaurantId) {
        this.restaurantId = restaurantId;
    }

    

    public String getRestaurantName() {
        return restaurantName;
    }

    public void setRestaurantName(String restaurantName) {
        this.restaurantName = restaurantName;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class RestaurantInfo {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    public RestaurantInfo getRestaurant() {
        return restaurant;
    }

    public void setRestaurant(RestaurantInfo restaurant) {
        this.restaurant = restaurant;
    }

    public Map<String, Double> getTotalByPayment() {
        return totalByPayment;
    }

    public void setTotalByPayment(Map<String, Double> totalByPayment) {
        this.totalByPayment = totalByPayment;
    }

    // Getters et setters
    public List<Payment> getPayments() {
        return payments;
    }

    public void setPayments(List<Payment> payments) {
        this.payments = payments;
    }

    public Double getTotalPriceDiscountedWithTaxIncluded() {
        return totalPriceDiscountedWithTaxIncluded / 100;
    }

    public void setTotalPriceDiscountedWithTaxIncluded(Double totalPriceDiscountedWithTaxIncluded) {
        this.totalPriceDiscountedWithTaxIncluded = totalPriceDiscountedWithTaxIncluded;
    }

    public Double getTotalPriceWithTaxIncluded() {
        return totalPriceWithTaxIncluded / 100;
    }

    public void setTotalPriceWithTaxIncluded(Double totalPriceWithTaxIncluded) {
        this.totalPriceWithTaxIncluded = totalPriceWithTaxIncluded;
    }

    public Double getTotalPriceWithTaxExcluded() {
        return totalPriceWithTaxExcluded / 100;
    }

    public void setTotalPriceWithTaxExcluded(Double totalPriceWithTaxExcluded) {
        this.totalPriceWithTaxExcluded = totalPriceWithTaxExcluded;
    }

    public Double getTotalDiscount() {
        return totalDiscount / 100;
    }

    public void setTotalDiscount(Double totalDiscount) {
        this.totalDiscount = totalDiscount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public Double getTotalTax() {
        return totalTax / 100;
    }

    public void setTotalTax(Double totalTax) {
        this.totalTax = totalTax;
    }

    public String getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(String ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getDateVoyage() {
        return dateVoyage;
    }

    public void setDateVoyage(LocalDate dateVoyage) {
        this.dateVoyage = dateVoyage;
    }

    public String getCodeLigne() {
        return codeLigne;
    }

    public void setCodeLigne(String codeLigne) {
        this.codeLigne = codeLigne;
    }

    public LocalTime getHeureDepart() {
        return heureDepart;
    }

    public void setHeureDepart(LocalTime heureDepart) {
        this.heureDepart = heureDepart;
    }

    public String getCaisse() {
        return caisse;
    }

    public void setCaisse(String caisse) {
        this.caisse = caisse;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }
}
