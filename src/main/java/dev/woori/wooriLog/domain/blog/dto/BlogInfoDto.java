package dev.woori.wooriLog.domain.blog.dto;

import dev.woori.wooriLog.domain.blog.entity.Blog;
import dev.woori.wooriLog.domain.blog.enums.Category;
import lombok.Builder;

import java.util.List;

@Builder
public record BlogInfoDto(
        String title,
        Category category,
        Long idx,
        List<String> tags
) {
    public static BlogInfoDto create(Blog blog) {
        return BlogInfoDto.builder()
                .title(blog.getTitle())
                .category(blog.getCategory())
                .idx(blog.getIdx())
                .tags(blog.getTags())
                .build();
    }
}
