package letsdev.core.password.encoder.option;

import letsdev.core.password.encoder.GeneralPasswordEncoderType;
import letsdev.core.password.encoder.PasswordEncoderType;
import letsdev.core.password.exception.PasswordEncoderOptionErrorCode;

public record BcryptPasswordEncoderOption(int strength) implements PasswordEncoderOption {

    public BcryptPasswordEncoderOption {
        var BCRYPT_STRENGTH_OUT_OF_RANGE = PasswordEncoderOptionErrorCode.BCRYPT_STRENGTH_OUT_OF_RANGE;

        if (strength == 0) {
            strength = 10;
        } else if (strength < 4 || 32 <= strength) {
            throw BCRYPT_STRENGTH_OUT_OF_RANGE.defaultException();
        }
    }

    public BcryptPasswordEncoderOption() {
        this(10);
    }

    @Override
    public PasswordEncoderType encoderType() {
        return GeneralPasswordEncoderType.BCRYPT;
    }
}
