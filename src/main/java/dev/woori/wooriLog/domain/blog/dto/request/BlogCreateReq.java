package dev.woori.wooriLog.domain.blog.dto.request;

import dev.woori.wooriLog.domain.blog.entity.Progress;
import dev.woori.wooriLog.domain.blog.enums.Category;
import dev.woori.wooriLog.domain.blog.enums.ProgressValue;
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

        @NotNull
        List<String> tags,

        @NotNull
        List<ProgressDto> progresses
) {
}
