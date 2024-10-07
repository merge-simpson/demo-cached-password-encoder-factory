package letsdev.core.password.exception;

import letsdev.common.exception.support.ErrorCode;
import org.springframework.http.HttpStatus;

public class PasswordEncoderException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "";
    protected final ErrorCode errorCode;
    private final String message;

    public PasswordEncoderException() {
        this(DEFAULT_MESSAGE);
    }

    public PasswordEncoderException(String message) {
        super(message);
        this.errorCode = DefaultErrorCodeHolder.DEFAULT_ERROR_CODE;
        this.message = message;
    }

    public PasswordEncoderException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = DefaultErrorCodeHolder.DEFAULT_ERROR_CODE;
        this.message = message;
    }

    public PasswordEncoderException(ErrorCode errorCode) {
        super(errorCode.defaultMessage());
        this.errorCode = errorCode;
        this.message = errorCode.defaultMessage();
    }

    public PasswordEncoderException(ErrorCode errorCode, Throwable cause) {
        super(errorCode.defaultMessage(), cause);
        this.errorCode = errorCode;
        this.message = errorCode.defaultMessage();
    }

    public PasswordEncoderException(ErrorCode errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
        this.message = message;
    }

    public PasswordEncoderException(ErrorCode errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
        this.message = message;
    }

    public PasswordEncoderException(Throwable cause) {
        this(DEFAULT_MESSAGE, cause);
    }

    protected PasswordEncoderException(
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace
    ) {
        this(DEFAULT_MESSAGE, cause, enableSuppression, writableStackTrace);
    }

    protected PasswordEncoderException(
            String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace
    ) {
        super(message, cause, enableSuppression, writableStackTrace);
        this.errorCode = DefaultErrorCodeHolder.DEFAULT_ERROR_CODE;
        this.message = message;
    }

    public ErrorCode errorCode() {
        return errorCode;
    }

    public String message() {
        return message;
    }

    private static class DefaultErrorCodeHolder {
        private static final ErrorCode DEFAULT_ERROR_CODE = new ErrorCode() {
            public String name() {
                return "SERVER_ERROR";
            }

            public HttpStatus defaultHttpStatus() {
                return HttpStatus.INTERNAL_SERVER_ERROR;
            }

            public String defaultMessage() {
                return "서버 오류";
            }

            public PasswordEncoderException defaultException() {
                return new PasswordEncoderException(this);
            }

            public PasswordEncoderException defaultException(Throwable cause) {
                return new PasswordEncoderException(this, cause);
            }
        };

        private DefaultErrorCodeHolder() {
        }
    }
}
