package dev.woori.wooriLog.domain.project.dto;

import dev.woori.wooriLog.domain.project.entity.Project;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ProjectBasicInfoDto(
        Long projectId,
        String projectName,
        Integer blogCount,
        List<String> techStack,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ProjectBasicInfoDto create(Project project, Integer blogCount) {
        return ProjectBasicInfoDto.builder()
                .projectId(project.getId())
                .projectName(project.getProjectName())
                .blogCount(blogCount)
                .techStack(project.getTechStack())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }

    public static ProjectBasicInfoDto of(Project project) {
        return ProjectBasicInfoDto.builder()
                .projectId(project.getId())
                .projectName(project.getProjectName())
                .techStack(project.getTechStack())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }
}
