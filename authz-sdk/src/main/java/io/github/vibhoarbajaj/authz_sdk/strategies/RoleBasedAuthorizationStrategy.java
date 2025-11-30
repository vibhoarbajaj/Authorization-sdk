package io.github.vibhoarbajaj.authz_sdk.strategies;

import io.github.vibhoarbajaj.authz_sdk.models.AuthorizationType;
import jakarta.servlet.http.HttpServletRequest;

public class RoleBasedAuthorizationStrategy implements AuthorizationStrategy {

    @Override
    public boolean authorize(HttpServletRequest request) {
        String role = request.getHeader("X-User-Role");
        return "ADMIN".equalsIgnoreCase(role);
    }

    @Override
    public String getName() {
        return AuthorizationType.ROLE_BASED.name();
    }
}