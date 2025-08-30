package dev.woori.wooriLog.domain.member.dto;

import dev.woori.wooriLog.domain.blog.dto.BlogInfoDto;
import dev.woori.wooriLog.domain.blog.dto.BlogSummaryDto;
import dev.woori.wooriLog.domain.blog.entity.Blog;
import dev.woori.wooriLog.domain.member.entity.Member;
import dev.woori.wooriLog.domain.project.dto.ProjectSummaryDto;
import dev.woori.wooriLog.domain.project.entity.Project;
import lombok.Builder;

import java.util.List;

@Builder
public record ProfileDto(
        Long userId,
        String email,
        String name,
        String introduce,
        List<BlogSummaryDto> blogList,
        List<ProjectSummaryDto> projectList
) {
    public static ProfileDto create(Member member, List<Blog> blogs, List<Project> projects) {
        List<BlogSummaryDto> blogSummaryDtos = blogs.stream()
                .map(BlogSummaryDto::create)
                .toList();
        List<ProjectSummaryDto> projectSummaryDtos = projects.stream()
                .map(ProjectSummaryDto::create)
                .toList();
        return ProfileDto.builder()
                .userId(member.getId())
                .email(member.getEmail())
                .name(member.getName())
                .introduce(member.getIntroduce())
                .blogList(blogSummaryDtos)
                .projectList(projectSummaryDtos)
                .build();
    }
}
