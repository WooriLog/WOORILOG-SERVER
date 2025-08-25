package dev.woori.wooriLog.domain.blog.dto;

import dev.woori.wooriLog.domain.blog.enums.Category;
import lombok.Builder;

import java.util.List;

@Builder
public record BlogCreateReq(
        String document,
        String title,
        Category category,
        List<String> tags
) {
    public BlogCreateReq create(
            String document,
            String title,
            Category category,
            List<String> tags
    ) {
        return BlogCreateReq.builder()
                .document(document)
                .title(title)
                .category(category)
                .tags(tags)
                .build();
    }
}
