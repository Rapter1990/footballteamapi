package com.example.footballteamapi.auth.application.service;

import com.example.footballteamapi.auth.application.dto.request.TokenInvalidateRequest;

public interface LogoutService {

    void logout(final TokenInvalidateRequest tokenInvalidateRequest);

}
