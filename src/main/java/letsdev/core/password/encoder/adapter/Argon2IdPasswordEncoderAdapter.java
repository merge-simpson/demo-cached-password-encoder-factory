package letsdev.core.password.encoder.adapter;

import jakarta.annotation.Nonnull;
import letsdev.core.password.encoder.option.Argon2idPasswordEncoderOption;
import letsdev.core.password.encoder.port.Argon2IdCustomSaltingPasswordEncoder;
import letsdev.core.password.encoder.port.Argon2IdPasswordEncoder;
import letsdev.core.password.exception.PasswordEncoderEncryptionException;
import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

public class Argon2IdPasswordEncoderAdapter
        implements Argon2IdPasswordEncoder,
        Argon2IdCustomSaltingPasswordEncoder {

    private static final String PREFIX = "{argon2}";

    private final Argon2PasswordEncoder delegatedEncoder;
    private final int saltLength;
    private final int hashLength;
    private final int parallelism;
    private final int iterations;
    private final int memory;

    public Argon2IdPasswordEncoderAdapter(Argon2idPasswordEncoderOption option) {
        this(
                new Argon2PasswordEncoder(
                        option.saltLength(),
                        option.hashLength(),
                        option.parallelism(),
                        option.memory(),
                        option.iterations()
                ),
                option.saltLength(),
                option.hashLength(),
                option.parallelism(),
                option.memory(),
                option.iterations()
        );
    }

    private Argon2IdPasswordEncoderAdapter(
            Argon2PasswordEncoder passwordEncoder,
            int saltLength,
            int hashLength,
            int parallelism,
            int memory,
            int iterations
    ) {
        this.delegatedEncoder = passwordEncoder;
        this.saltLength = saltLength;
        this.hashLength = hashLength;
        this.parallelism = parallelism;
        this.iterations = iterations;
        this.memory = memory;
    }

    @Override
    public String encode(String rawPassword) {
        return PREFIX + delegatedEncoder.encode(rawPassword);
    }

    @Override
    public String encodeWithCustomSalt(@Nonnull String rawPassword, @Nonnull byte[] salt) {
        Objects.requireNonNull(rawPassword);
        Objects.requireNonNull(salt);
        if (salt.length != saltLength) {
            throw new PasswordEncoderEncryptionException("Salt length for BCrypt encryption must be exactly 16 bytes.");
        }

        byte[] rawPasswordBytes = rawPassword.getBytes(StandardCharsets.UTF_8);
        byte[] hash = new byte[this.hashLength];

        Argon2Parameters parameters = new Argon2Parameters.Builder(Argon2Parameters.ARGON2_id)
                .withSalt(salt)
                .withParallelism(parallelism)
                .withMemoryAsKB(memory)
                .withIterations(iterations)
                .build();

        Argon2BytesGenerator generator = new Argon2BytesGenerator();
        generator.init(parameters);
        generator.generateBytes(rawPasswordBytes, hash);

        return Argon2EncodingUtil.encode(hash, parameters);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        encodedPassword = encodedPassword.substring(PREFIX.length());
        return delegatedEncoder.matches(rawPassword, encodedPassword);
    }
}