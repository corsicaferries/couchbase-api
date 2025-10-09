package com.neptune2.couchbase_api.model;

import org.springframework.data.couchbase.core.mapping.Document;
import com.couchbase.client.core.deps.com.fasterxml.jackson.annotation.JsonIgnore;

@Document
public class Travel {

    private Line line;
    @JsonIgnore
    private Counts counts;    

    private Ship ship;
    private Departure departure;
    private String taxId;
    private String id;

    @JsonIgnore
    private String _class;
    @JsonIgnore
    private String status;

    // ---- GETTERS & SETTERS ----
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
    public Counts getCounts() {
        return counts;
    }

    public void setCounts(Counts counts) {
        this.counts = counts;
    }

    public String get_class() {
        return _class;
    }

    public void set_class(String _class) {
        this._class = _class;
    }

}
