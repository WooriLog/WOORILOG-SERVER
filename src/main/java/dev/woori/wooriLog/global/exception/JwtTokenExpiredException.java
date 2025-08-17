package dev.woori.wooriLog.global.exception;

import dev.woori.wooriLog.global.response.error.ErrorCode;

public class JwtTokenExpiredException extends JwtTokenException {
    public JwtTokenExpiredException(ErrorCode errorCode) {
        super(errorCode);
    }
}
