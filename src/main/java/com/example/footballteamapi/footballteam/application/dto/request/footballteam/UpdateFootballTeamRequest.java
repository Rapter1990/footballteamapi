package com.example.footballteamapi.footballteam.application.dto.request.footballteam;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UpdateFootballTeamRequest(
        @NotBlank(message = "Team name must not be blank")
        String teamName
) {}
