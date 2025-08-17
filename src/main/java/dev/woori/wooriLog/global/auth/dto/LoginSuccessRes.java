package dev.woori.wooriLog.global.auth.dto;

import dev.woori.wooriLog.domain.member.entity.Member;
import lombok.Builder;

@Builder
public record LoginSuccessRes(
        String username,
        String email,
        String accessToken,
        String refreshToken
) {

    public static LoginSuccessRes create(Member member, String accessToken, String refreshToken) {
        return LoginSuccessRes.builder()
                .username(member.getName())
                .email(member.getEmail())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }
}
