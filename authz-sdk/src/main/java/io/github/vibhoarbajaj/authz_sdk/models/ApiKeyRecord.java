package io.github.vibhoarbajaj.authz_sdk.models;

import java.io.Serializable;
import java.time.Instant;

public record ApiKeyRecord(String apiKey, Instant expiry) implements Serializable {
    public Instant getExpiry() {
        return expiry;
    }

    public String getApiKey() {
        return apiKey;
    }
}