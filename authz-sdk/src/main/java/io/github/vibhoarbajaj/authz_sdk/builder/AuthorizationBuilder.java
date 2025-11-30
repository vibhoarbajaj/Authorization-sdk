package io.github.vibhoarbajaj.authz_sdk.builder;


import io.github.vibhoarbajaj.authz_sdk.factory.AuthorizationFactory;
import io.github.vibhoarbajaj.authz_sdk.manager.AuthorizationManager;
import io.github.vibhoarbajaj.authz_sdk.models.AuthorizationConfig;

import java.util.Map;

public class AuthorizationBuilder {

    private AuthorizationConfig config;

    public AuthorizationBuilder withConfig(AuthorizationConfig config) {
        this.config = config;
        return this;
    }

    public AuthorizationManager build() {
        initFactory(config.getConfigValues());
        return new AuthorizationManager(
                AuthorizationFactory.create(config.getStrategies())
        );
    }

    private void initFactory(Map<String, Object> configs) {
        new AuthorizationFactory(configs);
    }
}