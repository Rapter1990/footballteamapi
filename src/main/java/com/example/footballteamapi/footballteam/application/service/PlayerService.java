package com.example.footballteamapi.footballteam.application.service;

import com.example.footballteamapi.common.application.dto.request.CustomPagingRequest;
import com.example.footballteamapi.common.domain.model.CustomPage;
import com.example.footballteamapi.footballteam.application.dto.request.player.AddPlayerRequest;
import com.example.footballteamapi.footballteam.application.dto.request.player.UpdatePlayerRequest;
import com.example.footballteamapi.footballteam.domain.model.Player;

public interface PlayerService {

    Player addPlayerToTeam(String teamId, AddPlayerRequest addPlayerRequest);

    void deletePlayer(String teamId, String playerId);

    Player updatePlayer(String teamId, String playerId, UpdatePlayerRequest updatePlayerRequest);

    CustomPage<Player> getPlayersByTeamId(String teamId, CustomPagingRequest customPagingRequest);

}
