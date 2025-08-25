package dev.woori.wooriLog.domain.member.blog.controller;

import dev.woori.wooriLog.domain.member.blog.dto.BlogCreateReq;
import dev.woori.wooriLog.domain.member.blog.dto.BlogInfoRes;
import dev.woori.wooriLog.domain.member.blog.service.BlogService;
import dev.woori.wooriLog.global.resolver.UserId;
import dev.woori.wooriLog.global.response.ApiResponseUtil;
import dev.woori.wooriLog.global.response.BaseResponse;
import dev.woori.wooriLog.global.response.SuccessCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;

    @PostMapping("/blog/post/{projectId}")
    public ResponseEntity<BaseResponse<?>> blogCreate(
            @PathVariable("projectId") Long projectId,
            @UserId Long userId,
            @RequestBody BlogCreateReq request
    ) {
        blogService.createBlog(projectId, userId, request);
        return ApiResponseUtil.success(SuccessCode.OK);
    }

    @GetMapping("/blog/{postId}")
    public ResponseEntity<BaseResponse<?>> blogCreate(
            @PathVariable("postId") Long postId
    ) {
        BlogInfoRes res = blogService.createBlogInfoRes(postId);
        return ResponseEntity.ok(BaseResponse.of(SuccessCode.OK, res));
    }
}
