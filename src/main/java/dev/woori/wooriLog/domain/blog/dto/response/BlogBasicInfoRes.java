package dev.woori.wooriLog.domain.blog.dto.response;

import dev.woori.wooriLog.domain.blog.dto.request.ProgressDto;
import dev.woori.wooriLog.domain.blog.entity.Blog;
import dev.woori.wooriLog.domain.blog.entity.Progress;
import dev.woori.wooriLog.domain.blog.enums.Category;
import lombok.Builder;

import java.util.List;

@Builder
public record BlogBasicInfoRes(
        Long blogId,
        String title,
        String projectName,
        String authorName,
        Category category,
        List<String> tags,
        List<ProgressDto> progresses
) {
    public static BlogBasicInfoRes create(Blog blog) {
        return BlogBasicInfoRes.builder()
                .blogId(blog.getId())
                .title(blog.getTitle())
                .projectName(blog.getProject().getProjectName())
                .authorName(blog.getMember().getName())
                .category(blog.getCategory())
                .tags(blog.getTags())
                .progresses(transProgressDtos(blog.getProgresses()))
                .build();
    }

    private static List<ProgressDto> transProgressDtos (List<Progress> progresses) {
        return progresses.stream()
                .map(ProgressDto::create)
                .toList();
    }
}
