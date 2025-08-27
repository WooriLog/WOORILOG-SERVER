package dev.woori.wooriLog.domain.project.dto;

import dev.woori.wooriLog.domain.blog.dto.BlogInfoDto;
import dev.woori.wooriLog.domain.member.dto.MemberInfoDto;

import java.util.List;

public record ProjectInfoRes(
        ProjectInfoDto projectInfo,
        List<BlogInfoDto> posts,
        List<MemberInfoDto> members
) {
    public static ProjectInfoRes of(ProjectInfoDto projectInfo, List<BlogInfoDto> posts, List<MemberInfoDto> members) {
        return new ProjectInfoRes(projectInfo, posts, members);
    }
}
