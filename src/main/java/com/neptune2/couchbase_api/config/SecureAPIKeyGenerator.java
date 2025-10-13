package com.neptune2.couchbase_api.config;

import java.security.SecureRandom;
import java.util.Base64;

public class SecureAPIKeyGenerator {
    public static void main(String[] args) {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32]; // 256 bits
        random.nextBytes(bytes);
        String apiKey = Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
        System.out.println("Clé API sécurisée : " + apiKey);
    }
}
