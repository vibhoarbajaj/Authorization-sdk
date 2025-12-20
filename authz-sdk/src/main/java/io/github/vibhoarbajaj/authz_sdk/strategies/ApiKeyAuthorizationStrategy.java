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

    private static void putInMap(Iterable<?> apiKeyObj, Instant expiry) {
        for (Object keyObj : apiKeyObj) {
            if (keyObj != null) {
                String key = keyObj.toString().trim();
                if (!key.isEmpty()) {
                    API_KEY_STORE.put(key, new ApiKeyRecord(key, expiry));
                }
            }
        }
    }

    private static void putInMapBySplitting(String apiKeyStr, Instant expiry) {
        for (String key : apiKeyStr.split(",")) {
            String trimmedKey = key.trim();
            if (!trimmedKey.isEmpty()) {
                API_KEY_STORE.put(trimmedKey, new ApiKeyRecord(trimmedKey, expiry));
            }
        }
    }

    private void safeInitializeApiKeyStrategyValues(Map<String, Object> configs) {
        Object apiKeyObj = configs.get(API_KEY);
        if (apiKeyObj == null) {
            return;
        }
        Instant expiry = null;
        Object ttlObj = configs.get(TTL);
        if (ttlObj != null) {
            long ttlMillis = Long.parseLong(ttlObj.toString());
            expiry = Instant.now().plusMillis(ttlMillis);
        }
        if (apiKeyObj instanceof String apiKeyStr) {
            putInMapBySplitting(apiKeyStr, expiry);
        } else if (apiKeyObj instanceof Iterable<?>) {
            putInMap((Iterable<?>) apiKeyObj, expiry);
        } else {
            throw new IllegalArgumentException("Unsupported API_KEY config type");
        }
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