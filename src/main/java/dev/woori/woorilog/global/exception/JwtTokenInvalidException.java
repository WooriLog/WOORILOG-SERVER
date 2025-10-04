package dev.woori.woorilog.global.exception;

import dev.woori.woorilog.global.response.error.ErrorCode;

public class JwtTokenInvalidException extends JwtTokenException {

    public JwtTokenInvalidException(ErrorCode errorCode) {
        super(errorCode);
    }
}
