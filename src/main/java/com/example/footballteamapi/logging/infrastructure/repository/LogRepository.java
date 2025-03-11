package com.example.footballteamapi.logging.infrastructure.repository;

import com.example.footballteamapi.logging.domain.model.LogEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LogRepository extends JpaRepository<LogEntity,String> {

}
