package io.github.vibhoarbajaj.authz_sdk.strategies;

import jakarta.servlet.http.HttpServletRequest;

public class RoleBasedAuthorizationStrategy implements AuthorizationStrategy {

    @Override
    public boolean authorize(HttpServletRequest request) {
        // Example pseudo-logic: check header or attribute
        String role = request.getHeader("X-User-Role");
        return "ADMIN".equalsIgnoreCase(role);
    }
}