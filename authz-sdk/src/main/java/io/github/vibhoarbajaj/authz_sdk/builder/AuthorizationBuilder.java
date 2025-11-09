package io.github.vibhoarbajaj.authz_sdk.builder;


import io.github.vibhoarbajaj.authz_sdk.utils.AuthorizationConfig;
import io.github.vibhoarbajaj.authz_sdk.factory.AuthorizationFactory;
import io.github.vibhoarbajaj.authz_sdk.manager.AuthorizationManager;

public class AuthorizationBuilder {

    private AuthorizationConfig config;

    public AuthorizationBuilder withConfig(AuthorizationConfig config) {
        this.config = config;
        return this;
    }

    public AuthorizationManager build() {
        return new AuthorizationManager(
                AuthorizationFactory.create(config.getConfigValues(), config.getStrategies())
        );
    }
}