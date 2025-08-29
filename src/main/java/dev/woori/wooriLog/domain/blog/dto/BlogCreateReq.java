package dev.woori.wooriLog.domain.blog.dto;

import dev.woori.wooriLog.domain.blog.enums.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record BlogCreateReq(
        @NotBlank
        String title,

        @NotBlank
        String document,

        @NotNull
        Category category,

        List<String> tags
) {
}
