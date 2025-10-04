package dev.woori.woorilog.global.cache;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;
import java.util.concurrent.ConcurrentMap;

@Component
public class InMemoryEnrollCache implements EnrollCache{

    private final Cache<@NonNull String, String> cache = Caffeine.newBuilder()
            .expireAfterWrite(Duration.ofMinutes(10))
            .maximumSize(1000)
            .build();

    @Override
    public void save(String ticket, String accessToken) {
        cache.put(ticket, accessToken);
    }

    @Override
    public Optional<String> findAndConsume(String ticket) {
        ConcurrentMap<String, @NonNull String> tokenMap = cache.asMap();
        String accessToken = tokenMap.remove(ticket);
        return Optional.ofNullable(accessToken);
    }
}
