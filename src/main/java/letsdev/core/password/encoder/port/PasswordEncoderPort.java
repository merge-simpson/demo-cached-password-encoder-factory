package letsdev.core.password.encoder.port;

public interface PasswordEncoderPort {
    String encode(String rawPassword);
    String encodeWithCustomSalt(String rawPassword, byte[] salt);
    boolean matches(String rawPassword, String encodedPassword);
}
