package dev.woori.wooriLog.domain.blog.dto;

import dev.woori.wooriLog.domain.blog.entity.Blog;
import dev.woori.wooriLog.domain.blog.enums.Category;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record BlogPostDto(
        String title,
        List<String> tags,
        Category category,
        String document,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static BlogPostDto create(
            Blog blog
    ) {
        return BlogPostDto.builder()
                .title(blog.getTitle())
                .tags(blog.getTags())
                .category(blog.getCategory())
                .document(blog.getDocument())
                .createdAt(blog.getCreatedAt())
                .updatedAt(blog.getUpdatedAt())
                .build();
    }
}
