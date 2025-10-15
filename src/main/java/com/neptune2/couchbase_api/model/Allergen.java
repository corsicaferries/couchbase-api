package com.neptune2.couchbase_api.model;

import java.util.Map;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Allergen {
    private String id;
    private Map<String, Object> name; // clé = code langue (fr, en, it, de)

    // Getters et setters
    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("name")
    public Map<String, Object> getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(Map<String, Object> name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Allergen{" +
                "id=" + id +
                ", name=" + name +
                '}';
    }
}
