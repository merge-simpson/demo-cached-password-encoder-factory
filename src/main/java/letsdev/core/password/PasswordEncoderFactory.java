package letsdev.core.password;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.RemovalListener;
import letsdev.core.password.encoder.GeneralPasswordEncoderType;
import letsdev.core.password.encoder.GeneralPasswordEncoderType.Argon2Variant;
import letsdev.core.password.encoder.adapter.Argon2dPasswordEncoderAdapter;
import letsdev.core.password.encoder.adapter.Argon2idPasswordEncoderAdapter;
import letsdev.core.password.encoder.adapter.BCryptPasswordEncoderAdapter;
import letsdev.core.password.encoder.option.Argon2dPasswordEncoderOption;
import letsdev.core.password.encoder.option.Argon2idPasswordEncoderOption;
import letsdev.core.password.encoder.option.BcryptPasswordEncoderOption;
import letsdev.core.password.encoder.option.PasswordEncoderOption;
import letsdev.core.password.encoder.port.PasswordEncoderPort;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class PasswordEncoderFactory {
    private final Cache<PasswordEncoderOption, PasswordEncoderPort> instanceCache;

    public PasswordEncoderFactory() {
        this.instanceCache = Caffeine.newBuilder()
                .expireAfterAccess(10, TimeUnit.MINUTES)
                .maximumSize(100)
                .build();
    }

    private PasswordEncoderFactory(
            long expirationAfterAccessDuration,
            TimeUnit expirationAfterAccessTimeUnit,
            long expirationAfterWriteDuration,
            TimeUnit expirationAfterWriteTimeUnit,
            long maximumSize,
            long maximumWeight,
            RemovalListener<PasswordEncoderOption, PasswordEncoderPort> removalListener
    ) {
        var cacheBuilder = Caffeine.newBuilder();
        if (expirationAfterAccessDuration != -1L) {
            expirationAfterAccessTimeUnit = expirationAfterAccessTimeUnit != null
                    ? expirationAfterAccessTimeUnit
                    : TimeUnit.NANOSECONDS;
            cacheBuilder.expireAfterAccess(expirationAfterAccessDuration, expirationAfterAccessTimeUnit);
        }
        if (expirationAfterWriteDuration != -1L) {
            expirationAfterWriteTimeUnit = expirationAfterWriteTimeUnit != null
                    ? expirationAfterWriteTimeUnit
                    : TimeUnit.NANOSECONDS;
            cacheBuilder.expireAfterWrite(expirationAfterWriteDuration, expirationAfterWriteTimeUnit);
        }
        if (maximumSize != -1L) {
            cacheBuilder.maximumSize(maximumSize);
        }
        if (maximumWeight != -1L) {
            cacheBuilder.maximumWeight(maximumWeight);
        }
        if (removalListener != null) {
            cacheBuilder.removalListener(removalListener);
        }
        this.instanceCache = cacheBuilder.build();
    }

    public static PasswordEncoderFactoryBuilder builder() {
        return new PasswordEncoderFactoryBuilder();
    }

    public PasswordEncoderPort create(PasswordEncoderOption option) {
        return instanceCache.get(option, this::createWithOption);
    }

    private PasswordEncoderPort createWithOption(PasswordEncoderOption option) {
        Objects.requireNonNull(option);
        Objects.requireNonNull(option.encoderType());

        return switch (option.encoderType()) {
//            case GeneralPasswordEncoderType.PBKDF2 ->
//                    new Pbkdf2PasswordEncoderAdapter(option.as(BcryptPasswordEncoderOption.class));
            case GeneralPasswordEncoderType.BCRYPT ->
                    new BCryptPasswordEncoderAdapter(option.as(BcryptPasswordEncoderOption.class));
//            case GeneralPasswordEncoderType.SCRYPT ->
//                    new SCryptPasswordEncoderAdapter(option.as(ScryptPasswordEncoderOption.class));
            case GeneralPasswordEncoderType.ARGON2, Argon2Variant.ARGON2ID ->
                    new Argon2idPasswordEncoderAdapter(option.as(Argon2idPasswordEncoderOption.class));
//            case Argon2Variant.ARGON2I ->
//                    new Argon2iPasswordEncoderAdapter(option.as(Argon2iPasswordEncoderOption.class));
            case Argon2Variant.ARGON2D ->
                    new Argon2dPasswordEncoderAdapter(option.as(Argon2dPasswordEncoderOption.class));
            default ->
                    throw new Error("Not Supported");
        };
    }

    public static class PasswordEncoderFactoryBuilder {

        private long expireAfterAccessDuration = -1L;
        private TimeUnit expireAfterAccessTimeUnit = null;
        private long expirationAfterWriteDuration = -1L;
        private TimeUnit expirationAfterWriteTimeUnit;
        private long maximumSize = -1L;
        private long maximumWeight = -1L;
        private RemovalListener<PasswordEncoderOption, PasswordEncoderPort> removalListener = null;

        public PasswordEncoderFactoryBuilder expireAfterAccess(long duration) {
            this.expireAfterAccessDuration = duration;
            return this;
        }

        public PasswordEncoderFactoryBuilder expireAfterAccess(long duration, TimeUnit unit) {
            this.expireAfterAccessDuration = duration;
            this.expireAfterAccessTimeUnit = unit;
            return this;
        }

        public PasswordEncoderFactoryBuilder expirationAfterWriteDuration(long duration) {
            this.expirationAfterWriteDuration = duration;
            return this;
        }

        public PasswordEncoderFactoryBuilder expirationAfterWriteTimeUnit(long duration, TimeUnit timeUnit) {
            this.expirationAfterWriteDuration = duration;
            this.expirationAfterWriteTimeUnit = timeUnit;
            return this;
        }

        public PasswordEncoderFactoryBuilder maximumSize(long maximumSize) {
            this.maximumSize = maximumSize;
            return this;
        }

        public PasswordEncoderFactoryBuilder maximumWeight(long maximumWeight) {
            this.maximumWeight = maximumWeight;
            return this;
        }

        public PasswordEncoderFactoryBuilder removalListener(
                RemovalListener<PasswordEncoderOption, PasswordEncoderPort> removalListener
        ) {
            this.removalListener = removalListener;
            return this;
        }

        public PasswordEncoderFactory build() {
            return new PasswordEncoderFactory(
                    expireAfterAccessDuration,
                    expireAfterAccessTimeUnit,
                    expirationAfterWriteDuration,
                    expirationAfterWriteTimeUnit,
                    maximumSize,
                    maximumWeight,
                    removalListener
            );
        }
    }
}
