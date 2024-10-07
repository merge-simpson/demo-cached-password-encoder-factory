package letsdev.core.password.exception;

public class PasswordEncoderGenerationException extends PasswordEncoderException {

    private static final String DEFAULT_MESSAGE = "";

    public PasswordEncoderGenerationException() {
        this(DEFAULT_MESSAGE);
    }

    public PasswordEncoderGenerationException(String message) {
        super(message);
    }

    public PasswordEncoderGenerationException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordEncoderGenerationException(Throwable cause) {
        this(DEFAULT_MESSAGE, cause);
    }

    protected PasswordEncoderGenerationException(
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace
    ) {
        this(DEFAULT_MESSAGE, cause, enableSuppression, writableStackTrace);
    }

    protected PasswordEncoderGenerationException(
            String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace
    ) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
