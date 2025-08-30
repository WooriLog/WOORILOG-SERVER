package dev.woori.wooriLog.domain.blog.dto.response;

import dev.woori.wooriLog.domain.blog.dto.BlogPostDto;
import dev.woori.wooriLog.domain.blog.dto.BlogProjectDto;
import dev.woori.wooriLog.domain.member.dto.MemberInfoDto;
import lombok.Builder;

@Builder
public record BlogDetailInfoRes(
        BlogPostDto post,
        BlogProjectDto project,
        MemberInfoDto author
) {
    public static BlogDetailInfoRes create(
            BlogPostDto post,
            BlogProjectDto project,
            MemberInfoDto author
    ) {
        return BlogDetailInfoRes.builder()
                .post(post)
                .project(project)
                .author(author)
                .build();
    }
}
