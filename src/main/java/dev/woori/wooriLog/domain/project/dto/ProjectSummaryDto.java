package dev.woori.wooriLog.domain.project.dto;

import dev.woori.wooriLog.domain.project.entity.Project;
import lombok.Builder;

import java.util.List;

@Builder
public record ProjectSummaryDto(
        Long projectId,
        String projectName,
        List<String> techStack
) {
    public static ProjectSummaryDto create(Project project) {
        return ProjectSummaryDto.builder()
                .projectId(project.getId())
                .projectName(project.getProjectName())
                .techStack(project.getTechStack())
                .build();
    }
}
