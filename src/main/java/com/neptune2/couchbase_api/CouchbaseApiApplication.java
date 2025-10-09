package com.neptune2.couchbase_api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication  // <-- pas besoin de scanBasePackages ici
public class CouchbaseApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(CouchbaseApiApplication.class, args);
    }
}
