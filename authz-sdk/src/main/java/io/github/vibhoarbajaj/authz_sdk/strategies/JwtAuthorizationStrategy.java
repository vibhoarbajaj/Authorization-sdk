package io.github.vibhoarbajaj.authz_sdk.strategies;

import com.nimbusds.jwt.SignedJWT;
import io.github.vibhoarbajaj.authz_sdk.utils.AuthorizationType;
import jakarta.servlet.http.HttpServletRequest;

import java.text.ParseException;
import java.util.List;

public class JwtAuthorizationStrategy implements AuthorizationStrategy {
    @Override
    public boolean authorize(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return false;
        }
        try {
            String token = authHeader.substring(7);
            SignedJWT jwt = SignedJWT.parse(token);
            String role = jwt.getJWTClaimsSet().getStringClaim("role");
            if (role == null) {
                List<String> roles = jwt.getJWTClaimsSet().getStringListClaim("roles");
                role = (roles != null && !roles.isEmpty()) ? roles.get(0) : null;
            }

            if (role == null) {
                return false;
            }

            return role.equalsIgnoreCase("ADMIN") || role.equalsIgnoreCase("USER");
        } catch (ParseException e) {
            return false;
        }
    }

    @Override
    public String getName() {
        return AuthorizationType.JWT.name();
    }
}
