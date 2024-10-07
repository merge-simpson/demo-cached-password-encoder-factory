package letsdev.core.password.encoder.option;

import letsdev.core.password.encoder.GeneralPasswordEncoderType.Argon2Variant;

public interface Argon2PasswordEncoderOption extends PasswordEncoderOption {
    int saltLength();
    int hashLength();
    int parallelism();
    int iterations();
    float alpha();
    int memory();
    float gain();
    Argon2Variant mode();
}
