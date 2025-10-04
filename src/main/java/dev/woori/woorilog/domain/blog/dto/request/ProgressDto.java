package dev.woori.woorilog.domain.blog.dto.request;

import dev.woori.woorilog.domain.blog.entity.Progress;
import dev.woori.woorilog.domain.blog.enums.ProgressValue;
import lombok.Builder;

@Builder
public record ProgressDto(
        ProgressValue progress,
        String message
) {
    public static ProgressDto create(Progress progress) {
        return new ProgressDto(progress.getProgress(), progress.getMessage());
    }
}
