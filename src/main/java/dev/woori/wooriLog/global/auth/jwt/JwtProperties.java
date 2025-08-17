package dev.woori.wooriLog.global.auth.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@ConfigurationProperties("jwt")
public record JwtProperties(
        String secret,
        long accessTokenExpirationTime,
        long refreshTokenExpirationTime,
        String issuer
) { }
