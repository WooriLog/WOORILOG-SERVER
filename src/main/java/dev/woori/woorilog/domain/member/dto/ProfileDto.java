package dev.woori.woorilog.domain.member.dto;

import dev.woori.woorilog.domain.blog.dto.BlogSummaryDto;
import dev.woori.woorilog.domain.blog.entity.Blog;
import dev.woori.woorilog.domain.member.entity.Member;
import dev.woori.woorilog.domain.project.dto.ProjectBasicInfoDto;
import dev.woori.woorilog.domain.project.entity.Project;
import lombok.Builder;

import java.util.List;

@Builder
public record ProfileDto(
        MemberInfoDto member,
        List<BlogSummaryDto> blogList,
        List<ProjectBasicInfoDto> projectList
) {
    public static ProfileDto create(Member member, List<Blog> blogs, List<Project> projects) {
        MemberInfoDto memberInfoDto = MemberInfoDto.create(member);

        List<BlogSummaryDto> blogSummaryDtos = blogs.stream()
                .map(BlogSummaryDto::create)
                .toList();

        List<ProjectBasicInfoDto> projectBasicInfoDtos = projects.stream()
                .map(ProjectBasicInfoDto::of)
                .toList();

        return ProfileDto.builder()
                .member(memberInfoDto)
                .blogList(blogSummaryDtos)
                .projectList(projectBasicInfoDtos)
                .build();
    }
}
