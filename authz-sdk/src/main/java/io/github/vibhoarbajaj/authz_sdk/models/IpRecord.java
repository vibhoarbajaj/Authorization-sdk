package io.github.vibhoarbajaj.authz_sdk.models;

import java.time.Instant;

public record IpRecord(String ip, Instant expiry) {
    public String getIp() {
        return ip;
    }
    public Instant getExpiry() {
        return expiry;
    }
}
