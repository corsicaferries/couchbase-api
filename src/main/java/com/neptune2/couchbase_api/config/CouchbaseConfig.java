package com.neptune2.couchbase_api.config;

import org.springframework.context.annotation.Bean;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Bucket;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;
import java.time.Duration;

@Configuration
@EnableCouchbaseRepositories(basePackages = "com.neptune2.couchbase_api.repository")
public class CouchbaseConfig extends AbstractCouchbaseConfiguration {

    @Override
    public String getConnectionString() {
        return "couchbase://172.20.28.3"; // adapte à ton serveur
    }

    @Override
    public String getUserName() {
        return "neptune-dev"; // ton utilisateur Couchbase
    }

    @Override
    public String getPassword() {
        return "ZdSlvjQlFVykemf7m9ww"; // ton mot de passe
    }

    @Override
    public String getBucketName() {
        return "ClickAndCollect"; // ton bucket
    }
      // Spécifie le scope
    @Override
    public String getScopeName() {
        return "repository";  // remplace par le scope réel de ton bucket
    }
  @Bean
    public Cluster couchbaseCluster() {
        return Cluster.connect(getConnectionString(), getUserName(), getPassword());
    }

@Bean
public Bucket clickAndCollectBucket(Cluster cluster) {
    Bucket bucket = cluster.bucket("ClickAndCollect");
    bucket.waitUntilReady(Duration.ofSeconds(10));
    return bucket;
}

    
}
