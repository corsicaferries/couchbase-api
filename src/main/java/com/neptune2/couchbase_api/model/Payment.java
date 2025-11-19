package com.neptune2.couchbase_api.model;

public class Payment {

    private String type;
    private Integer quantity;
    private Double value;
    private String luncheonCode;
    private String transactionId;

    // Getters & Setters

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public Double getValue() {
        return value/100;
    }

    public void setValue(Double value) {
        this.value = value;
    }

    public String getLuncheonCode() {
        return luncheonCode;
    }

    public void setLuncheonCode(String luncheonCode) {
        this.luncheonCode = luncheonCode;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
