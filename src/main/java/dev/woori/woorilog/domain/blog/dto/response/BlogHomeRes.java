package dev.woori.woorilog.domain.blog.dto.response;

import dev.woori.woorilog.domain.blog.dto.BlogBasicInfoDto;
import lombok.Builder;

import java.util.List;

@Builder
public record BlogHomeRes(
        int currentPage,
        int totalPage,
        List<BlogBasicInfoDto> blogInfos
) {
    public static BlogHomeRes of(List<BlogBasicInfoDto> blogInfos, int totalPage, int currentPage) {
        return BlogHomeRes.builder()
                .blogInfos(blogInfos)
                .totalPage(totalPage)
                .currentPage(currentPage)
                .build();
    }
}
