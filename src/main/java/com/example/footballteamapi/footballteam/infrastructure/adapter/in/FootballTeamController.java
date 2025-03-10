package com.example.footballteamapi.footballteam.infrastructure.adapter.in;

import com.example.footballteamapi.common.application.dto.request.CustomPagingRequest;
import com.example.footballteamapi.common.application.dto.response.CustomPagingResponse;
import com.example.footballteamapi.common.application.dto.response.CustomResponse;
import com.example.footballteamapi.common.domain.model.CustomPage;
import com.example.footballteamapi.footballteam.application.dto.request.footballteam.CreateFootballTeamRequest;
import com.example.footballteamapi.footballteam.application.dto.request.footballteam.UpdateFootballTeamRequest;
import com.example.footballteamapi.footballteam.application.dto.response.FootballTeamResponse;
import com.example.footballteamapi.footballteam.application.service.FootballTeamService;
import com.example.footballteamapi.footballteam.domain.model.FootballTeam;
import com.example.footballteamapi.footballteam.infrastructure.mapper.footballteam.CustomPageFootballTeamToCustomPagingFootballTeamResponseMapper;
import com.example.footballteamapi.footballteam.infrastructure.mapper.footballteam.FootballTeamToFootballTeamResponseMapper;
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
@RequestMapping("/api/v1/football-teams")
@RequiredArgsConstructor
@Validated
@Tag(name = "Football Teams", description = "Handles football team operations.")
public class FootballTeamController {

    private final FootballTeamService footballTeamService;

    private final FootballTeamToFootballTeamResponseMapper footballTeamToFootballTeamResponseMapper =
            FootballTeamToFootballTeamResponseMapper.initialize();

    private final CustomPageFootballTeamToCustomPagingFootballTeamResponseMapper customPageFootballTeamToCustomPagingFootballTeamResponseMapper =
            CustomPageFootballTeamToCustomPagingFootballTeamResponseMapper.initialize();

    @Operation(
            summary = "Create a new football team",
            description = "Creates a new football team in the system.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Football team successfully created"),
                    @ApiResponse(responseCode = "400", description = "Invalid request data")
            }
    )
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public CustomResponse<FootballTeamResponse> createTeam(@RequestBody @Valid final CreateFootballTeamRequest request) {
        FootballTeam team = footballTeamService.createTeam(request);
        FootballTeamResponse response = footballTeamToFootballTeamResponseMapper.map(team);
        return CustomResponse.successOf(response);
    }

    @Operation(
            summary = "Update a football team",
            description = "Updates an existing football team's name.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Football team successfully updated"),
                    @ApiResponse(responseCode = "404", description = "Football team not found")
            }
    )
    @PutMapping("/{teamId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public CustomResponse<FootballTeamResponse> updateTeam(
            @PathVariable @Valid @UUID final String teamId,
            @RequestBody @Valid final UpdateFootballTeamRequest request) {
        FootballTeam team = footballTeamService.updateTeam(teamId, request);
        FootballTeamResponse response = footballTeamToFootballTeamResponseMapper.map(team);
        return CustomResponse.successOf(response);
    }

    @Operation(
            summary = "Get football team by ID",
            description = "Retrieves a football team's details by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Football team details retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "Football team not found")
            }
    )
    @GetMapping("/{teamId}")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public CustomResponse<FootballTeamResponse> getTeamById(@PathVariable @Valid @UUID final String teamId) {
        FootballTeam team = footballTeamService.getTeamById(teamId);
        FootballTeamResponse response = footballTeamToFootballTeamResponseMapper.map(team);
        return CustomResponse.successOf(response);
    }

    @Operation(
            summary = "Delete a football team",
            description = "Deletes a football team by its ID.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Football team successfully deleted"),
                    @ApiResponse(responseCode = "404", description = "Football team not found")
            }
    )
    @DeleteMapping("/{teamId}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public CustomResponse<Void> deleteTeam(@PathVariable @Valid @UUID final String teamId) {
        footballTeamService.deleteTeam(teamId);
        return CustomResponse.SUCCESS;
    }

    @Operation(
            summary = "List football teams with pagination",
            description = "Retrieves a paginated list of football teams.",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Football teams retrieved successfully"),
                    @ApiResponse(responseCode = "404", description = "No football teams found")
            }
    )
    @PostMapping("/teamsList")
    @PreAuthorize("hasAnyAuthority('ADMIN','USER')")
    public CustomResponse<CustomPagingResponse<FootballTeamResponse>> getAllTeamsWithPageable(@RequestBody @Valid final CustomPagingRequest request) {
        CustomPage<FootballTeam> domainPage = footballTeamService.getAllTeamsWithPageable(request);
        final CustomPagingResponse<FootballTeamResponse> response = customPageFootballTeamToCustomPagingFootballTeamResponseMapper
                .toPagingResponse(domainPage);
        return CustomResponse.successOf(response);
    }

}