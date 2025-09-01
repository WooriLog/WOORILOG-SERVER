package dev.woori.wooriLog.domain.blog.dto;

import dev.woori.wooriLog.domain.blog.entity.Blog;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record BlogSummaryDto(
        Long blogId,
        String title,
        List<String> tags,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static BlogSummaryDto create(
            Blog blog
    ) {
        return BlogSummaryDto.builder()
                .blogId(blog.getId())
                .title(blog.getTitle())
                .tags(blog.getTags())
                .createdAt(blog.getCreatedAt())
                .updatedAt(blog.getUpdatedAt())
                .build();
    }
}
