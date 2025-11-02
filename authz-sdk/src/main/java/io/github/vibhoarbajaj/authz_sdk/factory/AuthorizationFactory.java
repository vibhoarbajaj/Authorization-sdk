package io.github.vibhoarbajaj.authz_sdk.factory;

import io.github.vibhoarbajaj.authz_sdk.strategies.AuthorizationStrategy;
import io.github.vibhoarbajaj.authz_sdk.utils.AuthorizationType;

import java.util.*;

import static io.github.vibhoarbajaj.authz_sdk.utils.Constants.STRATEGIES;

public class AuthorizationFactory {

    private static final Map<String, AuthorizationStrategy> strategyRegistry = new HashMap<>();

    AuthorizationFactory(List<AuthorizationStrategy> authorizationStrategies) {
        for (AuthorizationStrategy strategy : authorizationStrategies) {
            strategyRegistry.put(strategy.getName().toUpperCase(Locale.ROOT), strategy);
        }
    }

    public static List<AuthorizationStrategy> create(Set<AuthorizationType> types, Map<String, Object> configs, Set<String> strategies) {
        if (strategies == null || strategies.isEmpty()) {
            throw new IllegalArgumentException(STRATEGIES + " is null");
        }
        List<AuthorizationStrategy> authorizationStrategies = new ArrayList<>();
        for (String key : strategies) {
            authorizationStrategies.add(strategyRegistry.get(key.toUpperCase(Locale.ROOT)));
        }
        return authorizationStrategies;
    }
}