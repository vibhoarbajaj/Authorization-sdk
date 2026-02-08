package io.github.vibhoarbajaj.authz_sdk.factory;

import io.github.vibhoarbajaj.authz_sdk.strategies.*;

import java.util.*;



public class AuthorizationFactory {

    private static final Map<String, AuthorizationStrategy> strategyRegistry = new HashMap<>();

    public AuthorizationFactory(Map<String, Object> configs) {
        initializeStrategies(configs);
    }

    public static List<AuthorizationStrategy> create(Set<String> strategies) {
        List<AuthorizationStrategy> authorizationStrategies = new ArrayList<>();
        for (String key : strategies) {
            authorizationStrategies.add(strategyRegistry.get(key.toUpperCase(Locale.ROOT)));
        }
        return authorizationStrategies;
    }

    // initializing all before start because service might want to use a strategy at runtime ,
    // could give more thought on this for performance
    private void initializeStrategies(Map<String, Object> configs) {
        List<AuthorizationStrategy> strategies = List.of(
                new JwtAuthorizationStrategy(configs),
                new RoleBasedAuthorizationStrategy(),
                new ApiKeyAuthorizationStrategy(configs),
                new IpBasedAuthorizationStrategy(configs)

        );
        for (AuthorizationStrategy strategy : strategies) {
            strategyRegistry.put(strategy.getName().toUpperCase(Locale.ROOT), strategy);
        }
    }
}