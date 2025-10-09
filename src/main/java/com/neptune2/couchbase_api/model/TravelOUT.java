package com.neptune2.couchbase_api.model;

public class TravelOUT {
    private Line line;
    private Ship ship;
    private Departure departure;
    private String taxId;
    private Tax tax;

    public Line getLine() {
        return line;
    }

    public void setLine(Line line) {
        this.line = line;
    }

    public Ship getShip() {
        return ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public Departure getDeparture() {
        return departure;
    }

    public void setDeparture(Departure departure) {
        this.departure = departure;
    }

    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    public Tax getTax() {
        return tax;
    }

    public void setTax(Tax tax) {
        System.out.println(("dans SETTE TAX :" + tax.getRates()));
        this.tax = tax;
    }

}
