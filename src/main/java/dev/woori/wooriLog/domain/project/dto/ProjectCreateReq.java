package dev.woori.wooriLog.domain.project.dto;

import dev.woori.wooriLog.domain.member.dto.ProjectMemberDto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record ProjectCreateReq(
        @NotBlank
        String name,

        String summary,

        @NotNull
        List<ProjectMemberDto> members
) {
}
