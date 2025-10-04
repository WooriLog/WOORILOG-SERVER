package dev.woori.woorilog.global.response;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public enum SuccessCode implements ApiCode {
    /**
     * 200 OK
     */
    OK(HttpStatus.OK, 200, "요청이 성공했습니다."),

    /**
     * 201 CREATED
     */
    CREATED(HttpStatus.CREATED, 201, "요청이 성공했습니다.");

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
