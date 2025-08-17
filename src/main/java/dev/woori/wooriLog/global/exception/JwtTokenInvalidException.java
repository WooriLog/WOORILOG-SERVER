package dev.woori.wooriLog.global.exception;

import dev.woori.wooriLog.global.response.error.ErrorCode;

public class JwtTokenInvalidException extends JwtTokenException {

    public JwtTokenInvalidException(ErrorCode errorCode) {
        super(errorCode);
    }
}
