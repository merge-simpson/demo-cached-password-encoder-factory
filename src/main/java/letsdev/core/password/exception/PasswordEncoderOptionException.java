package letsdev.core.password.exception;

import letsdev.common.exception.support.ErrorCode;

public class PasswordEncoderOptionException extends PasswordEncoderException {
    public PasswordEncoderOptionException() {
        super();
    }

    public PasswordEncoderOptionException(String message) {
        super(message);
    }

    public PasswordEncoderOptionException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordEncoderOptionException(ErrorCode errorCode) {
        super(errorCode);
    }

    public PasswordEncoderOptionException(ErrorCode errorCode, Throwable cause) {
        super(errorCode, cause);
    }

    public PasswordEncoderOptionException(ErrorCode errorCode, String message) {
        super(errorCode, message);
    }

    public PasswordEncoderOptionException(ErrorCode errorCode, String message, Throwable cause) {
        super(errorCode, message, cause);
    }

    public PasswordEncoderOptionException(Throwable cause) {
        super(cause);
    }

    protected PasswordEncoderOptionException(Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(cause, enableSuppression, writableStackTrace);
    }

    protected PasswordEncoderOptionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
