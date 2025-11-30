package io.github.vibhoarbajaj.authz_sdk.strategies;

import io.github.vibhoarbajaj.authz_sdk.models.ApiKeyRecord;
import io.github.vibhoarbajaj.authz_sdk.models.AuthorizationType;
import jakarta.servlet.http.HttpServletRequest;

import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.vibhoarbajaj.authz_sdk.utils.Constants.API_KEY;
import static io.github.vibhoarbajaj.authz_sdk.utils.Constants.TTL;

public class ApiKeyAuthorizationStrategy implements AuthorizationStrategy {

    private static final ConcurrentHashMap<String, ApiKeyRecord> API_KEY_STORE = new ConcurrentHashMap<>();


    public ApiKeyAuthorizationStrategy(Map<String, Object> configs) {
        safeInitializeApiKeyStrategyValues(configs);
    }

    private void safeInitializeApiKeyStrategyValues(Map<String, Object> configs) {
        String apiKey;
        Object apiKeyObj = configs.get(API_KEY);
        if (apiKeyObj == null) {
            return;
        }
        apiKey = apiKeyObj.toString();
        Instant expiry = null;
        Object ttlObj = configs.get(TTL);
        if (ttlObj != null) {
            long ttlMillis = Long.parseLong(ttlObj.toString());
            expiry = Instant.now().plusMillis(ttlMillis);
        }
        assert apiKey != null;
        API_KEY_STORE.put(apiKey, new ApiKeyRecord(apiKey, expiry));
    }

    @Override
    public boolean authorize(HttpServletRequest request) {

        String requestKey = request.getHeader(API_KEY);
        if (requestKey == null) {
            return false;
        }

        ApiKeyRecord keyRecord = API_KEY_STORE.get(requestKey);
        if (keyRecord == null) {
            return false;
        }

        Instant expiry = keyRecord.getExpiry();
        if (expiry != null && Instant.now().isAfter(expiry)) {
            API_KEY_STORE.remove(requestKey);
            return false;
        }

        return true;
    }

    @Override
    public String getName() {
        return AuthorizationType.API_KEY.name();
    }
}