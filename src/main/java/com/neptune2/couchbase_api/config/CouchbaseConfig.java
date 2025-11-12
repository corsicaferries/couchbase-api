package com.neptune2.couchbase_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.couchbase.config.AbstractCouchbaseConfiguration;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;
import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.env.ClusterEnvironment;

import com.couchbase.client.java.Bucket;

import java.time.Duration;

@Configuration
@EnableCouchbaseRepositories(basePackages = "com.neptune2.couchbase_api.repository")
public class CouchbaseConfig extends AbstractCouchbaseConfiguration {

    @Value("${spring.couchbase.connection-string}")
    private String connectionString;

    @Value("${spring.couchbase.username}")
    private String username;

    @Value("${spring.couchbase.password}")
    private String password;

    @Value("${spring.data.couchbase.bucket-name}")
    private String bucketName;

    @Override
    public String getConnectionString() {
        return connectionString;
    }

    @Override
    public String getUserName() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getBucketName() {
        return bucketName;
    }

    public Boolean enableTls() {
        return getConnectionString().startsWith("couchbases://");
    }

    @Bean
    public Cluster couchbaseCluster() {
        // SANS TLS
        // return Cluster.connect(getConnectionString(), getUserName(), getPassword());

        ClusterEnvironment environment = ClusterEnvironment.builder()
                .securityConfig(security -> security.enableTls(enableTls()))
                .build();

        return Cluster.connect(
                getConnectionString(),
                com.couchbase.client.java.ClusterOptions.clusterOptions(getUserName(), getPassword())
                        .environment(environment));

    }

    @Bean
    public Bucket clickAndCollectBucket(Cluster cluster) {
        Bucket bucket = cluster.bucket(getBucketName());
        bucket.waitUntilReady(Duration.ofSeconds(10));
        return bucket;
    }
}
