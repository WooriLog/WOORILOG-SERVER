package dev.woori.woorilog.global.exception;

import lombok.Getter;

import static dev.woori.woorilog.global.auth.Constants.UNENROLLED_USER;

@Getter
public class UnEnrolledException extends RuntimeException {
    private final String ticket;

    public UnEnrolledException(String ticket) {
        super(UNENROLLED_USER);
        this.ticket = ticket;
    }

    @Override
    public String getMessage() {
        return ticket;
    }

    @Override
    public String toString() {
        return getClass().getName() + ": " + UNENROLLED_USER;
    }
}
