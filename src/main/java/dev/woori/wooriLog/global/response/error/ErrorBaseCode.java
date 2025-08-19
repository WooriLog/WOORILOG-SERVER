package dev.woori.wooriLog.global.response.error;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorBaseCode implements ErrorCode {

    /**
     * 400 - BAD_REQUEST
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, 400, "잘못된 요청입니다."),
    INVALID_TOKEN(HttpStatus.BAD_REQUEST, 400, "토큰이 유효하지 않습니다."),

    /**
     * 401 - UNAUTHORIZED
     */
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, 401, "토큰이 만료되었습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, 401, "인증되지 않은 사용자입니다."),
    UNENROLLED(HttpStatus.UNAUTHORIZED, 40101, "가입되지 않은 사용자입니다."),

    /**
     * 404 NOT FOUND
     */
    NOT_FOUND_API(HttpStatus.NOT_FOUND, 404, "잘못된 API 요청입니다."),

    /**
     * 500
     */
    URL_DECODE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, "URL 디코딩 에러");

    private final HttpStatus httpStatus;
    private final int code;
    private final String message;

    @Override
    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    @Override
    public int getCode() {
        return code;
    }

    @Override
    public String getMessage() {
        return message;
    }
}
