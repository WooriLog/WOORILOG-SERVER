package dev.woori.wooriLog.domain.blog.dto.response;

import dev.woori.wooriLog.domain.blog.dto.BlogDto;
import dev.woori.wooriLog.domain.blog.dto.BlogProjectDto;
import dev.woori.wooriLog.domain.member.dto.MemberInfoDto;
import lombok.Builder;

@Builder
public record BlogDetailInfoRes(
        BlogDto post,
        BlogProjectDto project,
        MemberInfoDto author
) {
    public static BlogDetailInfoRes create(
            BlogDto post,
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
