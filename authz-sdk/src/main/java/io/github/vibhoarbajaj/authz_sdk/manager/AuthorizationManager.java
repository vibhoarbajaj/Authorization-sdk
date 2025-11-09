package io.github.vibhoarbajaj.authz_sdk.manager;

import io.github.vibhoarbajaj.authz_sdk.strategies.AuthorizationStrategy;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public class AuthorizationManager {

    private final List<AuthorizationStrategy> strategies;

    public AuthorizationManager(List<AuthorizationStrategy> strategies) {
        this.strategies = strategies;
    }

    public boolean authorize(HttpServletRequest request) {
        for (AuthorizationStrategy strategy : strategies) {
            boolean result = strategy.authorize(request);
            if (!result) {
                return false;
            }
        }
        return true;
    }
}