package com.example.footballteamapi.auth.application.dto.response;

import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TokenResponse {

    private String accessToken;
    private Long accessTokenExpiresAt;
    private String refreshToken;

}
