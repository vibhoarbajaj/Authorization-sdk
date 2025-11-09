package io.github.vibhoarbajaj.authz_sdk.strategies;

import io.github.vibhoarbajaj.authz_sdk.utils.AuthorizationType;
import jakarta.servlet.http.HttpServletRequest;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

public class HmacAuthorizationStrategy implements AuthorizationStrategy {

    private static final String SECRET = "my-hmac-secret";

    @Override
    public boolean authorize(HttpServletRequest request) {
        String signature = request.getHeader("X-Signature");
        String payload = request.getHeader("X-Payload");

        if (signature == null || payload == null) {
            return false;
        }

        try {
            Mac mac = Mac.getInstance("HmacSHA256");
            mac.init(new SecretKeySpec(SECRET.getBytes(StandardCharsets.UTF_8), "HmacSHA256"));
            String computed = Base64.getEncoder().encodeToString(mac.doFinal(payload.getBytes(StandardCharsets.UTF_8)));
            return computed.equals(signature);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public String getName() {
        return AuthorizationType.HMAC.name();
    }
}