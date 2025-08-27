package dev.woori.wooriLog.domain.project.dto;

import dev.woori.wooriLog.domain.member.dto.ProjectMemberDto;

import java.util.List;

public record ProjectCreateReq(
        String name,
        String summary,
        List<ProjectMemberDto> members
) {
}
