package com.neptune2.couchbase_api.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.data.annotation.Id;
import org.springframework.data.couchbase.core.mapping.Document;

import com.couchbase.client.java.json.JsonArray;
import com.couchbase.client.java.json.JsonObject;

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
    private String categorydescription_fr;
    private String description_fr;
    private List<Allergen> allergens;
    private JsonArray allergensJson;
    private String imageUrl;

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getCategorydescription_fr() {
        return categorydescription_fr;
    }

    public void setCategorydescription_fr(String categorydescription_fr) {
        this.categorydescription_fr = categorydescription_fr;
    }

    public List<Allergen> getAllergens() {
        return allergens;
    }

    public void setAllergens(List<Allergen> allergens) {
        JsonArray jsonArray = JsonArray.create();
        for (Allergen allergen : allergens) {
            JsonObject jsonObject = JsonObject.create()
                    .put("id", allergen.getId())
                    .put("name", JsonObject.from(allergen.getName()));
            jsonArray.add(jsonObject);
        }
        this.allergensJson = jsonArray;
        this.allergens = allergens;
    }

    public void setAllergens(String allergenName) {
        // Option : créer un Allergen à partir du nom
        Allergen a = new Allergen();
        // … initialiser a
        this.setAllergens(Collections.singletonList(a));
    }

    public List<Allergen> convertJsonArrayToAllergens(JsonArray jsonArray) {
        List<Allergen> allergens = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject allergenJson = jsonArray.getObject(i);
            Allergen allergen = new Allergen();
            allergen.setId(allergenJson.getString("id"));
            allergen.setName(allergenJson.getObject("name").toMap());
            allergens.add(allergen);
        }
        return allergens;
    }

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