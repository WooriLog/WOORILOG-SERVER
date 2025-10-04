package dev.woori.woorilog.global.exception;

import dev.woori.woorilog.global.response.error.ErrorCode;

public class WooriLogUseException extends CustomBaseException {
    public WooriLogUseException(ErrorCode errorCode) {
        super(errorCode);
    }
}
