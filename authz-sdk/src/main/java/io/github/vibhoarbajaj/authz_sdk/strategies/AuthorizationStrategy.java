package io.github.vibhoarbajaj.authz_sdk.strategies;

import jakarta.servlet.http.HttpServletRequest;

public interface AuthorizationStrategy {
    boolean authorize(HttpServletRequest request);
}