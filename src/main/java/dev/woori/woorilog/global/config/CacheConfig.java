package dev.woori.woorilog.global.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import org.jspecify.annotations.NonNull;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.caffeine.CaffeineCacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@EnableCaching
@Configuration
public class CacheConfig {

    @Bean
    public Caffeine<@NonNull Object, @NonNull Object> caffeineConfig() {
        return Caffeine.newBuilder()
                .expireAfterWrite(Duration.ofSeconds(180))
                .initialCapacity(10)
                .maximumSize(1000);
    }

    @Bean
    public CacheManager cacheManager(Caffeine<@NonNull Object, @NonNull Object> caffeine) {
        CaffeineCacheManager manager = new CaffeineCacheManager();
        manager.setCaffeine(caffeine);
        return manager;
    }
}
