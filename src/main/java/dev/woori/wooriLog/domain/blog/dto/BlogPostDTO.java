package dev.woori.wooriLog.domain.blog.dto;

import dev.woori.wooriLog.domain.blog.enums.Category;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.List;

@Builder
public record BlogPostDTO(
        String title,
        List<String> tags,
        Category category,
        String document,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
    public static BlogPostDTO create(
            String title,
            List<String> tags,
            Category category,
            String document,
            LocalDateTime createdAt,
            LocalDateTime updatedAt
    ) {
        return BlogPostDTO.builder()
                .title(title)
                .tags(tags)
                .category(category)
                .document(document)
                .createdAt(createdAt)
                .updatedAt(updatedAt)
                .build();
    }
}
