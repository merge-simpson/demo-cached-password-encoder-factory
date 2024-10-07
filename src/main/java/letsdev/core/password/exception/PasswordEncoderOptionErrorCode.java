package letsdev.core.password.exception;

import letsdev.common.exception.support.ErrorCode;
import org.springframework.http.HttpStatus;

public enum PasswordEncoderOptionErrorCode implements ErrorCode {
    BCRYPT_STRENGTH_OUT_OF_RANGE(
            "The strength(cost factor) must be between 4 and 31 inclusive.",
            HttpStatus.INTERNAL_SERVER_ERROR
    ),
    INVALID_PASSWORD_ENCODER_OPTION(
            "올바른 옵션을 입력하십시오.",
            HttpStatus.INTERNAL_SERVER_ERROR
    );

    PasswordEncoderOptionErrorCode(String message, HttpStatus status) {
        this.message = message;
        this.status = status;
    }

    private final String message;
    private final HttpStatus status;

    @Override
    public HttpStatus defaultHttpStatus() {
        return status;
    }

    @Override
    public String defaultMessage() {
        return message;
    }

    @Override
    public PasswordEncoderOptionException defaultException() {
        return new PasswordEncoderOptionException(this);
    }

    @Override
    public PasswordEncoderOptionException defaultException(Throwable cause) {
        return new PasswordEncoderOptionException(this, cause);
    }
}
