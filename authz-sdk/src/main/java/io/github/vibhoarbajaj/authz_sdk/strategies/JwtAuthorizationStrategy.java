package io.github.vibhoarbajaj.authz_sdk.strategies;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.crypto.MACVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jwt.SignedJWT;
import io.github.vibhoarbajaj.authz_sdk.models.AuthorizationType;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.security.interfaces.RSAPublicKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JwtAuthorizationStrategy implements AuthorizationStrategy {

    private Map<String, Object> configs;
    private Map<String, Object> keyMap = new ConcurrentHashMap<>();

    public JwtAuthorizationStrategy(Map<String, Object> configs) {
        this.configs = configs;
        loadKeysFromConfig(configs);
    }

    private void loadKeysFromConfig(Map<String, Object> configs) {
        Object pubKey = configs.get("jwt_public_key");
        Object secret = configs.get("jwt_hmac_secret");
        Object jwks = configs.get("jwt_jwk_set");

        if (pubKey != null) keyMap.put("RSA", pubKey);
        if (secret != null) keyMap.put("HMAC", secret);
        if (jwks != null) keyMap.put("JWKS", jwks);
    }

    @Override
    public boolean authorize(HttpServletRequest request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated()) return true;

        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) return false;

        try {
            String token = header.substring(7);
            SignedJWT jwt = SignedJWT.parse(token);

            Date exp = jwt.getJWTClaimsSet().getExpirationTime();
            if (exp == null || exp.toInstant().isBefore(Instant.now())) return false;

            if (!verifyBasedOnHeader(jwt)) return false;
            return true;

        } catch (Exception e) {
            return false;
        }
    }

    private boolean verifyBasedOnHeader(SignedJWT jwt) throws JOSEException {
        String alg = jwt.getHeader().getAlgorithm().getName();

        if (alg.startsWith("RS")) {
            RSAPublicKey key = (RSAPublicKey) keyMap.get("RSA");
            return jwt.verify(new RSASSAVerifier(key));
        }

        if (alg.startsWith("HS")) {
            byte[] secret = keyMap.get("HMAC").toString().getBytes();
            return jwt.verify(new MACVerifier(secret));
        }

        return false;
    }

    @Override
    public String getName() {
        return AuthorizationType.JWT.name();
    }
}