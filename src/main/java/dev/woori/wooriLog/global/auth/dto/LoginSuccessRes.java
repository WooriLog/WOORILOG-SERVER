package dev.woori.wooriLog.global.auth.dto;

import dev.woori.wooriLog.domain.member.entity.Member;
import dev.woori.wooriLog.global.auth.jwt.Token;
import lombok.Builder;

@Builder
public record LoginSuccessRes(
        String username,
        String email,
        String accessToken,
        String refreshToken
) {
    public static LoginSuccessRes create(Member member, Token token) {
        return LoginSuccessRes.builder()
                .username(member.getName())
                .email(member.getEmail())
                .accessToken(token.getAccessToken())
                .refreshToken(token.getRefreshToken())
                .build();
    }
}
