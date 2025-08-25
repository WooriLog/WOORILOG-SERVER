package dev.woori.wooriLog.domain.blog.dto;

import lombok.Builder;

@Builder
public record BlogInfoRes(
        BlogPostDTO post,
        BlogProjectDTO project,
        BlogMemberDTO author
) {
    public static BlogInfoRes create(
            BlogPostDTO post,
            BlogProjectDTO project,
            BlogMemberDTO author
    ) {
        return BlogInfoRes.builder()
                .post(post)
                .project(project)
                .author(author)
                .build();
    }
}
