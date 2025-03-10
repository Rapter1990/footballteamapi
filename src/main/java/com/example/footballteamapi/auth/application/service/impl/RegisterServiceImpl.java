package com.example.footballteamapi.auth.application.service.impl;

import com.example.footballteamapi.auth.application.dto.request.RegisterRequest;
import com.example.footballteamapi.auth.application.port.out.UserRepository;
import com.example.footballteamapi.auth.application.service.RegisterService;
import com.example.footballteamapi.auth.domain.exception.UserAlreadyExistException;
import com.example.footballteamapi.auth.domain.model.User;
import com.example.footballteamapi.auth.infrastructure.mapper.RegisterRequestToUserEntityMapper;
import com.example.footballteamapi.auth.infrastructure.mapper.UserEntityToUserMapper;
import com.example.footballteamapi.auth.infrastructure.persistence.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RegisterServiceImpl implements RegisterService {

    private final UserRepository userRepository;

    private final RegisterRequestToUserEntityMapper registerRequestToUserEntityMapper =
            RegisterRequestToUserEntityMapper.initialize();

    private final UserEntityToUserMapper userEntityToUserMapper = UserEntityToUserMapper.initialize();

    private final PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(final RegisterRequest registerRequest) {

        if (userRepository.existsUserEntityByEmail(registerRequest.getEmail())) {
            throw new UserAlreadyExistException("The email is already used for another user : " + registerRequest.getEmail());
        }

        final UserEntity userEntityToBeSaved = registerRequestToUserEntityMapper.mapForSaving(registerRequest);

        userEntityToBeSaved.setPassword(passwordEncoder.encode(registerRequest.getPassword()));

        final UserEntity savedUserEntity = userRepository.save(userEntityToBeSaved);

        return userEntityToUserMapper.map(savedUserEntity);

    }

}
