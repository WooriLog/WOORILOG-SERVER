package dev.woori.woorilog.global.cache;

import java.util.Optional;

// 추후 Redis 등 확장성을 위해 인터페이스 사용
public interface EnrollCache {

    void save(String ticket, String accessToken);

    Optional<String> findAndConsume(String ticket);
}
