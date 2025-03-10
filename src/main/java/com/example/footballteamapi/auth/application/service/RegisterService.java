package com.example.footballteamapi.auth.application.service;

import com.example.footballteamapi.auth.application.dto.request.RegisterRequest;
import com.example.footballteamapi.auth.domain.model.User;

public interface RegisterService {

    User registerUser(final RegisterRequest registerRequest);

}
