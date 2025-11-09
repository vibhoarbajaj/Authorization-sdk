package io.github.vibhoarbajaj.authz_sdk.factory;

import io.github.vibhoarbajaj.authz_sdk.strategies.AuthorizationStrategy;
import io.github.vibhoarbajaj.authz_sdk.strategies.JwtAuthorizationStrategy;
import io.github.vibhoarbajaj.authz_sdk.strategies.RoleBasedAuthorizationStrategy;

import java.util.*;

import static io.github.vibhoarbajaj.authz_sdk.utils.Constants.STRATEGIES;

public class AuthorizationFactory {

    private static final Map<String, AuthorizationStrategy> strategyRegistry = new HashMap<>();

    public AuthorizationFactory() {
        initializeStrategies();
    }

    public static List<AuthorizationStrategy> create(Map<String, Object> configs, Set<String> strategies) {
        if (strategies == null || strategies.isEmpty()) {
            throw new IllegalArgumentException(STRATEGIES + " is null");
        }
        List<AuthorizationStrategy> authorizationStrategies = new ArrayList<>();
        for (String key : strategies) {
            authorizationStrategies.add(strategyRegistry.get(key.toUpperCase(Locale.ROOT)));
        }
        return authorizationStrategies;
    }

    private void initializeStrategies() {
        List<AuthorizationStrategy> strategies = List.of(
                new JwtAuthorizationStrategy(),
                new RoleBasedAuthorizationStrategy()
        );
        for (AuthorizationStrategy strategy : strategies) {
            strategyRegistry.put(strategy.getName().toUpperCase(Locale.ROOT), strategy);
        }
    }
}