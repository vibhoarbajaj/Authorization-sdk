package io.github.vibhoarbajaj.authz_sdk.strategies;

import io.github.vibhoarbajaj.authz_sdk.models.AuthorizationType;
import io.github.vibhoarbajaj.authz_sdk.models.IpRecord;
import jakarta.servlet.http.HttpServletRequest;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import static io.github.vibhoarbajaj.authz_sdk.utils.Constants.IP_LIST;
import static io.github.vibhoarbajaj.authz_sdk.utils.Constants.TTL;

public class IpBasedAuthorizationStrategy implements AuthorizationStrategy {

    private static final ConcurrentHashMap<String, IpRecord> IP_STORE = new ConcurrentHashMap<>();

    public IpBasedAuthorizationStrategy(Map<String, Object> configs) {
        safeInitializeIpStrategyValues(configs);
    }

    private void safeInitializeIpStrategyValues(Map<String, Object> configs) {
        Object ipListObj = configs.get(IP_LIST);
        if (ipListObj == null) {
            return;
        }

        List<String> ips;

        if (ipListObj instanceof List<?> raw) {
            ips = raw.stream().map(Object::toString).toList();
        } else {
            ips = List.of(ipListObj.toString());
        }

        Instant expiry = null;

        Object ttlObj = configs.get(TTL);
        if (ttlObj != null) {
            long ttlMillis = Long.parseLong(ttlObj.toString());
            expiry = Instant.now().plusMillis(ttlMillis);
        }

        for (String ip : ips) {
            IP_STORE.put(ip, new IpRecord(ip, expiry));
        }
    }

    @Override
    public boolean authorize(HttpServletRequest request) {

        String requestIp = request.getRemoteAddr();
        IpRecord record = IP_STORE.get(requestIp);

        if (record == null) {
            return false;
        }

        Instant expiry = record.getExpiry();
        if (expiry != null && Instant.now().isAfter(expiry)) {
            IP_STORE.remove(requestIp);
            return false;
        }

        return true;
    }

    @Override
    public String getName() {
        return AuthorizationType.IP_BASED.name();
    }
}