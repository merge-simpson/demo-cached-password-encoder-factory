package letsdev.core.password.exception;

public class PasswordEncoderEncryptionException extends PasswordEncoderException {

    private static final String DEFAULT_MESSAGE = "";

    public PasswordEncoderEncryptionException() {
        this(DEFAULT_MESSAGE);
    }

    public PasswordEncoderEncryptionException(String message) {
        super(message);
    }

    public PasswordEncoderEncryptionException(String message, Throwable cause) {
        super(message, cause);
    }

    public PasswordEncoderEncryptionException(Throwable cause) {
        this(DEFAULT_MESSAGE, cause);
    }

    protected PasswordEncoderEncryptionException(
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace
    ) {
        this(DEFAULT_MESSAGE, cause, enableSuppression, writableStackTrace);
    }

    protected PasswordEncoderEncryptionException(
            String message,
            Throwable cause,
            boolean enableSuppression,
            boolean writableStackTrace
    ) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
