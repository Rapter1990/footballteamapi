package com.example.footballteamapi.auth.application.service;

import com.example.footballteamapi.auth.application.dto.request.LoginRequest;
import com.example.footballteamapi.auth.domain.model.Token;

public interface LoginService {

    Token login(final LoginRequest loginRequest);

}
