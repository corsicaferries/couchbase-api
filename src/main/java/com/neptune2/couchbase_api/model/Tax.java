package com.neptune2.couchbase_api.model;

import java.util.Map;

import com.couchbase.client.core.deps.com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Tax {

    private String _class;
    private String id;
    private String arrivalCountryId;
    private String departureCountryId;
    private Map<String, Double> rates;

    // Constructeurs
    public Tax() {
    }

    public Tax(String _class, String id, String arrivalCountryId, String departureCountryId,
            Map<String, Double> rates) {
        this._class = _class;
        this.id = id;
        this.arrivalCountryId = arrivalCountryId;
        this.departureCountryId = departureCountryId;
        this.rates = rates;
    }

    // Getters et Setters
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public Map<String, Double> getRates() {
        return rates;
    }

    public void setRates(Map<String, Double> rates) {
        this.rates = rates;
    }

    // toString() (optionnel)
    @Override
    public String toString() {
        return "Tax{" +
                "id='" + id + '\'' +
                ", arrivalCountryId='" + arrivalCountryId + '\'' +
                ", departureCountryId='" + departureCountryId + '\'' +
                ", rates=" + rates +
                '}';
    }

    public String get_class() {
        return _class;
    }

    public void set_class(String _class) {
        this._class = _class;
    }
}
