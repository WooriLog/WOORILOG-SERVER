package dev.woori.woorilog.domain.project.dto.response;

import dev.woori.woorilog.domain.project.dto.ProjectBasicInfoDto;
import lombok.Builder;

import java.util.List;

@Builder
public record ProjectHomeRes(
        int currentPage,
        int totalPage,
        List<ProjectBasicInfoDto> projectInfos
) {
    public static ProjectHomeRes of(int currentPage, int totalPage, List<ProjectBasicInfoDto> projectInfos) {
        return ProjectHomeRes.builder()
                .currentPage(currentPage)
                .totalPage(totalPage)
                .projectInfos(projectInfos)
                .build();
    }
}
