package com.neptune2.couchbase_api.controller;

import com.neptune2.couchbase_api.model.Product;
import com.neptune2.couchbase_api.service.ProductService;
import com.neptune2.couchbase_api.config.CouchbaseScopeService;
import com.couchbase.client.java.json.JsonObject;
import com.couchbase.client.java.kv.GetResult;
import com.couchbase.client.java.Collection;

import org.springframework.http.ResponseEntity;
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
    public ResponseEntity<Product> getProductById(@PathVariable Integer id) {
        Product product = service.getProductById(id);
        return ResponseEntity.ok(product);

    }

    @GetMapping
    public List<Product> getSomeProducts() {
        return service.getRepositoryProducts();
    }
}
