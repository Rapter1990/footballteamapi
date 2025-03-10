package com.example.footballteamapi.footballteam.application.service.impl;

import com.example.footballteamapi.common.application.dto.request.CustomPagingRequest;
import com.example.footballteamapi.common.domain.model.CustomPage;
import com.example.footballteamapi.footballteam.application.dto.request.player.AddPlayerRequest;
import com.example.footballteamapi.footballteam.application.dto.request.player.UpdatePlayerRequest;
import com.example.footballteamapi.footballteam.application.port.out.FootballTeamRepository;
import com.example.footballteamapi.footballteam.application.port.out.PlayerRepository;
import com.example.footballteamapi.footballteam.application.service.PlayerService;
import com.example.footballteamapi.footballteam.domain.enums.Position;
import com.example.footballteamapi.footballteam.domain.exception.footballteam.FootballTeamNotFoundException;
import com.example.footballteamapi.footballteam.domain.exception.player.MaxPlayersExceededException;
import com.example.footballteamapi.footballteam.domain.exception.player.PlayerNotFoundException;
import com.example.footballteamapi.footballteam.domain.exception.player.PlayerTeamMismatchException;
import com.example.footballteamapi.footballteam.domain.model.Player;
import com.example.footballteamapi.footballteam.infrastructure.mapper.player.AddPlayerRequestToPlayerEntityMapper;
import com.example.footballteamapi.footballteam.infrastructure.mapper.player.PlayerEntityToPlayerMapper;
import com.example.footballteamapi.footballteam.infrastructure.mapper.player.UpdatePlayerRequestToPlayerEntityMapper;
import com.example.footballteamapi.footballteam.infrastructure.persistence.entity.FootballTeamEntity;
import com.example.footballteamapi.footballteam.infrastructure.persistence.entity.PlayerEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlayerServiceImpl implements PlayerService {

    private final FootballTeamRepository footballTeamRepository;
    private final PlayerRepository playerRepository;
    private final AddPlayerRequestToPlayerEntityMapper addPlayerRequestToPlayerEntityMapper =
            AddPlayerRequestToPlayerEntityMapper.initialize();

    private final PlayerEntityToPlayerMapper playerEntityToPlayerMapper =
            PlayerEntityToPlayerMapper.initialize();

    private final UpdatePlayerRequestToPlayerEntityMapper updatePlayerRequestToPlayerEntityMapper =
            UpdatePlayerRequestToPlayerEntityMapper.initialize();

    /**
     * Adds a player to a team based on the provided AddPlayerRequest.
     *
     * @param teamId the ID of the football team.
     * @param addPlayerRequest the request data for adding a new player.
     * @return the added player as a domain model.
     */
    @Override
    public Player addPlayerToTeam(String teamId, AddPlayerRequest addPlayerRequest) {

        FootballTeamEntity teamEntity = footballTeamRepository.findById(teamId)
                .orElseThrow(() -> new FootballTeamNotFoundException("Team with id " + teamId + " does not exist"));

        // Business Rule 1: Maximum 18 players per team.
        if (teamEntity.getPlayers().size() >= 18) {
            throw new MaxPlayersExceededException();
        }

        // Business Rule 2: Maximum 6 foreign players per team.
        long foreignCount = teamEntity.getPlayers().stream()
                .filter(PlayerEntity::isForeignPlayer)
                .count();
        if (addPlayerRequest.foreignPlayer() && foreignCount >= 6) {
            throw new IllegalStateException("A team can have at most 6 foreign players");
        }

        // Business Rule 3: Maximum 2 goalkeepers per team.
        if (addPlayerRequest.position() == Position.GOALKEEPER) {
            long goalkeeperCount = teamEntity.getPlayers().stream()
                    .filter(player -> player.getPosition() == Position.GOALKEEPER)
                    .count();
            if (goalkeeperCount >= 2) {
                throw new IllegalStateException("A team can have at most 2 goalkeepers");
            }
        }

        // Map the AddPlayerRequest to a new PlayerEntity.
        PlayerEntity playerEntity = addPlayerRequestToPlayerEntityMapper.mapForSaving(addPlayerRequest);

        // Set the team association.
        playerEntity.setFootballTeam(teamEntity);

        // Save the new player.
        PlayerEntity savedEntity = playerRepository.save(playerEntity);

        // Update the team's player list.
        teamEntity.getPlayers().add(savedEntity);
        footballTeamRepository.save(teamEntity);

        // Convert the saved entity to the domain model and return.
        return playerEntityToPlayerMapper.map(savedEntity);

    }

    @Override
    public void deletePlayer(String teamId, String playerId) {

        FootballTeamEntity teamEntity = footballTeamRepository.findById(teamId)
                .orElseThrow(() -> new FootballTeamNotFoundException("Team with id " + teamId + " does not exist"));

        PlayerEntity playerEntity = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player with id " + playerId + " does not exist"));

        if (!playerEntity.getFootballTeam().getId().equals(teamEntity.getId())) {
            throw new PlayerTeamMismatchException("Mismatch between player's team and provided team id.");
        }

        teamEntity.getPlayers().remove(playerEntity);

        playerRepository.delete(playerEntity);

    }

    /**
     * Updates an existing player on a team based on the provided UpdatePlayerRequest.
     *
     * @param teamId             the ID of the football team.
     * @param playerId           the ID of the player to update.
     * @param updatePlayerRequest the request data for updating the player.
     * @return the updated player as a domain model.
     */
    @Override
    public Player updatePlayer(String teamId, String playerId, UpdatePlayerRequest updatePlayerRequest) {

        // Retrieve the team entity; throw if not found.
        FootballTeamEntity teamEntity = footballTeamRepository.findById(teamId)
                .orElseThrow(() -> new FootballTeamNotFoundException("Team with id " + teamId + " does not exist"));

        // Retrieve the player entity; throw if not found.
        PlayerEntity playerEntity = playerRepository.findById(playerId)
                .orElseThrow(() -> new PlayerNotFoundException("Player with id " + playerId + " does not exist"));

        // Verify that the player belongs to the given team.
        if (!playerEntity.getFootballTeam().getId().equals(teamEntity.getId())) {
            throw new PlayerTeamMismatchException("Mismatch between player's team and provided team id.");
        }

        // Update the player entity using data from the update request.
        updatePlayerRequestToPlayerEntityMapper.mapForUpdating(updatePlayerRequest, playerEntity);

        // Save the updated player entity.
        PlayerEntity updatedEntity = playerRepository.save(playerEntity);

        // Return the updated domain model.
        return playerEntityToPlayerMapper.map(updatedEntity);

    }

    @Override
    public CustomPage<Player> getPlayersByTeamId(String teamId, CustomPagingRequest customPagingRequest) {

        // Validate team existence.
        footballTeamRepository.findById(teamId)
                .orElseThrow(() -> new FootballTeamNotFoundException("Team with id " + teamId + " does not exist"));

        Page<PlayerEntity> playerEntities = playerRepository.findByFootballTeam_Id(teamId, customPagingRequest.toPageable());

        if (playerEntities.getContent().isEmpty()) {
            throw new PlayerNotFoundException("No players found for team with id " + teamId);
        }

        List<Player> players = playerEntities.getContent().stream()
                .map(playerEntityToPlayerMapper::map)
                .collect(Collectors.toList());
        return CustomPage.of(players, playerEntities);

    }

}
