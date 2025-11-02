package io.github.vibhoarbajaj.authz_sdk.strategies;

import java.util.Set;

public interface AuthorizationStrategyProvider {
    Set<String> supportedTypes();

    AuthorizationStrategy createInstance(Object config);
}