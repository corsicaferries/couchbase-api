package com.neptune2.couchbase_api.filter;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import com.neptune2.couchbase_api.config.ApiKeyProperties;

public class ApiKeyFilter implements Filter {

    private final ApiKeyProperties apiKeyProperties;

    public ApiKeyFilter(ApiKeyProperties apiKeyProperties) {
        this.apiKeyProperties = apiKeyProperties;
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        String apiKey = httpRequest.getHeader("X-API-KEY");

        if (!apiKeyProperties.getApiKey().equals(apiKey)) {
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            httpResponse.getWriter().write("Unauthorized access - Invalid API Key");
            return;
        }

        chain.doFilter(request, response);
    }
}
