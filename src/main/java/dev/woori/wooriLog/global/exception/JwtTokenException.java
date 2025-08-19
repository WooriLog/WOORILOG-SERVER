package dev.woori.wooriLog.global.exception;

import dev.woori.wooriLog.global.response.error.ErrorCode;

public class JwtTokenException extends CustomBaseException {
    public JwtTokenException(final ErrorCode errorCode) {
        super(errorCode);
    }
}

