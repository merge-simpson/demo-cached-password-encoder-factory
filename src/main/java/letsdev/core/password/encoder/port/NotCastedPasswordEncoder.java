package letsdev.core.password.encoder.port;

public interface NotCastedPasswordEncoder {
    boolean matches(String rawPassword, String encodedPassword);
}
