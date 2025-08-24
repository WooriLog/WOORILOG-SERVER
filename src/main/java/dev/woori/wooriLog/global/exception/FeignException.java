package dev.woori.wooriLog.global.exception;

import dev.woori.wooriLog.global.response.error.ErrorCode;


public class FeignException extends CustomBaseException {
    public FeignException(ErrorCode errorCode) {
        super(errorCode);
    }
}