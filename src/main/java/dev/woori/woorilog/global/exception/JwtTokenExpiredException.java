package dev.woori.woorilog.global.exception;

import dev.woori.woorilog.global.response.error.ErrorCode;

public class JwtTokenExpiredException extends JwtTokenException {
    public JwtTokenExpiredException(ErrorCode errorCode) {
        super(errorCode);
    }
}
