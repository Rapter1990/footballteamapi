package com.example.footballteamapi.auth.application.service.impl;

import com.example.footballteamapi.auth.application.port.out.InvalidTokenRepository;
import com.example.footballteamapi.auth.application.service.InvalidTokenService;
import com.example.footballteamapi.auth.domain.exception.TokenAlreadyInvalidatedException;
import com.example.footballteamapi.auth.infrastructure.persistence.entity.InvalidTokenEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InvalidTokenServiceImpl implements InvalidTokenService {

    private final InvalidTokenRepository invalidTokenRepository;

    @Override
    public void invalidateTokens(Set<String> tokenIds) {
        final Set<InvalidTokenEntity> invalidTokenEntities = tokenIds.stream()
                .map(tokenId -> InvalidTokenEntity.builder()
                        .tokenId(tokenId)
                        .build()
                )
                .collect(Collectors.toSet());

        invalidTokenRepository.saveAll(invalidTokenEntities);
    }

    @Override
    public void checkForInvalidityOfToken(String tokenId) {
        final boolean isTokenInvalid = invalidTokenRepository.findByTokenId(tokenId).isPresent();

        if (isTokenInvalid) {
            throw new TokenAlreadyInvalidatedException(tokenId);
        }
    }

}
