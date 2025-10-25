package dev.woori.woorilog.domain.blog.dto;

import dev.woori.woorilog.domain.blog.dto.request.ProgressDto;
import dev.woori.woorilog.domain.blog.entity.Blog;
import dev.woori.woorilog.domain.blog.entity.Progress;
import dev.woori.woorilog.domain.blog.enums.Category;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record BlogDto(
        String title,
        List<String> tags,
        Category category,
        String document,
        List<ProgressDto> progresses,
        Long viewCount,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static BlogDto create(
            Blog blog
    ) {
        return BlogDto.builder()
                .title(blog.getTitle())
                .tags(blog.getTags())
                .category(blog.getCategory())
                .document(blog.getDocument())
                .progresses(progressToDto(blog.getProgresses()))
                .viewCount(blog.getViewCount())
                .createdAt(blog.getCreatedAt())
                .updatedAt(blog.getUpdatedAt())
                .build();
    }

    public static List<ProgressDto> progressToDto(List<Progress> progresses) {
        return progresses.stream()
                .map(ProgressDto::create)
                .toList();
    }
}
