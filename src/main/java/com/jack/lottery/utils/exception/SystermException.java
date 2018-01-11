package com.jack.lottery.utils.exception;

public class SystermException extends BaseException {
    public SystermException() {
        super();
    }

    public SystermException(String message) {
        super(message);
    }

    public SystermException(String message, Throwable cause) {
        super(message, cause);
    }

    public SystermException(Throwable cause) {
        super(cause);
    }

    protected SystermException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
