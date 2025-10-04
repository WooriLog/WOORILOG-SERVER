package dev.woori.woorilog.domain.member.dto;

public record ProjectMemberDto(
        Long userId,
        String email,
        String role
) {
}
