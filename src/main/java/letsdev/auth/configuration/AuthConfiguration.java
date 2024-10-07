package letsdev.auth.configuration;

import letsdev.core.password.PasswordEncoderFactory;
import letsdev.core.password.encoder.option.Argon2dPasswordEncoderOption;
import letsdev.core.password.encoder.option.Argon2idPasswordEncoderOption;
import letsdev.core.password.encoder.port.CustomSaltingPasswordEncoder;
import letsdev.core.password.encoder.port.PasswordEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

@Configuration
public class AuthConfiguration {
    @Bean
    public PasswordEncoderFactory passwordEncoderFactory() {
        return new PasswordEncoderFactory();
    }

    @Bean
    @Primary
    public PasswordEncoder passwordEncoder(PasswordEncoderFactory passwordEncoderFactory) {
        var option = Argon2idPasswordEncoderOption.fromDefaultBuilder()
                .gain(3f)
                .build();
        return passwordEncoderFactory.create(option);
    }

    @Bean
    public CustomSaltingPasswordEncoder passwordHistoryEncoder(PasswordEncoderFactory passwordEncoderFactory) {
        var option = Argon2dPasswordEncoderOption.fromDefaultBuilder()
                .gain(3f)
                .build();
        return passwordEncoderFactory.createCustomSaltingEncoder(option);
    }
}
