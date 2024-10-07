package letsdev.core.password.encoder.adapter;

import jakarta.annotation.Nonnull;
import letsdev.core.password.encoder.option.Argon2dPasswordEncoderOption;
import letsdev.core.password.encoder.port.Argon2DCustomSaltingPasswordEncoder;
import letsdev.core.password.encoder.port.Argon2DPasswordEncoder;
import letsdev.core.password.exception.PasswordEncoderEncryptionException;
import org.bouncycastle.crypto.generators.Argon2BytesGenerator;
import org.bouncycastle.crypto.params.Argon2Parameters;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Objects;

public class Argon2DPasswordEncoderAdapter
        implements Argon2DPasswordEncoder,
        Argon2DCustomSaltingPasswordEncoder {

    private final SecureRandom secureRandom = new SecureRandom();
    private byte[] seed;
    private final int saltLength;
    private final int hashLength;
    private final int parallelism;
    private final int iterations;
    private final int memory;

    public Argon2DPasswordEncoderAdapter(Argon2dPasswordEncoderOption option) {
        this(
                option.saltLength(),
                option.hashLength(),
                option.parallelism(),
                option.memory(),
                option.iterations()
        );
    }

    private Argon2DPasswordEncoderAdapter(
            int saltLength,
            int hashLength,
            int parallelism,
            int memory,
            int iterations
    ) {
        this.saltLength = saltLength;
        this.hashLength = hashLength;
        this.parallelism = parallelism;
        this.iterations = iterations;
        this.memory = memory;
    }

    @Override
    public String encode(String rawPassword) {
        secureRandom.setSeed(SecureRandom.getSeed(saltLength));
        byte[] salt = new byte[saltLength];
        secureRandom.nextBytes(salt);
        return encodeWithCustomSalt(
                rawPassword,
                salt
        );
    }

    @Override
    public String encodeWithCustomSalt(@Nonnull String rawPassword, @Nonnull byte[] salt) {
        Objects.requireNonNull(rawPassword);
        Objects.requireNonNull(salt);
        if (salt.length != saltLength) {
            throw new PasswordEncoderEncryptionException(
                    STR."Salt length for BCrypt encryption must be exactly 16 bytes. Input length: \{salt.length}"
            );
        }
        Argon2Parameters parameters = createParameters(salt);
        byte[] hash = hashPassword(rawPassword, parameters);
        return Argon2EncodingUtil.encode(hash, parameters);
    }

    @Override
    public boolean matches(String rawPassword, String encodedPassword) {
        DecodedSaltAndDigest saltAndDigest = Argon2EncodingUtil.decode(encodedPassword);

        byte[] salt = saltAndDigest.salt();
        Argon2Parameters parameters = createParameters(salt);
        byte[] hash = hashPassword(rawPassword, parameters);

        return MessageDigest.isEqual(hash, saltAndDigest.digest());
    }

    private Argon2Parameters createParameters(byte[] salt) {
        return new Argon2Parameters.Builder(Argon2Parameters.ARGON2_d)
                .withSalt(salt)
                .withParallelism(parallelism)
                .withMemoryAsKB(memory)
                .withIterations(iterations)
                .build();
    }

    private byte[] hashPassword(String rawPassword, Argon2Parameters parameters) {
        byte[] rawPasswordBytes = rawPassword.getBytes(StandardCharsets.UTF_8);
        byte[] hash = new byte[this.hashLength];
        Argon2BytesGenerator generator = new Argon2BytesGenerator();
        generator.init(parameters);
        generator.generateBytes(rawPasswordBytes, hash);
        return hash;
    }
}