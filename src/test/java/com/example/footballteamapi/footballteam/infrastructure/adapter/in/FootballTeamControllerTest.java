package com.example.footballteamapi.footballteam.infrastructure.adapter.in;

import com.example.footballteamapi.base.AbstractRestControllerTest;
import com.example.footballteamapi.common.application.dto.response.CustomPagingResponse;
import com.example.footballteamapi.common.domain.model.CustomPage;
import com.example.footballteamapi.common.domain.model.CustomPaging;
import com.example.footballteamapi.footballteam.application.dto.request.footballteam.CreateFootballTeamRequest;
import com.example.footballteamapi.footballteam.application.dto.request.footballteam.FootballTeamPagingRequest;
import com.example.footballteamapi.footballteam.application.dto.request.footballteam.UpdateFootballTeamRequest;
import com.example.footballteamapi.footballteam.application.dto.response.FootballTeamResponse;
import com.example.footballteamapi.footballteam.application.service.FootballTeamService;
import com.example.footballteamapi.footballteam.domain.model.FootballTeam;
import com.example.footballteamapi.footballteam.infrastructure.mapper.footballteam.CustomPageFootballTeamToCustomPagingFootballTeamResponseMapper;
import com.example.footballteamapi.footballteam.infrastructure.mapper.footballteam.FootballTeamToFootballTeamResponseMapper;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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

class FootballTeamControllerTest extends AbstractRestControllerTest {

    @MockitoBean
    FootballTeamService footballTeamService;

    private final FootballTeamToFootballTeamResponseMapper footballTeamToFootballTeamResponseMapper =
            FootballTeamToFootballTeamResponseMapper.initialize();

    private final CustomPageFootballTeamToCustomPagingFootballTeamResponseMapper customPageFootballTeamToCustomPagingFootballTeamResponseMapper =
            CustomPageFootballTeamToCustomPagingFootballTeamResponseMapper.initialize();

    @Test
    void givenValidCreateTeamRequest_whenCreateTeamForAdmin_thenReturnFootballTeamResponse() throws Exception {

        // Given
        CreateFootballTeamRequest createRequest = CreateFootballTeamRequest.builder()
                .teamName("Team A")
                .build();

        FootballTeam team = FootballTeam.builder()
                .id(UUID.randomUUID().toString())
                .teamName("Team A")
                .build();

        FootballTeamResponse response = footballTeamToFootballTeamResponseMapper.map(team);

        // When
        when(footballTeamService.createTeam(any(CreateFootballTeamRequest.class)))
                .thenReturn(team);

        // Then
        mockMvc.perform(post("/api/v1/football-teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest))
                        .header(HttpHeaders.AUTHORIZATION,"Bearer " + mockAdminToken.getAccessToken()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.response.id").value(response.id()))
                .andExpect(jsonPath("$.response.teamName").value(response.teamName()));

        // Verify
        verify(footballTeamService).createTeam(any(CreateFootballTeamRequest.class));

    }

    @Test
    void givenValidCreateTeamRequest_whenCreateTeamForUser_thenReturnForbidden() throws Exception {

        // Given
        CreateFootballTeamRequest createRequest = CreateFootballTeamRequest.builder()
                .teamName("Team A")
                .build();

        // When & Then
        mockMvc.perform(post("/api/v1/football-teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockUserToken.getAccessToken())
                        .content(objectMapper.writeValueAsString(createRequest))
                )
                .andDo(print())
                .andExpect(status().isForbidden());

        // Verify
        verify(footballTeamService, never()).createTeam(any(CreateFootballTeamRequest.class));

    }

    @Test
    void givenValidCreateTeamRequest_whenCreateTeamWithoutToken_thenReturnUnauthorized() throws Exception {

        // Given
        CreateFootballTeamRequest createRequest = CreateFootballTeamRequest.builder()
                .teamName("Team A")
                .build();

        // When & Then
        mockMvc.perform(post("/api/v1/football-teams")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createRequest))
                )
                .andDo(print())
                .andExpect(status().isUnauthorized());

        // Verify
        verify(footballTeamService, never()).createTeam(any(CreateFootballTeamRequest.class));

    }

    @Test
    void givenValidUpdateTeamRequest_whenUpdateTeamForAdmin_thenReturnFootballTeamResponse() throws Exception {

        // Given
        String teamId = UUID.randomUUID().toString();

        UpdateFootballTeamRequest updateRequest = UpdateFootballTeamRequest.builder()
                .teamName("Team A Updated")
                .build();

        FootballTeam team = FootballTeam.builder()
                .id(teamId)
                .teamName("Team A Updated")
                .build();

        FootballTeamResponse response = footballTeamToFootballTeamResponseMapper.map(team);

        // When
        when(footballTeamService.updateTeam(eq(teamId), any(UpdateFootballTeamRequest.class)))
                .thenReturn(team);

        // Then
        mockMvc.perform(put("/api/v1/football-teams/{teamId}", teamId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockAdminToken.getAccessToken()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.response.id").value(response.id()))
                .andExpect(jsonPath("$.response.teamName").value(response.teamName()));

        // Verify
        verify(footballTeamService).updateTeam(eq(teamId), any(UpdateFootballTeamRequest.class));

    }

    @Test
    void givenValidUpdateTeamRequest_whenUpdateTeamWithoutToken_thenReturnUnauthorized() throws Exception {

        // Given
        String teamId = UUID.randomUUID().toString();
        UpdateFootballTeamRequest updateRequest = UpdateFootballTeamRequest.builder()
                .teamName("Team A Updated")
                .build();

        // When & Then: Omitting the token should result in Unauthorized (401)
        mockMvc.perform(put("/api/v1/football-teams/{teamId}", teamId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        // Verify: Service method should not be called.
        verify(footballTeamService, never()).updateTeam(any(String.class), any(UpdateFootballTeamRequest.class));

    }

    @Test
    void givenValidUpdateTeamRequest_whenUpdateTeamForUser_thenReturnForbidden() throws Exception {

        // Given
        String teamId = UUID.randomUUID().toString();
        UpdateFootballTeamRequest updateRequest = UpdateFootballTeamRequest.builder()
                .teamName("Team A Updated")
                .build();

        // When & Then: Using a non-admin token should result in Forbidden (403)
        mockMvc.perform(put("/api/v1/football-teams/{teamId}", teamId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockUserToken.getAccessToken())
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andDo(print())
                .andExpect(status().isForbidden());

        // Verify
        verify(footballTeamService, never()).updateTeam(any(String.class), any(UpdateFootballTeamRequest.class));

    }

    @Test
    void givenExistingTeamId_whenGetTeamByIdForAdmin_thenReturnFootballTeamResponse() throws Exception {

        // Given
        String teamId = UUID.randomUUID().toString();

        FootballTeam team = FootballTeam.builder()
                .id(teamId)
                .teamName("Team A")
                .build();

        FootballTeamResponse response = footballTeamToFootballTeamResponseMapper.map(team);

        // When
        when(footballTeamService.getTeamById(teamId))
                .thenReturn(team);

        // Then
        mockMvc.perform(get("/api/v1/football-teams/{teamId}", teamId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockAdminToken.getAccessToken()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.response.id").value(response.id()))
                .andExpect(jsonPath("$.response.teamName").value(response.teamName()));

        // Verify
        verify(footballTeamService).getTeamById(teamId);

    }

    @Test
    void givenExistingTeamId_whenGetTeamByIdForUser_thenReturnFootballTeamResponse() throws Exception {

        // Given
        String teamId = UUID.randomUUID().toString();

        FootballTeam team = FootballTeam.builder()
                .id(teamId)
                .teamName("Team A")
                .build();

        FootballTeamResponse response = footballTeamToFootballTeamResponseMapper.map(team);

        // When
        when(footballTeamService.getTeamById(teamId))
                .thenReturn(team);

        // Then
        mockMvc.perform(get("/api/v1/football-teams/{teamId}", teamId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockUserToken.getAccessToken()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.response.id").value(response.id()))
                .andExpect(jsonPath("$.response.teamName").value(response.teamName()));

        // Verify
        verify(footballTeamService).getTeamById(teamId);

    }

    @Test
    void givenExistingTeamId_whenDeleteTeam_thenReturnSuccess() throws Exception {

        // Given
        String teamId = UUID.randomUUID().toString();

        // When & Then
        mockMvc.perform(delete("/api/v1/football-teams/{teamId}", teamId)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockAdminToken.getAccessToken()))
                .andExpect(status().isOk())
                .andDo(print())
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.isSuccess").value(true));

        // Verify
        verify(footballTeamService).deleteTeam(teamId);

    }

    @Test
    void givenExistingTeamId_whenDeleteTeamWithoutToken_thenReturnUnauthorized() throws Exception {

        // Given
        String teamId = UUID.randomUUID().toString();

        // When & Then: Omitting the token should result in Unauthorized (401)
        mockMvc.perform(delete("/api/v1/football-teams/{teamId}", teamId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isUnauthorized());

        // Verify: The deleteTeam method should not be called
        verify(footballTeamService, never()).deleteTeam(any());

    }

    @Test
    void givenExistingTeamId_whenDeleteTeamForUser_thenReturnForbidden() throws Exception {

        // Given
        String teamId = UUID.randomUUID().toString();

        // When & Then: Using a non-admin token should result in Forbidden (403)
        mockMvc.perform(delete("/api/v1/football-teams/{teamId}", teamId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockUserToken.getAccessToken()))
                .andDo(print())
                .andExpect(status().isForbidden());

        // Verify
        verify(footballTeamService, never()).deleteTeam(any());

    }

    @Test
    void givenValidPagingRequest_whenGetAllTeamsWithPageableForAdmin_thenReturnCustomPagingResponse() throws Exception {

        // Given
        FootballTeamPagingRequest pagingRequest = FootballTeamPagingRequest.builder()
                .pagination(CustomPaging.builder()
                        .pageNumber(1)
                        .pageSize(10)
                        .build())
                .build();

        // Create a sample FootballTeam domain model
        FootballTeam team = FootballTeam.builder()
                .id(UUID.randomUUID().toString())
                .teamName("Team A")
                .build();
        List<FootballTeam> teams = Collections.singletonList(team);

        // Create a Page instance with effective page index 0
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<FootballTeam> teamPage = new PageImpl<>(teams, pageRequest, teams.size());

        // Build a CustomPage from the list and the Page instance
        CustomPage<FootballTeam> customPage = CustomPage.of(teams, teamPage);

        // Use the mapper to generate the expected response from the custom page
        CustomPagingResponse<FootballTeamResponse> expectedResponse =
                customPageFootballTeamToCustomPagingFootballTeamResponseMapper.toPagingResponse(customPage);

        // When
        when(footballTeamService.getAllTeamsWithPageable(any(FootballTeamPagingRequest.class)))
                .thenReturn(customPage);

        // Then
        mockMvc.perform(post("/api/v1/football-teams/teamsList")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pagingRequest))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockAdminToken.getAccessToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.response.totalElementCount").value(expectedResponse.getTotalElementCount()))
                .andExpect(jsonPath("$.response.totalPageCount").value(expectedResponse.getTotalPageCount()))
                .andExpect(jsonPath("$.response.pageNumber").value(expectedResponse.getPageNumber()))
                .andExpect(jsonPath("$.response.pageSize").value(expectedResponse.getPageSize()));

        // Verify
        verify(footballTeamService, times(1)).getAllTeamsWithPageable(any(FootballTeamPagingRequest.class));

    }

    @Test
    void givenValidPagingRequest_whenGetAllTeamsWithPageableForUser_thenReturnCustomPagingResponse() throws Exception {

        // Given
        FootballTeamPagingRequest pagingRequest = FootballTeamPagingRequest.builder()
                .pagination(CustomPaging.builder()
                        .pageNumber(1)
                        .pageSize(10)
                        .build())
                .build();

        // Create a sample FootballTeam domain model
        FootballTeam team = FootballTeam.builder()
                .id(UUID.randomUUID().toString())
                .teamName("Team A")
                .build();
        List<FootballTeam> teams = Collections.singletonList(team);

        // Create a Page instance with effective page index 0
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<FootballTeam> teamPage = new PageImpl<>(teams, pageRequest, teams.size());

        // Build a CustomPage from the list and the Page instance
        CustomPage<FootballTeam> customPage = CustomPage.of(teams, teamPage);

        // Use the mapper to generate the expected response from the custom page
        CustomPagingResponse<FootballTeamResponse> expectedResponse =
                customPageFootballTeamToCustomPagingFootballTeamResponseMapper.toPagingResponse(customPage);

        // When
        when(footballTeamService.getAllTeamsWithPageable(any(FootballTeamPagingRequest.class)))
                .thenReturn(customPage);

        // Then
        mockMvc.perform(post("/api/v1/football-teams/teamsList")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pagingRequest))
                        .header(HttpHeaders.AUTHORIZATION, "Bearer " + mockUserToken.getAccessToken()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.httpStatus").value("OK"))
                .andExpect(jsonPath("$.isSuccess").value(true))
                .andExpect(jsonPath("$.response.totalElementCount").value(expectedResponse.getTotalElementCount()))
                .andExpect(jsonPath("$.response.totalPageCount").value(expectedResponse.getTotalPageCount()))
                .andExpect(jsonPath("$.response.pageNumber").value(expectedResponse.getPageNumber()))
                .andExpect(jsonPath("$.response.pageSize").value(expectedResponse.getPageSize()));

        // Verify
        verify(footballTeamService, times(1)).getAllTeamsWithPageable(any(FootballTeamPagingRequest.class));

    }



}