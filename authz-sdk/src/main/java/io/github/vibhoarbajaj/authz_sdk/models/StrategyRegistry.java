package io.github.vibhoarbajaj.authz_sdk.models;

import io.github.vibhoarbajaj.authz_sdk.strategies.*;

import java.util.Locale;
import java.util.Map;
import java.util.function.Function;

public final class StrategyRegistry {

    private static final Map<String, Function<Map<String, Object>, AuthorizationStrategy>> REGISTRY =
            Map.of(
                    "JWT", JwtAuthorizationStrategy::new,
                    "API_KEY", ApiKeyAuthorizationStrategy::new,
                    "IP", IpBasedAuthorizationStrategy::new,
                    "ROLE", cfg -> new RoleBasedAuthorizationStrategy()
            );

    private StrategyRegistry() {
    }

    public static AuthorizationStrategy create(
            String name,
            Map<String, Object> configs
    ) {
        Function<Map<String, Object>, AuthorizationStrategy> factory =
                REGISTRY.get(name.toUpperCase(Locale.ROOT));

        if (factory == null) {
            throw new IllegalArgumentException("Unknown authorization strategy: " + name);
        }

        return factory.apply(configs);
    }
}