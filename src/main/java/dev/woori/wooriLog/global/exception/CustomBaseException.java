package dev.woori.wooriLog.global.exception;

import dev.woori.wooriLog.global.response.error.ErrorCode;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public class CustomBaseException extends RuntimeException {
    private final ErrorCode errorCode;
}
