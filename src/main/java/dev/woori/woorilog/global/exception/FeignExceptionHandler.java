package dev.woori.woorilog.global.exception;

import dev.woori.woorilog.global.response.ApiResponseUtil;
import dev.woori.woorilog.global.response.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class FeignExceptionHandler {

    @ExceptionHandler(FeignException.class)
    public ResponseEntity<BaseResponse<?>> handleFeignException(FeignException e) {
        return ApiResponseUtil.failure(e.getErrorCode());
    }
}
