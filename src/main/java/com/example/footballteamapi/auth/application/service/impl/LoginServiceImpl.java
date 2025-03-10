package com.example.footballteamapi.auth.application.service.impl;

import com.example.footballteamapi.auth.application.dto.request.LoginRequest;
import com.example.footballteamapi.auth.application.port.out.UserRepository;
import com.example.footballteamapi.auth.application.service.LoginService;
import com.example.footballteamapi.auth.application.service.TokenService;
import com.example.footballteamapi.auth.domain.exception.PasswordNotValidException;
import com.example.footballteamapi.auth.domain.exception.UserNotFoundException;
import com.example.footballteamapi.auth.domain.model.Token;
import com.example.footballteamapi.auth.infrastructure.persistence.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Override
    public Token login(LoginRequest loginRequest) {

        final UserEntity userEntityFromDB = userRepository
                .findUserEntityByEmail(loginRequest.getEmail())
                .orElseThrow(
                        () -> new UserNotFoundException(loginRequest.getEmail())
                );

        if (Boolean.FALSE.equals(passwordEncoder.matches(
                loginRequest.getPassword(), userEntityFromDB.getPassword()))) {
            throw new PasswordNotValidException();
        }

        return tokenService.generateToken(userEntityFromDB.getClaims());

    }

}
