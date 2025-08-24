package dev.woori.wooriLog.global.exception;

import com.fasterxml.jackson.databind.JsonMappingException;
import dev.woori.wooriLog.global.response.ApiResponseUtil;
import dev.woori.wooriLog.global.response.BaseResponse;
import dev.woori.wooriLog.global.response.error.ErrorBaseCode;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.io.UnsupportedEncodingException;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * 400 - MissingServletRequestParameterException
     * 예외 내용 : 필수 파라미터가 존재하지 않음
     */
    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<BaseResponse<?>> handleMissingServletRequestParameterException(final MissingServletRequestParameterException e) {
        final String errorMessage = "누락 파라미터 : " + e.getParameterName();
        return ApiResponseUtil.failure(ErrorBaseCode.MISSING_PARAM, errorMessage);
    }

    /**
     * 400 - HttpMessageNotReadableException
     * 예외 내용 : JSON 바인딩 오류 || @RequestBody 필수 값 오류 || @RequestBody 데이터 자료형 오류 || 데이터 포맷 오류
     */
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<BaseResponse<?>> handleHttpMessageNotReadableException(final HttpMessageNotReadableException e) {
        // JSON 매핑 오류
        if (e.getCause() instanceof JsonMappingException jsonMappingException) {

            String errorMessage = jsonMappingException.getPath().stream()
                    .map(ref -> String.format("잘못된 필드 값 : '%s'", ref.getFieldName()))
                    .collect(Collectors.joining("\n"));

            return ApiResponseUtil.failure(ErrorBaseCode.NOT_READABLE, errorMessage);
        } else {
            return ApiResponseUtil.failure(ErrorBaseCode.NOT_READABLE);
        }
    }

    /**
     * 400 - IllegalArgumentException
     * 예외 내용 : 잘못된 인자값 전달로 인한 오류
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<BaseResponse<?>> handleException(IllegalArgumentException e) {
        return ApiResponseUtil.failure(ErrorBaseCode.BAD_REQUEST, e.getCause().getMessage());
    }

    /**
     * 404 - EntityNotFoundException
     * 예외 내용 : 리소스에 대한 엔티티를 찾을 수 없는 오류
     */
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<BaseResponse<?>> handleEntityNotFoundException(final EntityNotFoundException e) {
        return ApiResponseUtil.failure(ErrorBaseCode.NOT_FOUND_ENTITY);
    }

    /**
     * 404 - NoHandlerFoundException
     * 예외 내용 : 잘못된 api로 요청했을 때 발생
     */
    @ExceptionHandler(NoHandlerFoundException.class)
    public ResponseEntity<BaseResponse<?>> handleNoHandlerFoundException(final NoHandlerFoundException e) {
        return ApiResponseUtil.failure(ErrorBaseCode.NOT_FOUND_API);
    }

    /**
     * 404 - NoResourceFoundException
     * 예외 내용 : 잘못된 엔드포인트로 요청했을 때 발생
     */
    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<BaseResponse<?>> handleNoResourceFoundException(final NoResourceFoundException e) {
        return ApiResponseUtil.failure(ErrorBaseCode.NOT_FOUND_API);
    }

    /**
     * 405 - HttpRequestMethodNotSupportedException
     * 예외 내용 : 잘못된 HTTP METHOD로 요청했을 때 발생
     */
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<BaseResponse<?>> handleHttpRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException e) {
        return ApiResponseUtil.failure(ErrorBaseCode.METHOD_NOT_ALLOWED);
    }

    /**
     * 403 - InvalidTokenException
     * 예외 내용 : 유효하지 않은 토큰으로 요청했을 때 발생
     */
    @ExceptionHandler(JwtTokenInvalidException.class)
    public ResponseEntity<BaseResponse<?>> handleInvalidTokenException(final JwtTokenInvalidException e) {
        return ApiResponseUtil.failure(ErrorBaseCode.UNAUTHORIZED);
    }

    /**
     * 40101 - UnRolledException
     * 예외 내용 : 등록되지 않은 사용자로 요청했을 때 발생
     */
    @ExceptionHandler(UnRolledException.class)
    public ResponseEntity<BaseResponse<?>> handleGoogleException(final UnRolledException e) {
        return ApiResponseUtil.failure(ErrorBaseCode.UNENROLLED, e.getMessage());
    }

    /**
     * 403 - ExpiredTokenException
     * 예외 내용 : 유효기간이 만료된 토큰으로 요청했을 때 발생
     */
    @ExceptionHandler(JwtTokenExpiredException.class)
    public ResponseEntity<BaseResponse<?>> handleExpiredTokenException(final JwtTokenExpiredException e) {
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

    /**
     * 500 - ServerError
     * 예외 내용 : 서버 내부 오류
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<BaseResponse<?>> handleServerException(final Exception e) {
        if (e.getCause() != null)
            e.printStackTrace();
        return ApiResponseUtil.failure(ErrorBaseCode.INTERNAL_SERVER_ERROR, e.getMessage());
    }
}
