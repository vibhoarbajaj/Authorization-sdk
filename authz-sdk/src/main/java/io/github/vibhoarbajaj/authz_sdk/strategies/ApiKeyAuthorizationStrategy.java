package io.github.vibhoarbajaj.authz_sdk.strategies;

import io.github.vibhoarbajaj.authz_sdk.utils.AuthorizationType;
import jakarta.servlet.http.HttpServletRequest;

public class ApiKeyAuthorizationStrategy implements AuthorizationStrategy {

    private static final String VALID_API_KEY = "my-secure-api-key"; // can be loaded from config/env

    @Override
    public boolean authorize(HttpServletRequest request) {
        String apiKey = request.getHeader("X-API-KEY");
        return VALID_API_KEY.equals(apiKey);
    }

    @Override
    public String getName() {
        return AuthorizationType.API_KEY.name();
    }
}