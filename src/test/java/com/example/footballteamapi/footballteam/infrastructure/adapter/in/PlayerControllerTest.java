package com.example.footballteamapi.footballteam.infrastructure.adapter.in;

import com.example.footballteamapi.base.AbstractRestControllerTest;
import com.example.footballteamapi.common.application.dto.response.CustomPagingResponse;
import com.example.footballteamapi.common.domain.model.CustomPage;
import com.example.footballteamapi.footballteam.application.dto.request.player.AddPlayerRequest;
import com.example.footballteamapi.footballteam.application.dto.request.player.PlayerPagingRequest;
import com.example.footballteamapi.footballteam.application.dto.request.player.UpdatePlayerRequest;
import com.example.footballteamapi.footballteam.application.dto.response.PlayerResponse;
import com.example.footballteamapi.footballteam.application.service.PlayerService;
import com.example.footballteamapi.footballteam.domain.enums.Position;
import com.example.footballteamapi.footballteam.domain.model.Player;
import com.example.footballteamapi.footballteam.infrastructure.mapper.player.CustomPagePlayerToCustomPagingPlayerResponseMapper;
import com.example.footballteamapi.footballteam.infrastructure.mapper.player.PlayerToPlayerResponseMapper;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class PlayerControllerTest extends AbstractRestControllerTest {

    @MockitoBean
    private PlayerService playerService;

    private final PlayerToPlayerResponseMapper playerToPlayerResponseMapper =
            PlayerToPlayerResponseMapper.initialize();
    private final CustomPagePlayerToCustomPagingPlayerResponseMapper customPagePlayerToCustomPagingPlayerResponseMapper =
            CustomPagePlayerToCustomPagingPlayerResponseMapper.initialize();

    @Test
    void givenValidAddPlayerRequest_whenAddPlayerForAdmin_thenReturnPlayerResponse() throws Exception {

        // Given
        String teamId = UUID.randomUUID().toString();
        AddPlayerRequest addRequest = AddPlayerRequest.builder()
                .name("Player One")
                .foreignPlayer(true)
                .position(Position.FORWARD)
                .build();

        Player player = Player.builder()
                .id(UUID.randomUUID().toString())
                .name("Player One")
                .foreignPlayer(true)
                .position(Position.FORWARD)
                .build();

        PlayerResponse expectedResponse = playerToPlayerResponseMapper.map(player);

        // When
        when(playerService.addPlayerToTeam(eq(teamId), any(AddPlayerRequest.class)))
                .thenReturn(player);

        // Then
        mockMvc.perform(post("/api/v1/football-teams/{teamId}/players", teamId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockAdminToken.getAccessToken())
                        .content(objectMapper.writeValueAsString(addRequest)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.response.id").value(expectedResponse.id()))
                .andExpect(jsonPath("$.response.name").value(expectedResponse.name()))
                .andExpect(jsonPath("$.response.foreignPlayer").value(expectedResponse.foreignPlayer()))
                .andExpect(jsonPath("$.response.position").value(expectedResponse.position().toString()));

        // Verify
        verify(playerService).addPlayerToTeam(eq(teamId), any(AddPlayerRequest.class));

    }

    @Test
    void givenValidAddPlayerRequest_whenAddPlayerWithoutToken_thenReturnUnauthorized() throws Exception {

        // Given
        String teamId = UUID.randomUUID().toString();
        AddPlayerRequest addRequest = AddPlayerRequest.builder()
                .name("Player One")
                .foreignPlayer(true)
                .position(Position.FORWARD)
                .build();

        // When & Then: Omit token to get Unauthorized (401)
        mockMvc.perform(post("/api/v1/football-teams/{teamId}/players", teamId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        // Verify
        verify(playerService, never()).addPlayerToTeam(any(String.class), any(AddPlayerRequest.class));

    }

    @Test
    void givenValidAddPlayerRequest_whenAddPlayerForUser_thenReturnForbidden() throws Exception {

        // Given
        String teamId = UUID.randomUUID().toString();
        AddPlayerRequest addRequest = AddPlayerRequest.builder()
                .name("Player One")
                .foreignPlayer(true)
                .position(Position.FORWARD)
                .build();

        // When & Then: Use non-admin token to get Forbidden (403)
        mockMvc.perform(post("/api/v1/football-teams/{teamId}/players", teamId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockUserToken.getAccessToken())
                        .content(objectMapper.writeValueAsString(addRequest)))
                .andDo(print())
                .andExpect(status().isForbidden());

        // Verify
        verify(playerService, never()).addPlayerToTeam(any(String.class), any(AddPlayerRequest.class));

    }

    @Test
    void givenValidUpdatePlayerRequest_whenUpdatePlayerForAdmin_thenReturnPlayerResponse() throws Exception {

        // Given
        String teamId = UUID.randomUUID().toString();
        String playerId = UUID.randomUUID().toString();
        UpdatePlayerRequest updateRequest = UpdatePlayerRequest.builder()
                .name("Updated Player")
                .foreignPlayer(false)
                .position(Position.MIDFIELDER)
                .build();

        Player player = Player.builder()
                .id(playerId)
                .name("Updated Player")
                .foreignPlayer(false)
                .position(Position.MIDFIELDER)
                .build();

        PlayerResponse expectedResponse = playerToPlayerResponseMapper.map(player);

        // When
        when(playerService.updatePlayer(eq(teamId), eq(playerId), any(UpdatePlayerRequest.class)))
                .thenReturn(player);

        // Then
        mockMvc.perform(put("/api/v1/football-teams/{teamId}/players/{playerId}", teamId, playerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockAdminToken.getAccessToken())
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.response.id").value(expectedResponse.id()))
                .andExpect(jsonPath("$.response.name").value(expectedResponse.name()))
                .andExpect(jsonPath("$.response.foreignPlayer").value(expectedResponse.foreignPlayer()))
                .andExpect(jsonPath("$.response.position").value(expectedResponse.position().toString()));

        // Verify
        verify(playerService).updatePlayer(eq(teamId), eq(playerId), any(UpdatePlayerRequest.class));
    }

    @Test
    void givenValidUpdatePlayerRequest_whenUpdatePlayerWithoutToken_thenReturnUnauthorized() throws Exception {

        // Given
        String teamId = UUID.randomUUID().toString();
        String playerId = UUID.randomUUID().toString();
        UpdatePlayerRequest updateRequest = UpdatePlayerRequest.builder()
                .name("Updated Player")
                .foreignPlayer(false)
                .position(Position.MIDFIELDER)
                .build();

        // When & Then: Omit token to expect Unauthorized (401)
        mockMvc.perform(put("/api/v1/football-teams/{teamId}/players/{playerId}", teamId, playerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        // Verify
        verify(playerService, never()).updatePlayer(any(String.class), any(String.class), any(UpdatePlayerRequest.class));

    }

    @Test
    void givenValidUpdatePlayerRequest_whenUpdatePlayerForUser_thenReturnForbidden() throws Exception {

        // Given
        String teamId = UUID.randomUUID().toString();
        String playerId = UUID.randomUUID().toString();
        UpdatePlayerRequest updateRequest = UpdatePlayerRequest.builder()
                .name("Updated Player")
                .foreignPlayer(false)
                .position(Position.MIDFIELDER)
                .build();

        // When & Then: Use non-admin token to expect Forbidden (403)
        mockMvc.perform(put("/api/v1/football-teams/{teamId}/players/{playerId}", teamId, playerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockUserToken.getAccessToken())
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isForbidden());

        // Verify
        verify(playerService, never()).updatePlayer(any(String.class), any(String.class), any(UpdatePlayerRequest.class));

    }

    @Test
    void givenExistingPlayerId_whenDeletePlayerForAdmin_thenReturnSuccess() throws Exception {

        // Given
        String teamId = UUID.randomUUID().toString();
        String playerId = UUID.randomUUID().toString();

        // When & Then
        mockMvc.perform(delete("/api/v1/football-teams/{teamId}/players/{playerId}", teamId, playerId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockAdminToken.getAccessToken()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.isSuccess").value(true));

        // Verify
        verify(playerService).deletePlayer(teamId, playerId);

    }

    @Test
    void givenExistingPlayerId_whenDeletePlayerWithoutToken_thenReturnUnauthorized() throws Exception {

        // Given
        String teamId = UUID.randomUUID().toString();
        String playerId = UUID.randomUUID().toString();

        // When & Then: Omit token to expect Unauthorized (401)
        mockMvc.perform(delete("/api/v1/football-teams/{teamId}/players/{playerId}", teamId, playerId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        // Verify
        verify(playerService, never()).deletePlayer(any(String.class), any(String.class));

    }

    @Test
    void givenExistingPlayerId_whenDeletePlayerForUser_thenReturnForbidden() throws Exception {

        // Given
        String teamId = UUID.randomUUID().toString();
        String playerId = UUID.randomUUID().toString();

        // When & Then: Use non-admin token to expect Forbidden (403)
        mockMvc.perform(delete("/api/v1/football-teams/{teamId}/players/{playerId}", teamId, playerId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockUserToken.getAccessToken()))
                .andDo(print())
                .andExpect(status().isForbidden());

        // Verify
        verify(playerService, never()).deletePlayer(any(String.class), any(String.class));

    }

    @Test
    void givenValidPlayerPagingRequest_whenGetPlayersByTeamIdForAdmin_thenReturnCustomPagingResponse() throws Exception {

        // Given
        String teamId = UUID.randomUUID().toString();
        PlayerPagingRequest pagingRequest = PlayerPagingRequest.builder()
                .pagination(com.example.footballteamapi.common.domain.model.CustomPaging.builder().pageNumber(1).pageSize(5).build())
                .build();

        // Create a sample player list and a dummy CustomPage using Mockito
        Player player = Player.builder()
                .id("p1")
                .name("Player One")
                .foreignPlayer(false)
                .position(Position.MIDFIELDER)
                .build();
        List<Player> players = Collections.singletonList(player);

        CustomPage<Player> customPage = mock(CustomPage.class);
        when(customPage.getContent()).thenReturn(players);
        when(customPage.getTotalElementCount()).thenReturn(1L);
        when(customPage.getTotalPageCount()).thenReturn(1);
        when(customPage.getPageNumber()).thenReturn(0);
        when(customPage.getPageSize()).thenReturn(5);

        CustomPagingResponse<PlayerResponse> expectedResponse =
                customPagePlayerToCustomPagingPlayerResponseMapper.toPagingResponse(customPage);

        // When
        when(playerService.getPlayersByTeamId(eq(teamId), any(PlayerPagingRequest.class)))
                .thenReturn(customPage);

        // Then
        mockMvc.perform(get("/api/v1/football-teams/{teamId}/players", teamId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockAdminToken.getAccessToken())
                        .content(objectMapper.writeValueAsString(pagingRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.response.totalElementCount").value(expectedResponse.getTotalElementCount()))
                .andExpect(jsonPath("$.response.totalPageCount").value(expectedResponse.getTotalPageCount()))
                .andExpect(jsonPath("$.response.pageNumber").value(expectedResponse.getPageNumber()))
                .andExpect(jsonPath("$.response.pageSize").value(expectedResponse.getPageSize()))
                .andDo(print());

        // Verify
        verify(playerService).getPlayersByTeamId(eq(teamId), any(PlayerPagingRequest.class));
    }

    @Test
    void givenValidPlayerPagingRequest_whenGetPlayersByTeamIdForUser_thenReturnCustomPagingResponse() throws Exception {

        // Given
        String teamId = UUID.randomUUID().toString();
        PlayerPagingRequest pagingRequest = PlayerPagingRequest.builder()
                .pagination(com.example.footballteamapi.common.domain.model.CustomPaging.builder().pageNumber(1).pageSize(5).build())
                .build();

        // Create a sample player list and a dummy CustomPage using Mockito
        Player player = Player.builder()
                .id("p1")
                .name("Player One")
                .foreignPlayer(false)
                .position(Position.MIDFIELDER)
                .build();
        List<Player> players = Collections.singletonList(player);

        CustomPage<Player> customPage = mock(CustomPage.class);
        when(customPage.getContent()).thenReturn(players);
        when(customPage.getTotalElementCount()).thenReturn(1L);
        when(customPage.getTotalPageCount()).thenReturn(1);
        when(customPage.getPageNumber()).thenReturn(0);
        when(customPage.getPageSize()).thenReturn(5);

        CustomPagingResponse<PlayerResponse> expectedResponse =
                customPagePlayerToCustomPagingPlayerResponseMapper.toPagingResponse(customPage);

        // When
        when(playerService.getPlayersByTeamId(eq(teamId), any(PlayerPagingRequest.class)))
                .thenReturn(customPage);

        // Then
        mockMvc.perform(get("/api/v1/football-teams/{teamId}/players", teamId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockUserToken.getAccessToken())
                        .content(objectMapper.writeValueAsString(pagingRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.response.totalElementCount").value(expectedResponse.getTotalElementCount()))
                .andExpect(jsonPath("$.response.totalPageCount").value(expectedResponse.getTotalPageCount()))
                .andExpect(jsonPath("$.response.pageNumber").value(expectedResponse.getPageNumber()))
                .andExpect(jsonPath("$.response.pageSize").value(expectedResponse.getPageSize()))
                .andDo(print());

        // Verify
        verify(playerService).getPlayersByTeamId(eq(teamId), any(PlayerPagingRequest.class));
    }

}