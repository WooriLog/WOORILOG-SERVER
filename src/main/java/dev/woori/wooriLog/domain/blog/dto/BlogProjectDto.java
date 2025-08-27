package dev.woori.wooriLog.domain.blog.dto;

import dev.woori.wooriLog.domain.member.dto.MemberInfoDto;
import lombok.Builder;

import java.util.List;

@Builder
public record BlogProjectDto(
        String name,
        String summary,
        List<MemberInfoDto> member
) {
    public static BlogProjectDto create(
            String name,
            String summary,
            List<MemberInfoDto> member
    ) {
        return BlogProjectDto.builder()
                .name(name)
                .summary(summary)
                .member(member)
                .build();
    }
}
