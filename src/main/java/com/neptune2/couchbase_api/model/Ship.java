package com.neptune2.couchbase_api.model;

public class Ship {
    private int id;
    private String name;
    private String code;

    // ---- GETTERS & SETTERS ----
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
