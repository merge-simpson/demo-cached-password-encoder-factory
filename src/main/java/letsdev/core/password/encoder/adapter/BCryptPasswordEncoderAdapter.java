package letsdev.core.password.encoder.adapter;

import jakarta.annotation.Nonnull;
import letsdev.core.password.encoder.option.BcryptPasswordEncoderOption;
import letsdev.core.password.encoder.port.BCryptPasswordEncoderPort;
import letsdev.core.password.exception.PasswordEncoderEncryptionException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Base64;

public class BCryptPasswordEncoderAdapter implements BCryptPasswordEncoderPort {

    private static final Base64.Encoder BASE64_ENCODER = Base64.getEncoder().withoutPadding();
    private static final String PREFIX = "{bcrypt}";

    private final BCryptPasswordEncoder delegatedEncoder;
    private final int strength;

    public BCryptPasswordEncoderAdapter(int strength) {
        this(new BCryptPasswordEncoder(strength), strength);
    }

    public BCryptPasswordEncoderAdapter(BcryptPasswordEncoderOption option) {
        this(new BCryptPasswordEncoder(option.strength()), option.strength());
    }

    private BCryptPasswordEncoderAdapter(BCryptPasswordEncoder delegatedEncoder, int strength) {
        this.delegatedEncoder = delegatedEncoder;
        this.strength = strength;
    }

    @Override
    public String encode(String rawPassword) {
        return PREFIX + delegatedEncoder.encode(rawPassword);
    }

    @Override
    public String encodeWithCustomSalt(@Nonnull String rawPassword, @Nonnull byte[] salt) {
        if (salt.length != 16) {
            throw new PasswordEncoderEncryptionException(
                    "Salt length for BCrypt encryption must be exactly 16 bytes."
            );
        }
        String prefixedSalt = STR."$2a$\{strength}$\{ModifiedBase64.encodeAlternativeBase64(salt)}";

        return  PREFIX + BCrypt.hashpw(rawPassword, prefixedSalt);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        encodedPassword = encodedPassword.substring(PREFIX.length());
        return delegatedEncoder.matches(rawPassword, encodedPassword);
    }
}
