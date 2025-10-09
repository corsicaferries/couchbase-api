package com.neptune2.couchbase_api.model;

public class Departure {
    private String date;
    private String hour;

    // ---- GETTERS & SETTERS ----
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }
}
