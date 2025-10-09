package com.neptune2.couchbase_api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.neptune2.couchbase_api.config.CouchbaseScopeService;
import com.neptune2.couchbase_api.model.Product;
import com.neptune2.couchbase_api.service.ProductService;

import java.util.List;



@RestController
public class HomeController {

private final ProductService service;

public HomeController(ProductService service) {
    this.service = service;
}

@GetMapping("/")     
public String home(CouchbaseScopeService scopeService) {
    return "✅ Ton serveur Spring Boot est bien en ligne sur le port 9090 !";
}

@GetMapping("/api2/products/repository")
public List<Product> getRepositoryProducts() {
    return service.getRepositoryProducts();
}

}





