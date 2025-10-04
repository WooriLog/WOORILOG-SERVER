package dev.woori.woorilog.domain.project.dto.response;

import dev.woori.woorilog.domain.blog.dto.BlogInfoDto;
import dev.woori.woorilog.domain.member.dto.MemberInfoDto;
import dev.woori.woorilog.domain.project.dto.ProjectDetailInfoDto;

import java.util.List;

public record ProjectInfoRes(
        ProjectDetailInfoDto projectInfo,
        List<BlogInfoDto> posts,
        List<MemberInfoDto> members
) {
    public static ProjectInfoRes of(ProjectDetailInfoDto projectInfo, List<BlogInfoDto> posts, List<MemberInfoDto> members) {
        return new ProjectInfoRes(projectInfo, posts, members);
    }
}
