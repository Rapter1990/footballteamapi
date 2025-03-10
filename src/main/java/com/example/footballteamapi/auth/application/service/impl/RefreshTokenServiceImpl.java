package com.example.footballteamapi.auth.application.service.impl;

import com.example.footballteamapi.auth.application.dto.request.TokenRefreshRequest;
import com.example.footballteamapi.auth.application.port.out.UserRepository;
import com.example.footballteamapi.auth.application.service.RefreshTokenService;
import com.example.footballteamapi.auth.application.service.TokenService;
import com.example.footballteamapi.auth.domain.enums.TokenClaims;
import com.example.footballteamapi.auth.domain.enums.UserStatus;
import com.example.footballteamapi.auth.domain.exception.UserNotFoundException;
import com.example.footballteamapi.auth.domain.exception.UserStatusNotValidException;
import com.example.footballteamapi.auth.domain.model.Token;
import com.example.footballteamapi.auth.infrastructure.persistence.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService {

    private final UserRepository userRepository;
    private final TokenService tokenService;

    @Override
    public Token refreshToken(TokenRefreshRequest tokenRefreshRequest) {
        tokenService.verifyAndValidate(tokenRefreshRequest.getRefreshToken());

        final String adminId = tokenService
                .getPayload(tokenRefreshRequest.getRefreshToken())
                .get(TokenClaims.USER_ID.getValue())
                .toString();

        final UserEntity userEntityFromDB = userRepository
                .findById(adminId)
                .orElseThrow(UserNotFoundException::new);

        this.validateAdminStatus(userEntityFromDB);

        return tokenService.generateToken(
                userEntityFromDB.getClaims(),
                tokenRefreshRequest.getRefreshToken()
        );
    }

    private void validateAdminStatus(final UserEntity userEntity) {
        if (!(UserStatus.ACTIVE.equals(userEntity.getUserStatus()))) {
            throw new UserStatusNotValidException("UserStatus = " + userEntity.getUserStatus());
        }
    }

}
