package dev.woori.woorilog.global.exception;

import dev.woori.woorilog.global.response.error.ErrorCode;


public class FeignException extends CustomBaseException {
    public FeignException(ErrorCode errorCode) {
        super(errorCode);
    }
}