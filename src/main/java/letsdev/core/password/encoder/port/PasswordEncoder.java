package letsdev.core.password.encoder.port;

public interface PasswordEncoder extends NotCastedPasswordEncoder {
    String encode(String rawPassword);
}
