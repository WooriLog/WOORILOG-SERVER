package dev.woori.wooriLog.domain.blog.controller;

import dev.woori.wooriLog.domain.blog.dto.request.BlogCreateReq;
import dev.woori.wooriLog.domain.blog.dto.request.BlogUpdateReq;
import dev.woori.wooriLog.domain.blog.dto.response.BlogCreateRes;
import dev.woori.wooriLog.domain.blog.dto.response.BlogDetailInfoRes;
import dev.woori.wooriLog.domain.blog.service.BlogService;
import dev.woori.wooriLog.global.resolver.UserId;
import dev.woori.wooriLog.global.response.ApiResponseUtil;
import dev.woori.wooriLog.global.response.BaseResponse;
import dev.woori.wooriLog.global.response.SuccessCode;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;

    @GetMapping("/blog/home")
    public ResponseEntity<BaseResponse<?>> getHomeBlogInfos() {
        return ApiResponseUtil.success(SuccessCode.OK, blogService.getBlogBasicInfos());
    }

    @PostMapping("/blog/{projectId}")
    public ResponseEntity<BaseResponse<?>> createBlog(
            @PathVariable("projectId") Long projectId,
            @UserId Long userId,
            @Valid @RequestBody BlogCreateReq request
    ) {
        return ApiResponseUtil.success(
                SuccessCode.OK,
                BlogCreateRes.from(blogService.createBlog(projectId, userId, request))
        );
    }

    @GetMapping("/blog/{postId}")
    public ResponseEntity<BaseResponse<?>> getBlogInfo(
            @PathVariable("postId") Long postId
    ) {
        BlogDetailInfoRes res = blogService.getBlogInfo(postId);
        return ApiResponseUtil.success(SuccessCode.OK, res);
    }

    @PutMapping("/blog/{postId}")
    public ResponseEntity<BaseResponse<?>> updateBlog(@UserId Long userId, @PathVariable("postId") Long postId, @RequestBody BlogUpdateReq request){
        return ApiResponseUtil.success(SuccessCode.OK, blogService.updateBlog(userId, postId, request));
    }
}
