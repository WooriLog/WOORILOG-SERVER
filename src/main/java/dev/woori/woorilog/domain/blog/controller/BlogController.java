package dev.woori.woorilog.domain.blog.controller;

import dev.woori.woorilog.domain.blog.dto.request.BlogCreateOrUpdateReq;
import dev.woori.woorilog.domain.blog.dto.response.BlogCreateRes;
import dev.woori.woorilog.domain.blog.dto.response.BlogDetailInfoRes;
import dev.woori.woorilog.domain.blog.service.BlogService;
import dev.woori.woorilog.global.resolver.UserId;
import dev.woori.woorilog.global.response.ApiResponseUtil;
import dev.woori.woorilog.global.response.BaseResponse;
import dev.woori.woorilog.global.response.SuccessCode;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;


@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class BlogController {

    private final BlogService blogService;

    // 홈화면 블로그 목록 조회
    @GetMapping("/blog/home")
    public ResponseEntity<BaseResponse<?>> getHomeBlogInfos(@RequestParam(defaultValue = "1") int page) {
        return ApiResponseUtil.success(SuccessCode.OK, blogService.getBlogBasicInfos(page));
    }

    // 블로그 작성
    @PostMapping("/blog/{projectId}")
    public ResponseEntity<BaseResponse<?>> createBlog(
            @PathVariable("projectId") Long projectId,
            @UserId Long userId,
            @Valid @RequestBody BlogCreateOrUpdateReq request
    ) {
        return ApiResponseUtil.success(
                SuccessCode.OK,
                BlogCreateRes.from(blogService.createBlog(projectId, userId, request))
        );
    }

    // 블로그 조회
    @GetMapping("/blog/{postId}")
    public ResponseEntity<BaseResponse<?>> getBlogInfo(
            @UserId Optional<Long> userId,
            @PathVariable("postId") Long postId,
            HttpServletRequest request,
            HttpServletResponse response
    ) {
        BlogDetailInfoRes res = blogService.getBlogInfo(userId, postId, request, response);
        return ApiResponseUtil.success(SuccessCode.OK, res);
    }

    // 블로그 수정
    @PutMapping("/blog/{postId}")
    public ResponseEntity<BaseResponse<?>> updateBlog(@UserId Long userId, @PathVariable("postId") Long postId, @Valid @RequestBody BlogCreateOrUpdateReq request){
        return ApiResponseUtil.success(SuccessCode.OK, blogService.updateBlog(userId, postId, request));
    }

    // 블로그 삭제
    @DeleteMapping("/blog/{postId}")
    public ResponseEntity<BaseResponse<?>> deleteBlog(@UserId Long userId, @PathVariable("postId") Long postId) {
        blogService.deleteBlog(userId, postId);
        return ApiResponseUtil.success(SuccessCode.OK);
    }
}
