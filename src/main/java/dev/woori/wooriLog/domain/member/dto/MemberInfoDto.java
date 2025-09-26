package dev.woori.wooriLog.domain.member.dto;

import dev.woori.wooriLog.domain.member.entity.Member;
import lombok.Builder;

@Builder
public record MemberInfoDto(
        Long userId,
        String profileUrl,
        String email,
        String name,
        String introduce
) {
    public static MemberInfoDto create(Member member) {
        return MemberInfoDto.builder()
                .userId(member.getId())
                .profileUrl(member.getProfileUrl())
                .email(member.getEmail())
                .name(member.getName())
                .introduce(member.getIntroduce())
                .build();
    }
}
