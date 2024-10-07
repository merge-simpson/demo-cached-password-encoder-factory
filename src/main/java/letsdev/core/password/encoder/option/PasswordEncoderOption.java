package letsdev.core.password.encoder.option;

import letsdev.core.password.encoder.PasswordEncoderType;

public interface PasswordEncoderOption {
    PasswordEncoderType encoderType();

    default <T extends PasswordEncoderOption> T as(Class<T> clazz) {
        return (T) this;
    }
}
