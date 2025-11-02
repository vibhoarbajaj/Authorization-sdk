package io.github.vibhoarbajaj.authz_sdk.factory;

import io.github.vibhoarbajaj.authz_sdk.utils.AuthorizationType;
import io.github.vibhoarbajaj.authz_sdk.strategies.AuthorizationStrategy;
import io.github.vibhoarbajaj.authz_sdk.strategies.JwtAuthorizationStrategy;
import io.github.vibhoarbajaj.authz_sdk.strategies.RoleBasedAuthorizationStrategy;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class AuthorizationFactory {

    public static List<AuthorizationStrategy> create(Set<AuthorizationType> types, Map<String, Object> configs) {
        List<AuthorizationStrategy> strategies = new ArrayList<>();

        for (AuthorizationType type : types) {
            switch (type) {
                case JWT -> {
                    String keyUrl = (String) configs.getOrDefault("jwtPublicKeyUrl", "");
                    strategies.add(new JwtAuthorizationStrategy(keyUrl));
                }
                case ROLE_BASED -> strategies.add(new RoleBasedAuthorizationStrategy());
            }
        }
        return strategies;
    }
}