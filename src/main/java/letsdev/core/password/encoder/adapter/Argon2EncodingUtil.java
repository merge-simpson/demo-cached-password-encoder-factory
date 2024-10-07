package letsdev.core.password.encoder.adapter;

import letsdev.core.password.exception.PasswordEncoderGenerationException;
import org.bouncycastle.crypto.params.Argon2Parameters;

import java.util.Base64;

final class Argon2EncodingUtil {

    private static final Base64.Encoder BASE64_ENCODER = Base64.getEncoder().withoutPadding();
    private static final Base64.Decoder BASE64_DECODER = Base64.getDecoder();
    private static final String PREFIX = "{argon2}";

    static String encode(byte[] hash, Argon2Parameters parameters) {
        StringBuilder stringBuilder = new StringBuilder(PREFIX);
        String type = switch (parameters.getType()) {
            case 0 -> "$argon2d";
            case 1 -> "$argon2i";
            case 2 -> "$argon2id";
            default ->
                    throw new PasswordEncoderGenerationException(STR."Invalid algorithm type: \{parameters.getType()}");
        };
        stringBuilder.append(type);
        stringBuilder.append("$v=")
                .append(parameters.getVersion())
                .append("$m=")
                .append(parameters.getMemory())
                .append(",t=")
                .append(parameters.getIterations())
                .append(",p=")
                .append(parameters.getLanes());
        if (parameters.getSalt() != null) {
            stringBuilder.append("$").append(BASE64_ENCODER.encodeToString(parameters.getSalt()));
        }

        stringBuilder.append("$").append(BASE64_ENCODER.encodeToString(hash));
        return stringBuilder.toString();
    }

    static DecodedSaltAndDigest decode(String encodedPassword) {
        String[] parts = encodedPassword.split("\\$");
        String saltBase64 = parts[parts.length - 2];
        String digestBase64 = parts[parts.length - 1];
        byte[] salt = BASE64_DECODER.decode(saltBase64);
        byte[] digest = BASE64_DECODER.decode(digestBase64);
        return new DecodedSaltAndDigest(salt, digest);
    }
}
