package dev.woori.wooriLog.domain.project.dto;

import dev.woori.wooriLog.domain.project.entity.Project;
import lombok.Builder;

import java.util.List;

@Builder
public record ProjectInfoDto(
        Long projectId,
        String projectName,
        String summary,
        String readme,
        List<String> techStack
) {
    public static ProjectInfoDto create(Project project) {
        return ProjectInfoDto.builder()
                .projectId(project.getId())
                .projectName(project.getProjectName())
                .summary(project.getSummary())
                .readme(project.getReadMe())
                .techStack(project.getTechStack())
                .build();
    }
}
