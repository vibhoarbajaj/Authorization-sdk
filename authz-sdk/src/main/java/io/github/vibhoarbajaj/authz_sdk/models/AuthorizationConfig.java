package io.github.vibhoarbajaj.authz_sdk.models;

import java.util.Map;
import java.util.Set;


public class AuthorizationConfig {
    private Map<String, Object> configValues;
    private Set<String> strategies;

    public AuthorizationConfig(Map<String, Object> configValues, Set<String> strategies) {
        this.configValues = configValues;
        this.strategies = strategies;
    }

    public Map<String, Object> getConfigValues() {
        return configValues;
    }

    public Set<String> getStrategies() {
        return strategies;
    }
}