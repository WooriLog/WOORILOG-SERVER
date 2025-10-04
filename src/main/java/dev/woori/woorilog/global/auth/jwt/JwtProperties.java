package dev.woori.woorilog.global.auth.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;


@ConfigurationProperties("jwt")
public record JwtProperties(
        String secret,
        long accessTokenExpirationTime,
        long refreshTokenExpirationTime,
        String issuer
) { }
