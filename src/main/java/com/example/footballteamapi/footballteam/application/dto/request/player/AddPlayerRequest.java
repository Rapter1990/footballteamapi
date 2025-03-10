package com.example.footballteamapi.footballteam.application.dto.request.player;

import com.example.footballteamapi.footballteam.domain.enums.Position;

public record AddPlayerRequest(String name,
                               boolean foreignPlayer,
                               Position position) {
}
