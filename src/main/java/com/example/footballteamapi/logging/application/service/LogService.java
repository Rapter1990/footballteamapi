package com.example.footballteamapi.logging.application.service;

import com.example.footballteamapi.logging.domain.model.LogEntity;

public interface LogService {

    void saveLogToDatabase(final LogEntity logEntity);

}
