package dev.woori.woorilog.global.exception;

import dev.woori.woorilog.global.response.error.ErrorCode;

public class JwtTokenException extends CustomBaseException {
    public JwtTokenException(final ErrorCode errorCode) {
        super(errorCode);
    }
}

