package com.example.footballteamapi.footballteam.application.service;

import com.example.footballteamapi.common.application.dto.request.CustomPagingRequest;
import com.example.footballteamapi.common.domain.model.CustomPage;
import com.example.footballteamapi.footballteam.application.dto.request.footballteam.CreateFootballTeamRequest;
import com.example.footballteamapi.footballteam.application.dto.request.footballteam.UpdateFootballTeamRequest;
import com.example.footballteamapi.footballteam.domain.model.FootballTeam;

public interface FootballTeamService {

    FootballTeam createTeam(CreateFootballTeamRequest request);

    FootballTeam updateTeam(String teamId, UpdateFootballTeamRequest request);

    FootballTeam getTeamById(String teamId);

    void deleteTeam(String teamId);

    CustomPage<FootballTeam> getAllTeamsWithPageable(CustomPagingRequest request);

}
