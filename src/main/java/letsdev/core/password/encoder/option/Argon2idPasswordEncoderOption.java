package letsdev.core.password.encoder.option;

import letsdev.core.password.encoder.GeneralPasswordEncoderType.Argon2Variant;

/**
 *
 * @param saltLength recommended length: 16 Byte
 * @param hashLength recommended length: 32 Byte
 * @param parallelism p = 1
 * @param memory 메모리 비용 m ≥ 93750 ÷ ((3 × t − 1) × α)  (단위: kB) (수식은 추천되는 범위)
 * @param iterations t ≥ 1
 * @param alpha α ≈ 95% where m ≲ 64 MiB, m이 충분히 크면 α를 감소시켜도 됨.
 * @param gain 메모리 비용 계수(증폭비)
 * @param memoryInput 사용자에 의해 입력된 메모리 비용 보존값
 */
public record Argon2idPasswordEncoderOption(
        int saltLength,
        int hashLength,
        int parallelism,
        int memory,
        int iterations,
        float alpha,
        float gain,
        int memoryInput
) implements Argon2PasswordEncoderOption {

    @Override
    public Argon2Variant encoderType() {
        return Argon2Variant.ARGON2ID;
    }

    public static Argon2idPasswordEncoderOptionBuilder builder() {
        return new Argon2idPasswordEncoderOptionBuilder();
    }

    public static Argon2idPasswordEncoderOptionBuilder fromDefaultBuilder() {
        var builder = new Argon2idPasswordEncoderOptionBuilder();
        builder.saltLength =  16;
        builder.hashLength =  32;
        builder.parallelism =  1;
        builder.iterations =  1;
        builder.alpha =  0.95f;
        builder.memoryInput =  null;
        builder.gain =  1f;
        return builder;
    }

    public static class Argon2idPasswordEncoderOptionBuilder {
        private Integer saltLength;
        private Integer hashLength;
        private Integer parallelism;
        private Integer iterations;
        private Float alpha;
        private Integer memoryInput;
        private Float gain;

        private Argon2idPasswordEncoderOptionBuilder() {

        }

        public Argon2idPasswordEncoderOptionBuilder saltLength(int saltLength) {
            this.saltLength = saltLength;
            return this;
        }

        public Argon2idPasswordEncoderOptionBuilder hashLength(int hashLength) {
            this.hashLength = hashLength;
            return this;
        }

        public Argon2idPasswordEncoderOptionBuilder parallelism(int parallelism) {
            this.parallelism = parallelism;
            return this;
        }

        public Argon2idPasswordEncoderOptionBuilder iterations(int iterations) {
            this.iterations = iterations;
            return this;
        }

        public Argon2idPasswordEncoderOptionBuilder alpha(Float alpha) {
            this.alpha = alpha;
            return this;
        }

        public Argon2idPasswordEncoderOptionBuilder memoryInput(int memoryInput) {
            this.memoryInput = memoryInput;
            return this;
        }

        public Argon2idPasswordEncoderOptionBuilder gain(float gain) {
            this.gain = gain;
            return this;
        }

        public Argon2PasswordEncoderOption build() {
            int memory = memoryInput != null ? memoryInput : 0;
            if (saltLength == null) {
                saltLength = 16;
            } else if (saltLength <= 0) {
                throw new Error("salt length는 양수여야 합니다.");
            }

            if (hashLength == null) {
                hashLength = 32;
            } else if (hashLength <= 0) {
                throw new Error("hash length는 양수여야 합니다.");
            }

            if (parallelism == null) {
                parallelism = 1;
            } else if (parallelism <= 0) {
                throw new Error("parallelism은 양수여야 합니다.");
            }

            if (iterations == null) {
                iterations = 1;
            } else if (iterations <= 0) {
                throw new Error("iterations는 양수여야 합니다.");
            }

            if (alpha == null) {
                alpha = 0.95f;
            } else if (alpha <= 0 || alpha > 1F) {
                throw new Error("iterations는 양수여야 합니다.");
            }

            if (gain == null) {
                gain = 1f;
            } else if (gain <= 0) {
                throw new Error("memory coefficient는 양수여야 합니다.");
            }

            if (memoryInput == null) {
                float num = 93750f;
                float den = (3 * iterations - 1) * alpha;
                memory = (int) Math.ceil(num / den);
                memoryInput = memory;
            } else if (memoryInput <= 0) {
                throw new Error("memory cost는 양수여야 합니다.");
            }

            memory = (int) (memory * gain);
            return new Argon2idPasswordEncoderOption(
                    saltLength,
                    hashLength,
                    parallelism,
                    memory,
                    iterations,
                    alpha,
                    gain,
                    memoryInput
            );
        }
    }
}
