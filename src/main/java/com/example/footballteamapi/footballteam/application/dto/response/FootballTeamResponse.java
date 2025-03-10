package com.example.footballteamapi.footballteam.application.dto.response;

import java.util.List;

public record FootballTeamResponse(String id,
                                   String teamName,
                                   List<PlayerResponse> players) {}
