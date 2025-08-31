package dev.woori.wooriLog.domain.blog.dto.request;

import dev.woori.wooriLog.domain.blog.entity.Progress;
import dev.woori.wooriLog.domain.blog.enums.ProgressValue;
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
