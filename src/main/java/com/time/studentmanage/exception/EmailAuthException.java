package com.time.studentmanage.exception;

public class EmailAuthException extends RuntimeException {
    public EmailAuthException() {
        super();
    }

    public EmailAuthException(String message) {
        super(message);
    }

    public EmailAuthException(String message, Throwable cause) {
        super(message, cause);
    }

    public EmailAuthException(Throwable cause) {
        super(cause);
    }

    protected EmailAuthException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
