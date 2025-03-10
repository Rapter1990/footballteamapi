package com.example.footballteamapi.auth.application.service.impl;

import com.example.footballteamapi.auth.application.dto.request.TokenInvalidateRequest;
import com.example.footballteamapi.auth.application.service.InvalidTokenService;
import com.example.footballteamapi.auth.application.service.LogoutService;
import com.example.footballteamapi.auth.application.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
@RequiredArgsConstructor
public class LogoutServiceImpl implements LogoutService {

    private final TokenService tokenService;
    private final InvalidTokenService invalidTokenService;

    @Override
    public void logout(TokenInvalidateRequest tokenInvalidateRequest) {

        tokenService.verifyAndValidate(
                Set.of(
                        tokenInvalidateRequest.getAccessToken(),
                        tokenInvalidateRequest.getRefreshToken()
                )
        );

        final String accessTokenId = tokenService
                .getPayload(tokenInvalidateRequest.getAccessToken())
                .getId();

        invalidTokenService.checkForInvalidityOfToken(accessTokenId);


        final String refreshTokenId = tokenService
                .getPayload(tokenInvalidateRequest.getRefreshToken())
                .getId();

        invalidTokenService.checkForInvalidityOfToken(refreshTokenId);

        invalidTokenService.invalidateTokens(Set.of(accessTokenId,refreshTokenId));

    }

}
