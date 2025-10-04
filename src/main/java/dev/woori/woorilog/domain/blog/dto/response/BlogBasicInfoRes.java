package dev.woori.woorilog.domain.blog.dto.response;

import dev.woori.woorilog.domain.blog.dto.request.ProgressDto;
import dev.woori.woorilog.domain.blog.entity.Blog;
import dev.woori.woorilog.domain.blog.entity.Progress;
import dev.woori.woorilog.domain.blog.enums.Category;
import dev.woori.woorilog.domain.member.entity.Member;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record BlogBasicInfoRes(
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
    public static BlogBasicInfoRes create(Blog blog) {
        Member author = blog.getMember();

        return BlogBasicInfoRes.builder()
                .blogId(blog.getId())
                .title(blog.getTitle())
                .projectName(blog.getProject().getProjectName())
                .authorName(author.getName())
                .authorProfileUrl(author.getProfileUrl())
                .category(blog.getCategory())
                .tags(blog.getTags())
                .progresses(transProgressDtos(blog.getProgresses()))
                .createdAt(blog.getCreatedAt())
                .updatedAt(blog.getUpdatedAt())
                .build();
    }

    private static List<ProgressDto> transProgressDtos (List<Progress> progresses) {
        return progresses.stream()
                .map(ProgressDto::create)
                .toList();
    }
}
