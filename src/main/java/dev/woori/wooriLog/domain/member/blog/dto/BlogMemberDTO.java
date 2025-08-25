package dev.woori.wooriLog.domain.member.blog.dto;

import dev.woori.wooriLog.domain.member.entity.Member;
import lombok.Builder;

@Builder
public record BlogMemberDTO(
        Long userId,
        String name,
        String introduce
) {
    public static BlogMemberDTO create(Member member) {
        return BlogMemberDTO.builder()
                .userId(member.getId())
                .name(member.getName())
                .introduce(member.getIntroduce())
                .build();
    }
}
