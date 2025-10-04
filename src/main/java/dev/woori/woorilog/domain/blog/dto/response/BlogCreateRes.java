package dev.woori.woorilog.domain.blog.dto.response;

public record BlogCreateRes(
        Long blogId
) {
    public static BlogCreateRes from(Long blogId) {
        return new BlogCreateRes(blogId);
    }
}
