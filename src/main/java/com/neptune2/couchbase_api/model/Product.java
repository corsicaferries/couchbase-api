package com.neptune2.couchbase_api.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;

@Document
public class Product {
    @Id
    private Integer id;
    private String name;
    private double priceIncludingTax;
    private String vatType;
    private String type;
    private String categoryId;
    private String categoryName_fr;
    private String description_fr;

    public String getDescription_fr() {
        return description_fr;
    }

    public void setDescription_fr(String description_fr) {
        this.description_fr = description_fr;
    }

    public String getVatType() {
        return vatType;
    }

    public void setVatType(String vatType) {
        this.vatType = vatType;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName_fr() {
        return categoryName_fr;
    }

    public void setCategoryName_fr(String categoryName_fr) {
        this.categoryName_fr = categoryName_fr;
    }

    // Getters / Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getpriceIncludingTax() {
        return priceIncludingTax;
    }

    public void setpriceIncludingTax(double priceIncludingTax) {
        this.priceIncludingTax = priceIncludingTax;
    }
}
