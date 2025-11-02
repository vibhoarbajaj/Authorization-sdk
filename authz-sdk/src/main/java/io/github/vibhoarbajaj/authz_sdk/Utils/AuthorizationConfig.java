package io.github.vibhoarbajaj.authz_sdk.Utils;

import java.util.Map;
import java.util.Set;

public class AuthorizationConfig {
    private Set<AuthorizationType> enabledAuthTypes;
    private Map<String, Object> configValues;

    public AuthorizationConfig(Set<AuthorizationType> enabledAuthTypes, Map<String, Object> configValues) {
        this.enabledAuthTypes = enabledAuthTypes;
        this.configValues = configValues;
    }

    public Set<AuthorizationType> getEnabledAuthTypes() {
        return enabledAuthTypes;
    }

    public Map<String, Object> getConfigValues() {
        return configValues;
    }
}