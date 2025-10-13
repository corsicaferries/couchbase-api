package com.neptune2.couchbase_api.controller;

import com.neptune2.couchbase_api.service.ApiKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ApiKeyController {

    @Autowired
    private ApiKeyService apiKeyService;

    @GetMapping("/generate-key")
    public String getGeneratedApiKey() {
        return apiKeyService.generateApiKey();
    }
}
