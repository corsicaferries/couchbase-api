package com.neptune2.couchbase_api.controller;

import com.neptune2.couchbase_api.model.Product;
import com.neptune2.couchbase_api.service.ProductService;
import com.neptune2.couchbase_api.config.CouchbaseScopeService;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.kv.GetResult;
import com.couchbase.client.java.Collection;

import org.springframework.web.bind.annotation.*;
import java.util.*;


@RestController
@RequestMapping("/api/products")
public class ProductController {
        private final CouchbaseScopeService couchbaseScopeService;
    private final ProductService service;

    public ProductController(CouchbaseScopeService couchbaseScopeService, ProductService service) {
        this.couchbaseScopeService = couchbaseScopeService;
        this.service = service;
    }

    // ✅ GET : lire un produit par ID
    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Integer id) {
        /* 
        Collection collection = couchbaseScopeService.getRepositoryCollection();
        GetResult result = collection.get(String.valueOf(id));
        JsonObject json = result.contentAsObject();

        Product product = new Product();
        product.setId(id);
        product.setName(json.getString("name"));
        product.setpriceIncludingTax(json.getDouble("priceIncludingTax"));
        */
        
        /*
        Product product = new Product();
        return product;
        */
        return service.getProductById(id);
        
    }

    
    @GetMapping
    public List<Product> getSomeProducts() {
        return service.getRepositoryProducts();        
    }        
}
