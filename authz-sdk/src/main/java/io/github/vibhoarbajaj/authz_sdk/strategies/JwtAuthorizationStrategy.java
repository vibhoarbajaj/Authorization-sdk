package io.github.vibhoarbajaj.authz_sdk.strategies;

import jakarta.servlet.http.HttpServletRequest;

public class JwtAuthorizationStrategy implements AuthorizationStrategy {
    private final String publicKeyUrl;

    public JwtAuthorizationStrategy(String publicKeyUrl) {
        this.publicKeyUrl = publicKeyUrl;
    }

    @Override
    public boolean authorize(HttpServletRequest request) {
        // Example pseudo-logic
        String token = request.getHeader("Authorization");
        if (token == null || !token.startsWith("Bearer ")) return false;

        // TODO: validate JWT using publicKeyUrl
        return true;
    }
}
