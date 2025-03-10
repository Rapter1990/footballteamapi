package com.example.footballteamapi.footballteam.application.port.out;

import com.example.footballteamapi.footballteam.infrastructure.persistence.entity.FootballTeamEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FootballTeamRepository extends JpaRepository<FootballTeamEntity, String> {

    boolean existsByTeamName(String name);

}
