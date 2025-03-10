package com.example.footballteamapi.footballteam.infrastructure.adapter.in;

import com.example.footballteamapi.common.application.dto.request.CustomPagingRequest;
import com.example.footballteamapi.common.application.dto.response.CustomPagingResponse;
import com.example.footballteamapi.common.application.dto.response.CustomResponse;
import com.example.footballteamapi.common.domain.model.CustomPage;
import com.example.footballteamapi.footballteam.application.dto.request.player.AddPlayerRequest;
import com.example.footballteamapi.footballteam.application.dto.request.player.PlayerPagingRequest;
import com.example.footballteamapi.footballteam.application.dto.request.player.UpdatePlayerRequest;
import com.example.footballteamapi.footballteam.application.dto.response.PlayerResponse;
import com.example.footballteamapi.footballteam.application.service.PlayerService;
import com.example.footballteamapi.footballteam.domain.model.Player;
import com.example.footballteamapi.footballteam.infrastructure.mapper.player.CustomPagePlayerToCustomPagingPlayerResponseMapper;
import com.example.footballteamapi.footballteam.infrastructure.mapper.player.PlayerToPlayerResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.UUID;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/football-teams/{teamId}/players")
@RequiredArgsConstructor
@Validated
@Tag(name = "Players", description = "Handles player operations for a football team.")
public class PlayerController {

    private final PlayerService playerService;

    private final PlayerToPlayerResponseMapper playerToPlayerResponseMapper = PlayerToPlayerResponseMapper.initialize();

    private final CustomPagePlayerToCustomPagingPlayerResponseMapper customPagePlayerToCustomPagingPlayerResponseMapper =
            CustomPagePlayerToCustomPagingPlayerResponseMapper.initialize();

    @Operation(
            summary = "Add a new player to a team",
            description = "Adds a new player to the specified football team.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Player successfully added"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data"),
                    @ApiResponse(responseCode = "404", description = "Football team not found")
            }
    )
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public CustomResponse<PlayerResponse> addPlayerToTeam(
            @PathVariable @Valid @UUID  String teamId,
            @RequestBody @Valid AddPlayerRequest addPlayerRequest) {
        Player player = playerService.addPlayerToTeam(teamId, addPlayerRequest);
        PlayerResponse response = playerToPlayerResponseMapper.map(player);
        return CustomResponse.successOf(response);
    }

    @Operation(
            summary = "Update a player in a team",
            description = "Updates an existing player on the specified football team.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Player successfully updated"),
                    @ApiResponse(responseCode = "404", description = "Player or team not found")
            }
    )
    @PutMapping("/{playerId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public CustomResponse<PlayerResponse> updatePlayer(
            @PathVariable @Valid @UUID  String teamId,
            @PathVariable @Valid @UUID  String playerId,
            @RequestBody @Valid UpdatePlayerRequest updatePlayerRequest) {
        Player player = playerService.updatePlayer(teamId, playerId, updatePlayerRequest);
        PlayerResponse response = playerToPlayerResponseMapper.map(player);
        return CustomResponse.successOf(response);
    }

    @Operation(
            summary = "Delete a player from a team",
            description = "Deletes a player from the specified football team.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Player successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "Player or team not found")
            }
    )
    @PreAuthorize("hasAuthority('ADMIN')")
    @DeleteMapping("/{playerId}")
    public CustomResponse<Void> deletePlayer(
            @PathVariable @Valid @UUID String teamId,
            @PathVariable @Valid @UUID  String playerId) {
        playerService.deletePlayer(teamId, playerId);
        return CustomResponse.SUCCESS;
    }

    @Operation(
            summary = "List players with pagination",
            description = "Retrieves a paginated list of players for the specified football team.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Players retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "No players found for the team")
            }
    )
    @GetMapping
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public CustomResponse<CustomPagingResponse<PlayerResponse>> getPlayersByTeamId(
            @PathVariable @Valid @UUID String teamId,
            @RequestBody @Valid final PlayerPagingRequest request) {
        CustomPage<Player> domainPage = playerService.getPlayersByTeamId(teamId, request);
        CustomPagingResponse<PlayerResponse> response = customPagePlayerToCustomPagingPlayerResponseMapper.toPagingResponse(domainPage);
        return CustomResponse.successOf(response);
    }


}
