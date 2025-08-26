package dev.woori.wooriLog.domain.blog.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record BlogProjectDto(
        String name,
        String summary,
        List<BlogMemberDto> member
) {
    public static BlogProjectDto create(
            String name,
            String summary,
            List<BlogMemberDto> member
    ) {
        return BlogProjectDto.builder()
                .name(name)
                .summary(summary)
                .member(member)
                .build();
    }
}
