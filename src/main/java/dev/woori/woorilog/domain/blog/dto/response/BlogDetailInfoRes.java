package dev.woori.woorilog.domain.blog.dto.response;

import dev.woori.woorilog.domain.blog.dto.BlogDto;
import dev.woori.woorilog.domain.blog.dto.BlogProjectDto;
import dev.woori.woorilog.domain.member.dto.MemberInfoDto;
import lombok.Builder;

@Builder
public record BlogDetailInfoRes(
        boolean isAuthor,
        BlogDto post,
        BlogProjectDto project,
        MemberInfoDto author
) {
    public static BlogDetailInfoRes create(
            boolean isAuthor,
            BlogDto post,
            BlogProjectDto project,
            MemberInfoDto author
    ) {
        return BlogDetailInfoRes.builder()
                .isAuthor(isAuthor)
                .post(post)
                .project(project)
                .author(author)
                .build();
    }
}
