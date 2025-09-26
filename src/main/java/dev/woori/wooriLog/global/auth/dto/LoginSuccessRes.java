package dev.woori.wooriLog.global.auth.dto;

import dev.woori.wooriLog.domain.member.entity.Member;
import dev.woori.wooriLog.global.auth.jwt.Token;
import lombok.Builder;

@Builder
public record LoginSuccessRes(
        Long userId,
        String username,
        String email,
        String profileUrl,
        String accessToken,
        String refreshToken
) {
    public static LoginSuccessRes create(Member member, Token token) {
        return LoginSuccessRes.builder()
                .userId(member.getId())
                .username(member.getName())
                .email(member.getEmail())
                .profileUrl(member.getProfileUrl())
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .build();
    }
}
