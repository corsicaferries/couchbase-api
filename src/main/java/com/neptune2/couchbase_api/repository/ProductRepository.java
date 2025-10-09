package com.neptune2.couchbase_api.repository;

import com.neptune2.couchbase_api.model.Product;
import org.springframework.data.couchbase.repository.CouchbaseRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends CouchbaseRepository<Product, Integer> {
}
