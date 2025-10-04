package dev.woori.woorilog.global.auth.dto;

public record GoogleUserInfoRes(
        String sub,
        String email,
        Boolean email_verified,
        String name,
        String given_name,
        String family_name,
        String picture,
        String locale
) {
}
