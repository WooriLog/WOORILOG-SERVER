package dev.woori.woorilog.domain.blog.dto.request;

import dev.woori.woorilog.domain.blog.enums.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record BlogCreateOrUpdateReq(
        @NotBlank
        String title,

        @NotBlank
        String document,

        @NotNull
        Category category,

        @NotNull
        List<String> tags,

        @NotNull
        List<ProgressDto> progresses
) {
}
