package com.neptune2.couchbase_api.controller;

import com.neptune2.couchbase_api.model.Product;
import com.neptune2.couchbase_api.service.CouchbaseScopeService;
import com.neptune2.couchbase_api.service.ProductService;

import org.springframework.http.HttpStatus;
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

    // ✅ GET : lire tous les produits
    @GetMapping
    public List<Product> getSomeProducts() {
        return service.getRepositoryProducts();
    }

    // ✅ GET : lire un produit par code sous sous famille :ID
    @GetMapping("/ssfa/{id}")
    public ResponseEntity<?> getProductByssfa(@PathVariable Integer id) {
        List<Product> products = service.getProductsByssfa(id);
        if (products.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of(
                            "status", 404,
                            "message", "Aucun produit trouvé pour num_ssfa = " + id));
        }

        return ResponseEntity.ok(products);

    }
}
