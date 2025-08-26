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
}
