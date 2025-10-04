package dev.woori.woorilog.domain.blog.dto;

import dev.woori.woorilog.domain.blog.entity.Blog;
import dev.woori.woorilog.domain.blog.enums.Category;
import lombok.Builder;

import java.util.List;

@Builder
public record BlogInfoDto(
        Long blogId,
        String title,
        Category category,
        Long idx,
        List<String> tags
) {
    public static BlogInfoDto create(Blog blog) {
        return BlogInfoDto.builder()
                .blogId(blog.getId())
                .title(blog.getTitle())
                .category(blog.getCategory())
                .idx(blog.getIdx())
                .tags(blog.getTags())
                .build();
    }
}
