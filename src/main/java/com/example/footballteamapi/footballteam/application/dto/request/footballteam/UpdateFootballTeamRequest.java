package com.example.footballteamapi.footballteam.application.dto.request.footballteam;

import jakarta.validation.constraints.NotBlank;

public record UpdateFootballTeamRequest(
        @NotBlank(message = "Team name must not be blank")
        String teamName
) {}
