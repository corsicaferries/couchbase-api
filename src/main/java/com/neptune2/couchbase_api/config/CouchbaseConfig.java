package com.neptune2.couchbase_api.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.couchbase.repository.config.EnableCouchbaseRepositories;

import com.couchbase.client.java.Cluster;
import com.couchbase.client.java.Bucket;
import com.couchbase.client.java.env.ClusterEnvironment;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Duration;

@Configuration
@EnableCouchbaseRepositories(basePackages = "com.neptune2.couchbase_api.repository")
public class CouchbaseConfig {

    @Value("${spring.couchbase.connection-string}")
    private String connectionString;

    @Value("${spring.couchbase.username}")
    private String username;

    @Value("${spring.couchbase.password}")
    private String password;

    @Value("${spring.data.couchbase.bucket-name}")
    private String bucketName;

    /**
     * Vérifie si le TLS doit être activé (si le connection string commence par
     * couchbases://)
     */
    private boolean enableTls() {
        return connectionString.startsWith("couchbases://");
    }

    /**
     * Configure et crée le cluster Couchbase avec ou sans TLS
     */
    @Bean
    public Cluster couchbaseCluster() {
        try {
            ClusterEnvironment environment;

            if (enableTls()) {
                ClassPathResource certResource = new ClassPathResource("couchbase-cert.pem");

                System.out.println("TLS activé ? " + enableTls());
                System.out.println("Connection string = " + connectionString);
                System.out.println("Certificat trouvé ? " + certResource.exists());

                if (!certResource.exists()) {
                    throw new IllegalStateException("Certificat Couchbase introuvable dans src/main/resources !");
                }

                // Copier le certificat dans un fichier temporaire pour créer le Path attendu
                Path tempCert = Files.createTempFile("couchbase-cert", ".pem");
                try (InputStream is = certResource.getInputStream()) {
                    Files.copy(is, tempCert, StandardCopyOption.REPLACE_EXISTING);
                }

                environment = ClusterEnvironment.builder()
                        .securityConfig(security -> security
                                .enableTls(true)
                                .trustCertificate(tempCert))
                        .build();
            } else {
                environment = ClusterEnvironment.builder().build();
            }

            return Cluster.connect(
                    connectionString,
                    com.couchbase.client.java.ClusterOptions.clusterOptions(username, password)
                            .environment(environment));
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du chargement du certificat Couchbase", e);
        }
    }

    @Bean(name = "Neptunedev")
    public Bucket neptunedevBucket(Cluster cluster) {
        Bucket bucket = cluster.bucket(bucketName);
        bucket.waitUntilReady(Duration.ofSeconds(10));
        return bucket;
    }
}
