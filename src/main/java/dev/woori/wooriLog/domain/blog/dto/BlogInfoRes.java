package dev.woori.wooriLog.domain.blog.dto;

import lombok.Builder;

@Builder
public record BlogInfoRes(
        BlogPostDto post,
        BlogProjectDto project,
        BlogMemberDto author
) {
    public static BlogInfoRes create(
            BlogPostDto post,
            BlogProjectDto project,
            BlogMemberDto author
    ) {
        return BlogInfoRes.builder()
                .post(post)
                .project(project)
                .author(author)
                .build();
    }
}
