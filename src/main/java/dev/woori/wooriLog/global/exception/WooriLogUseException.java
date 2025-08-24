package dev.woori.wooriLog.global.exception;

import dev.woori.wooriLog.global.response.error.ErrorCode;

public class WooriLogUseException extends CustomBaseException {
    public WooriLogUseException(ErrorCode errorCode) {
        super(errorCode);
    }
}
