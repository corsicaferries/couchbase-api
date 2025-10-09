package com.neptune2.couchbase_api.model;

public class Line {
    private String id;
    private String departurePortId;
    private String arrivalPortId;
    private String arrivalCountryId;
    private String departureCountryId;

    // ---- GETTERS & SETTERS ----
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeparturePortId() {
        return departurePortId;
    }

    public void setDeparturePortId(String departurePortId) {
        this.departurePortId = departurePortId;
    }

    public String getArrivalPortId() {
        return arrivalPortId;
    }

    public void setArrivalPortId(String arrivalPortId) {
        this.arrivalPortId = arrivalPortId;
    }

    public String getArrivalCountryId() {
        return arrivalCountryId;
    }

    public void setArrivalCountryId(String arrivalCountryId) {
        this.arrivalCountryId = arrivalCountryId;
    }

    public String getDepartureCountryId() {
        return departureCountryId;
    }

    public void setDepartureCountryId(String departureCountryId) {
        this.departureCountryId = departureCountryId;
    }
}
