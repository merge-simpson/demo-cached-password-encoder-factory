package letsdev.core.password.encoder;

public enum GeneralPasswordEncoderType implements PasswordEncoderType {
    PBKDF2,
    BCRYPT,
    SCRYPT,
    ARGON2;

    public enum Argon2Variant implements PasswordEncoderType {
        ARGON2I,
        ARGON2D,
        ARGON2ID;

        public static Argon2Variant defaultVariant() {
            return Argon2Variant.ARGON2ID;
        }
    }
}
