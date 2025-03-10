package com.example.footballteamapi.auth.application.port.out;

import com.example.footballteamapi.auth.infrastructure.persistence.entity.InvalidTokenEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface InvalidTokenRepository extends JpaRepository<InvalidTokenEntity, String> {

    Optional<InvalidTokenEntity> findByTokenId(final String tokenId);

}
