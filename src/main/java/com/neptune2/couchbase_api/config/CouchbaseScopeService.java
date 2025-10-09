package com.neptune2.couchbase_api.config;

import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Bucket;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import java.time.Duration;
import com.couchbase.client.java.Collection;

@Service
public class CouchbaseScopeService {

    private final Cluster cluster;
    private final Bucket bucket;
    
    public CouchbaseScopeService(Cluster cluster,
                                 @Qualifier("clickAndCollectBucket") Bucket bucket) {
        this.cluster = cluster;
        this.bucket = bucket;
        this.bucket.waitUntilReady(Duration.ofSeconds(10)); // OK maintenant
    }

    public Collection getRepositoryCollection() {
                return bucket.scope("repository").collection("product");
    }

    public Collection getDatamodelCollection() {
        return bucket.scope("datamodel").collection("product");
    }
}
