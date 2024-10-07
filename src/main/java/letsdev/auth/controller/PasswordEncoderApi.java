package letsdev.auth.controller;

import letsdev.auth.controller.dto.PasswordEncodingRequest;
import letsdev.auth.controller.dto.PasswordEncodingResponse;
import letsdev.core.password.encoder.port.PasswordEncoderPort;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;

@RestController
public class PasswordEncoderApi {
    private static final Encoder BASE64_ENCODER = Base64.getEncoder().withoutPadding();
    private static final Decoder BASE64_DECODER = Base64.getDecoder();
    private final String exampleCustomSalt = BASE64_ENCODER.encodeToString(new byte[] {
            'S', 'a', 'l', 't', 'W', 'i', 't', 'h', '1', '6', 'l', 'e', 'n', 'g', 't', 'h'
    });

    private final PasswordEncoderPort passwordEncoder;
    private final PasswordEncoderPort passwordHistoryEncoder;

    public PasswordEncoderApi(
            PasswordEncoderPort passwordEncoder,
            @Qualifier("passwordHistoryEncoder")
            PasswordEncoderPort passwordHistoryEncoder
    ) {
        this.passwordEncoder = passwordEncoder;
        this.passwordHistoryEncoder = passwordHistoryEncoder;
    }

    @PostMapping("/password/encode")
    public PasswordEncodingResponse encode(@RequestBody PasswordEncodingRequest body) {
        String password = body.password();
        byte[] customSalt = BASE64_DECODER.decode(exampleCustomSalt);

        String encodedPassword = passwordEncoder.encode(password);
        String historyPassword = passwordHistoryEncoder.encodeWithCustomSalt(password, customSalt);

        return new PasswordEncodingResponse(
                encodedPassword,
                historyPassword
        );
    }
}
