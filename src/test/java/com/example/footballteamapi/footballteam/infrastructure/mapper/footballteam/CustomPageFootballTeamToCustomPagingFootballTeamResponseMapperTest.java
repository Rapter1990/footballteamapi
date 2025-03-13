package com.example.footballteamapi.footballteam.infrastructure.mapper.footballteam;

import com.example.footballteamapi.common.application.dto.response.CustomPagingResponse;
import com.example.footballteamapi.common.domain.model.CustomPage;
import com.example.footballteamapi.footballteam.application.dto.response.FootballTeamResponse;
import com.example.footballteamapi.footballteam.domain.model.FootballTeam;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomPageFootballTeamToCustomPagingFootballTeamResponseMapperTest {

    private final CustomPageFootballTeamToCustomPagingFootballTeamResponseMapper mapper =
            CustomPageFootballTeamToCustomPagingFootballTeamResponseMapper.initialize();

    // Define the footballTeamToFootballTeamResponseMapper instance used in the mapper
    private final FootballTeamToFootballTeamResponseMapper ftMapper =
            Mappers.getMapper(FootballTeamToFootballTeamResponseMapper.class);

    @Test
    void testToPagingResponse_Null() {
        CustomPagingResponse<FootballTeamResponse> result = mapper.toPagingResponse(null);
        assertNull(result, "Mapping null CustomPage should return null");
    }

    @Test
    void testToPagingResponse_ValidMapping() {
        // Create two sample FootballTeam domain models
        FootballTeam team1 = FootballTeam.builder().id("team1").teamName("Team A").build();
        FootballTeam team2 = FootballTeam.builder().id("team2").teamName("Team B").build();
        List<FootballTeam> teams = Arrays.asList(team1, team2);

        // Use Mockito to create a mock CustomPage<FootballTeam>
        CustomPage<FootballTeam> customPage = mock(CustomPage.class);
        when(customPage.getContent()).thenReturn(teams);
        when(customPage.getTotalElementCount()).thenReturn((long) teams.size());
        when(customPage.getTotalPageCount()).thenReturn(1);
        when(customPage.getPageNumber()).thenReturn(0);
        when(customPage.getPageSize()).thenReturn(10);

        CustomPagingResponse<FootballTeamResponse> response = mapper.toPagingResponse(customPage);

        assertNotNull(response, "Mapping valid CustomPage should not return null");
        assertEquals(teams.size(), response.getContent().size(), "Content size should match the source list size");
        assertEquals(teams.size(), response.getTotalElementCount(), "Total element count should match");
        assertEquals(1, response.getTotalPageCount(), "Total page count should be 1");
        assertEquals(0, response.getPageNumber(), "Page number should match");
        assertEquals(10, response.getPageSize(), "Page size should match");

        // Verify mapping for each FootballTeam using ftMapper
        FootballTeamResponse expectedResp1 = ftMapper.map(team1);
        FootballTeamResponse expectedResp2 = ftMapper.map(team2);

        FootballTeamResponse resp1 = response.getContent().get(0);
        FootballTeamResponse resp2 = response.getContent().get(1);

        assertEquals(expectedResp1.id(), resp1.id(), "Team1 id should be mapped correctly");
        assertEquals(expectedResp1.teamName(), resp1.teamName(), "Team1 name should be mapped correctly");
        assertEquals(expectedResp2.id(), resp2.id(), "Team2 id should be mapped correctly");
        assertEquals(expectedResp2.teamName(), resp2.teamName(), "Team2 name should be mapped correctly");
    }

    @Test
    void testToFootballTeamResponseList_Null() {
        List<FootballTeamResponse> result = mapper.toFootballTeamResponseList(null);
        assertNull(result, "Mapping null list should return null");
    }

    @Test
    void testToFootballTeamResponseList_Empty() {
        List<FootballTeamResponse> result = mapper.toFootballTeamResponseList(Collections.emptyList());
        assertNotNull(result, "Mapping an empty list should not return null");
        assertTrue(result.isEmpty(), "Mapping an empty list should return an empty list");
    }

    @Test
    void testToFootballTeamResponseList_WithNullElements() {
        FootballTeam validTeam = FootballTeam.builder().id("team1").teamName("Team A").build();
        List<FootballTeam> sources = Arrays.asList(validTeam, null);
        // Expect a NullPointerException because the mapper does not handle null elements.
        assertThrows(NullPointerException.class, () -> mapper.toFootballTeamResponseList(sources),
                "Mapping a list with a null element should throw a NullPointerException");
    }

    @Test
    void testToFootballTeamResponseList_SingleMapping() {
        FootballTeam team = FootballTeam.builder().id("team1").teamName("Team A").build();
        List<FootballTeam> teams = Collections.singletonList(team);
        List<FootballTeamResponse> result = mapper.toFootballTeamResponseList(teams);
        assertNotNull(result, "Mapping valid list should not return null");
        assertEquals(1, result.size(), "Result list should have one element");

        // Use ftMapper to generate expected mapping result
        FootballTeamResponse expected = ftMapper.map(team);
        FootballTeamResponse response = result.get(0);
        assertEquals(expected.id(), response.id(), "Team id should be mapped correctly");
        assertEquals(expected.teamName(), response.teamName(), "Team name should be mapped correctly");
    }
}