package com.example.footballteamapi.footballteam.application.dto.request.player;

import com.example.footballteamapi.footballteam.domain.enums.Position;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UpdatePlayerRequest(
        @NotBlank(message = "Player name must not be blank")
        String name,
        boolean foreignPlayer,
        @NotNull(message = "Position must not be null")
        Position position
) {}
