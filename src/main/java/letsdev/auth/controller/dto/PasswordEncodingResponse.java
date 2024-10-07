package letsdev.auth.controller.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record PasswordEncodingResponse(
        @JsonProperty("encoded_password")
        String encodedPassword,
        @JsonProperty("history_password")
        String historyPassword
) {
}
