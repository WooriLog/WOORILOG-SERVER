package dev.woori.wooriLog.global.response.error;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum ErrorBaseCode implements ErrorCode {

    /**
     * 400 BAD_REQUEST - 잘못된 요청
     */
    BAD_REQUEST(HttpStatus.BAD_REQUEST, 400, "잘못된 요청입니다."),
    MISSING_PARAM(HttpStatus.BAD_REQUEST, 400, "필수 파라미터가 존재하지 않습니다."),
    NOT_READABLE(HttpStatus.BAD_REQUEST, 400, "JSON 혹은 REQUEST BODY 필드 오류 입니다."),
    INVALID_GOOGLE_CODE(HttpStatus.BAD_REQUEST, 400, "유효하지 않은 코드입니다."),

    /**
     * 401 UNAUTHORIZED - 리소스 접근 권한
     */
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, 401, "토큰이 만료되었습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, 401, "인증되지 않은 사용자입니다."),
    UNENROLLED(HttpStatus.UNAUTHORIZED, 40101, "가입되지 않은 사용자입니다."),
    INVALID_GOOGLE_AUTHCODE(HttpStatus.UNAUTHORIZED, 401, "유효하지 않은 인증코드입니다."),
    INVALID_GOOGLE_TOKEN(HttpStatus.UNAUTHORIZED, 401, "유효하지 않은 토큰입니다."),

    /**
     * 403 FORBIDDEN - 리소스 접근 금지
     */
    FORBIDDEN(HttpStatus.FORBIDDEN, 403, "리소스 접근이 거부되었습니다."),

    /**
     * 404 NOT FOUND - 찾을 수 없음
     */
    NOT_FOUND_API(HttpStatus.NOT_FOUND, 404, "잘못된 API 요청입니다."),
    NOT_FOUND_ENTITY(HttpStatus.NOT_FOUND, 404, "대상을 찾을 수 없습니다."),

    /**
     * 405 METHOD NOT ALLOWED - 허용되지 않은 메서드
     */
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, 405, "잘못된 HTTP METHOD 요청입니다."),

    /**
     * 409 CONFLICT
     */
    CONFLICT(HttpStatus.CONFLICT, 409, "이미 존재하는 리소스입니다."),

    /**
     * 500 INTERNAL SERVER ERROR - 서버 오류
     */
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, "서버 내부 오류입니다."),
    URL_DECODE_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, 500, "URL 디코딩 에러"),

    /**
     * 501 NOT IMPLEMENTED - 구현되지 않음
     */
    NOT_IMPLEMENTED(HttpStatus.NOT_IMPLEMENTED, 501, "서버 내부 오류 입니다.");

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
