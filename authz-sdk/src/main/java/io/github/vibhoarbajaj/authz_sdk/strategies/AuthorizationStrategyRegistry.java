package io.github.vibhoarbajaj.authz_sdk.strategies;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AuthorizationStrategyRegistry {

    private static final Map<String, AuthorizationStrategyProvider> PROVIDERS = new ConcurrentHashMap<>();

    private AuthorizationStrategyRegistry() {}

    public static void register(String name, AuthorizationStrategyProvider provider) {
        PROVIDERS.put(name.toUpperCase(), provider);
    }

    public static AuthorizationStrategyProvider get(String name) {
        return PROVIDERS.get(name.toUpperCase());
    }

    public static boolean exists(String name) {
        return PROVIDERS.containsKey(name.toUpperCase());
    }
}