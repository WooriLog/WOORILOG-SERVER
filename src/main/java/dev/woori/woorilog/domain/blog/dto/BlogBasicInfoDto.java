package dev.woori.woorilog.domain.blog.dto;

import dev.woori.woorilog.domain.blog.dto.request.ProgressDto;
import dev.woori.woorilog.domain.blog.entity.Blog;
import dev.woori.woorilog.domain.blog.entity.Progress;
import dev.woori.woorilog.domain.blog.enums.Category;
import dev.woori.woorilog.domain.member.entity.Member;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;

@Builder
public record BlogBasicInfoDto(
        Long blogId,
        String title,
        String projectName,
        String authorName,
        String authorProfileUrl,
        Category category,
        List<String> tags,
        List<ProgressDto> progresses,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static BlogBasicInfoDto create(Blog blog) {
        Member author = blog.getMember();

        return BlogBasicInfoDto.builder()
                .blogId(blog.getId())
                .title(blog.getTitle())
                .projectName(blog.getProject().getProjectName())
                .authorName(author.getName())
                .authorProfileUrl(author.getProfileUrl())
                .category(blog.getCategory())
                .tags(blog.getTags())
                .progresses(toProgressDtoList(blog.getProgresses()))
                .createdAt(blog.getCreatedAt())
                .updatedAt(blog.getUpdatedAt())
                .build();
    }

    private static List<ProgressDto> toProgressDtoList(List<Progress> progresses) {
        return progresses.stream().sorted(Comparator.comparing(Progress::getId))
                .map(ProgressDto::create)
                .toList();
    }
}
