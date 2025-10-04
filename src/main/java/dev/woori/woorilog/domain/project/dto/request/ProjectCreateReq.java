package dev.woori.woorilog.domain.project.dto.request;

import dev.woori.woorilog.domain.member.dto.ProjectMemberDto;
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
