package dev.woori.wooriLog.domain.blog.dto;

import dev.woori.wooriLog.domain.member.dto.MemberInfoDto;
import dev.woori.wooriLog.domain.project.entity.Project;
import lombok.Builder;

import java.util.List;

@Builder
public record BlogProjectDto(
        Long id,
        String name,
        String summary,
        List<MemberInfoDto> member
) {
    public static BlogProjectDto create(
            Project project,
            List<MemberInfoDto> member
    ) {
        return BlogProjectDto.builder()
                .id(project.getId())
                .name(project.getProjectName())
                .summary(project.getSummary())
                .member(member)
                .build();
    }
}
