package letsdev.core.password.encoder.adapter;

public record DecodedSaltAndDigest(byte[] salt, byte[] digest) {
}
