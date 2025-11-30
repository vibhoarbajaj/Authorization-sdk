package io.github.vibhoarbajaj.authz_sdk.strategies;

import io.github.vibhoarbajaj.authz_sdk.models.AuthorizationType;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Set;

public class IpBasedAuthorizationStrategy implements AuthorizationStrategy {

    private static final Set<String> ALLOWED_IPS = Set.of("127.0.0.1", "10.0.0.5");

    @Override
    public boolean authorize(HttpServletRequest request) {
        String ip = request.getRemoteAddr();
        return ALLOWED_IPS.contains(ip);
    }

    @Override
    public String getName() {
        return AuthorizationType.IP_BASED.name();
    }
}