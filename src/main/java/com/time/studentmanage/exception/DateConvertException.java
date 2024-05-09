package com.time.studentmanage.exception;

public class DateConvertException extends RuntimeException {

    public DateConvertException() {
        super();
    }

    public DateConvertException(String message) {
        super(message);
    }

    public DateConvertException(String message, Throwable cause) {
        super(message, cause);
    }

    public DateConvertException(Throwable cause) {
        super(cause);
    }

    protected DateConvertException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
