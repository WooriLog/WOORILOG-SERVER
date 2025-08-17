package dev.woori.wooriLog.global.exception;

import dev.woori.wooriLog.global.response.ApiResponseUtil;
import dev.woori.wooriLog.global.response.BaseResponse;
import dev.woori.wooriLog.global.response.error.ErrorBaseCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.UnsupportedEncodingException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 404 - NoHandlerFoundException
     * 예외 내용 : 잘못된 api로 요청했을 때 발생
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<BaseResponse<?>> handleNoHandlerFoundException(final NoHandlerFoundException e) {
        return ApiResponseUtil.failure(ErrorBaseCode.NOT_FOUND_API);
    }

    /**
     * 400 - InvalidTokenException
     * 예외 내용 : 유효하지 않은 토큰으로 요청했을 때 발생
     */
    @ExceptionHandler(JwtTokenInvalidException.class)
    public ResponseEntity<BaseResponse<?>> handleInvalidTokenException(final JwtTokenException e) {
        return ApiResponseUtil.failure(ErrorBaseCode.INVALID_TOKEN);
    }

    /**
     * 403 - ExpiredTokenException
     * 예외 내용 : 유효기간이 만료된 토큰으로 요청했을 때 발생
     */
    @ExceptionHandler(JwtTokenExpiredException.class)
    public ResponseEntity<BaseResponse<?>> handleExpiredTokenException(final JwtTokenException e) {
        return ApiResponseUtil.failure(ErrorBaseCode.EXPIRED_TOKEN);
    }

    /**
     * 500 - UrlDecodeException
     * 예외 내용 : URL 디코딩시 에러 발생
     */
    @ExceptionHandler(UnsupportedEncodingException.class)
    public ResponseEntity<BaseResponse<?>> handleUrlDecodeException(final UnsupportedEncodingException e) {
        return ApiResponseUtil.failure(ErrorBaseCode.URL_DECODE_ERROR);
    }
}
