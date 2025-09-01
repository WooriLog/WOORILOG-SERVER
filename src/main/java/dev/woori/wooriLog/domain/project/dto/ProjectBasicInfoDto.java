package dev.woori.wooriLog.domain.project.dto;

import dev.woori.wooriLog.domain.project.entity.Project;
import lombok.Builder;

import java.util.List;

@Builder
public record ProjectBasicInfoDto(
        Long projectId,
        String projectName,
        List<String> techStack
) {
    public static ProjectBasicInfoDto create(Project project) {
        return ProjectBasicInfoDto.builder()
                .projectId(project.getId())
                .projectName(project.getProjectName())
                .techStack(project.getTechStack())
                .build();
    }
}
