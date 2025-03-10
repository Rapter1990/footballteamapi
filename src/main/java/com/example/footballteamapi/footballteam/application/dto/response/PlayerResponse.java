package com.example.footballteamapi.footballteam.application.dto.response;

import com.example.footballteamapi.footballteam.domain.enums.Position;

public record PlayerResponse(String id,
                             String name,
                             boolean foreignPlayer,
                             Position position) {}
