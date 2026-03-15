package io.github.vibhoarbajaj.authz_sdk.builder;


import io.github.vibhoarbajaj.authz_sdk.factory.AuthorizationFactory;
import io.github.vibhoarbajaj.authz_sdk.manager.AuthorizationManager;
import io.github.vibhoarbajaj.authz_sdk.models.AuthorizationConfig;
import io.github.vibhoarbajaj.authz_sdk.models.StrategyRegistry;
import io.github.vibhoarbajaj.authz_sdk.strategies.AuthorizationStrategy;

import java.util.List;
import java.util.Map;

public class AuthorizationBuilder {

    private AuthorizationConfig config;

    public AuthorizationBuilder withConfig(AuthorizationConfig config) {
        this.config = config;
        return this;
    }
    this is a test error
    public AuthorizationManager build() {
        List<AuthorizationStrategy> strategies =
                config.getStrategies().stream()
                        .map(s -> StrategyRegistry.create(s, config.getConfigValues()))
                        .toList();
        return new AuthorizationManager(strategies);
    }

    private void initFactory(Map<String, Object> configs) {
        new AuthorizationFactory(configs);
    }
}