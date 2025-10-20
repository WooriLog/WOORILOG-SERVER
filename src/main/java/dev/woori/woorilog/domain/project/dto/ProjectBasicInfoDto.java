package dev.woori.woorilog.domain.project.dto;

import dev.woori.woorilog.domain.project.entity.Project;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record ProjectBasicInfoDto(
        Long projectId,
        String projectName,
        Long blogCount,
        String summary,
        List<String> techStack,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static ProjectBasicInfoDto create(Project project, Long blogCount) {
        return ProjectBasicInfoDto.builder()
                .projectId(project.getId())
                .projectName(project.getProjectName())
                .blogCount(blogCount)
                .summary(project.getSummary())
                .techStack(project.getTechStack())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }

    public static ProjectBasicInfoDto of(Project project) {
        return ProjectBasicInfoDto.builder()
                .projectId(project.getId())
                .projectName(project.getProjectName())
                .summary(project.getSummary())
                .techStack(project.getTechStack())
                .createdAt(project.getCreatedAt())
                .updatedAt(project.getUpdatedAt())
                .build();
    }
}
