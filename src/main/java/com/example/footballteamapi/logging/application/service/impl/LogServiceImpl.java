package com.example.footballteamapi.logging.application.service.impl;

import com.example.footballteamapi.logging.application.service.LogService;
import com.example.footballteamapi.logging.domain.model.LogEntity;
import com.example.footballteamapi.logging.infrastructure.repository.LogRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class LogServiceImpl implements LogService {

    private final LogRepository logRepository;

    @Override
    public void saveLogToDatabase(final LogEntity logEntity) {
        logEntity.setTime(LocalDateTime.now());
        logRepository.save(logEntity);
    }

}
