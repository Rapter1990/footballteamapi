package com.example.footballteamapi.auth.application.service;

import com.example.footballteamapi.auth.application.dto.request.TokenRefreshRequest;
import com.example.footballteamapi.auth.domain.model.Token;

public interface RefreshTokenService {

    Token refreshToken(final TokenRefreshRequest tokenRefreshRequest);

}
