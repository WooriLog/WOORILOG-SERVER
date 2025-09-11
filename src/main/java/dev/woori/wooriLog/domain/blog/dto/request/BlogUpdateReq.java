package dev.woori.wooriLog.domain.blog.dto.request;

import dev.woori.wooriLog.domain.blog.enums.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public record BlogUpdateReq (
        @NotBlank
        String title,

        @NotBlank
        String document,

        @NotNull
        Category category,

        @NotNull
        List<String> tags
){
}
