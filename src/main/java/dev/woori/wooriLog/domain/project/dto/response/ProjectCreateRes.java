package dev.woori.wooriLog.domain.project.dto.response;

public record ProjectCreateRes(
        Long projectId
) {
    public static ProjectCreateRes from(Long projectId) {
        return new ProjectCreateRes(projectId);
    }
}
