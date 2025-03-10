package com.example.footballteamapi.footballteam.application.port.out;

import com.example.footballteamapi.footballteam.infrastructure.persistence.entity.PlayerEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PlayerRepository extends JpaRepository<PlayerEntity, String> {

    Page<PlayerEntity> findByFootballTeam_Id(String teamId, Pageable pageable);

}
