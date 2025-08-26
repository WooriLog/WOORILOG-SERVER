package dev.woori.wooriLog.domain.blog.dto;

import dev.woori.wooriLog.domain.member.entity.Member;
import lombok.Builder;

@Builder
public record BlogMemberDto(
        Long userId,
        String name,
        String introduce
) {
    public static BlogMemberDto create(Member member) {
        return BlogMemberDto.builder()
                .userId(member.getId())
                .name(member.getName())
                .introduce(member.getIntroduce())
                .build();
    }
}
