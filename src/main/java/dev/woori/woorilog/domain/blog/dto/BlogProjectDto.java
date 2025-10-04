package dev.woori.woorilog.domain.blog.dto;

import dev.woori.woorilog.domain.member.dto.MemberInfoDto;
import dev.woori.woorilog.domain.project.entity.Project;
import lombok.Builder;

import java.util.List;

@Builder
public record BlogProjectDto(
        Long projectId,
        String projectName,
        String summary,
        List<MemberInfoDto> member
) {
    public static BlogProjectDto create(
            Project project,
            List<MemberInfoDto> member
    ) {
        return BlogProjectDto.builder()
                .projectId(project.getId())
                .projectName(project.getProjectName())
                .summary(project.getSummary())
                .member(member)
                .build();
    }
}
