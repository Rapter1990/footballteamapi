package com.example.footballteamapi.auth.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LoginRequest {

    @NotBlank
    private String email;

    @NotBlank
    private String password;

}
