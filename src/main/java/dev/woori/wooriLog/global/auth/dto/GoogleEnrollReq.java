package dev.woori.wooriLog.global.auth.dto;

public record GoogleEnrollReq(
        String googleToken,
        String name,
        String introduce
) {
}
