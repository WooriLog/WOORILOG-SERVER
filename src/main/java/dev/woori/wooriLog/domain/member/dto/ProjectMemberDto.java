package dev.woori.wooriLog.domain.member.dto;

public record ProjectMemberDto(
        Long userId,
        String email,
        String role
) {
}
