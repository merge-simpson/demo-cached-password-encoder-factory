package letsdev.core.password.encoder.port;

public interface CustomSaltingPasswordEncoder extends NotCastedPasswordEncoder {
    String encodeWithCustomSalt(String rawPassword, byte[] salt);
}
