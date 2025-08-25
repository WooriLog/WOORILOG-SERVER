package dev.woori.wooriLog.domain.blog.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record BlogProjectDTO(
        String name,
        String summary,
        List<BlogMemberDTO> member
) {
    public static BlogProjectDTO create(
            String name,
            String summary,
            List<BlogMemberDTO> member
    ) {
        return BlogProjectDTO.builder()
                .name(name)
                .summary(summary)
                .member(member)
                .build();
    }
}
