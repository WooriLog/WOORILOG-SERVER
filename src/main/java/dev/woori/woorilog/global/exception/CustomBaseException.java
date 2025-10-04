package dev.woori.woorilog.global.exception;

import dev.woori.woorilog.global.response.error.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomBaseException extends RuntimeException {
    private final ErrorCode errorCode;
}
