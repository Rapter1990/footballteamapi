package com.example.footballteamapi.footballteam.infrastructure.mapper.footballteam;

import com.example.footballteamapi.footballteam.application.dto.response.FootballTeamResponse;
import com.example.footballteamapi.footballteam.application.dto.response.PlayerResponse;
import com.example.footballteamapi.footballteam.domain.enums.Position;
import com.example.footballteamapi.footballteam.domain.model.FootballTeam;
import com.example.footballteamapi.footballteam.domain.model.Player;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FootballTeamToFootballTeamResponseMapperTest {

    private final FootballTeamToFootballTeamResponseMapper mapper = FootballTeamToFootballTeamResponseMapper.initialize();

    @Test
    void testMapFootballTeamNull() {
        assertThrows(NullPointerException.class, () -> {
            mapper.map((FootballTeam) null);
        }, "Mapping null FootballTeam should throw NullPointerException");
    }

    @Test
    void testMapFootballTeamCollectionNull() {
        List<FootballTeamResponse> result = mapper.map((Collection<FootballTeam>) null);
        assertNull(result, "Mapping a null collection should return null");
    }

    @Test
    void testMapFootballTeamListEmpty() {
        List<FootballTeamResponse> result = mapper.map(Collections.emptyList());
        assertNotNull(result, "Mapping an empty list should not return null");
        assertTrue(result.isEmpty(), "Mapping an empty list should return an empty list");
    }

    @Test
    void testToFootballTeamResponseList_WithNullElements() {
        FootballTeam validTeam = FootballTeam.builder()
                .id("1")
                .teamName("Team A")
                .players(Collections.emptyList())
                .build();
        List<FootballTeam> sources = Arrays.asList(validTeam, null);

        // Expect a NullPointerException when mapping a list that contains a null element.
        assertThrows(NullPointerException.class, () -> {
            mapper.map(sources);
        }, "Mapping a list with a null element should throw NullPointerException");
    }

    @Test
    void testMapSingleFootballTeam() {
        // Create sample players
        Player player1 = Player.builder()
                .id("p1")
                .name("Player One")
                .foreignPlayer(true)
                .position(Position.FORWARD)
                .build();
        Player player2 = Player.builder()
                .id("p2")
                .name("Player Two")
                .foreignPlayer(false)
                .position(Position.MIDFIELDER)
                .build();
        List<Player> players = Arrays.asList(player1, player2);

        FootballTeam team = FootballTeam.builder()
                .id("team1")
                .teamName("Team A")
                .players(players)
                .build();

        FootballTeamResponse response = mapper.map(team);

        assertNotNull(response, "Mapped FootballTeamResponse should not be null");
        assertEquals("team1", response.id(), "Team id should be mapped correctly");
        assertEquals("Team A", response.teamName(), "Team name should be mapped correctly");

        // Assert the players mapping
        List<PlayerResponse> playerResponses = response.players();
        assertNotNull(playerResponses, "Mapped players list should not be null");
        assertEquals(2, playerResponses.size(), "Mapped players list size should match the source");

        PlayerResponse pr1 = playerResponses.get(0);
        assertEquals("p1", pr1.id(), "Player id should be mapped correctly");
        assertEquals("Player One", pr1.name(), "Player name should be mapped correctly");
        assertTrue(pr1.foreignPlayer(), "Foreign flag should be mapped correctly");
        assertEquals(Position.FORWARD, pr1.position(), "Player position should be mapped correctly");

        PlayerResponse pr2 = playerResponses.get(1);
        assertEquals("p2", pr2.id(), "Player id should be mapped correctly");
        assertEquals("Player Two", pr2.name(), "Player name should be mapped correctly");
        assertFalse(pr2.foreignPlayer(), "Foreign flag should be mapped correctly");
        assertEquals(Position.MIDFIELDER, pr2.position(), "Player position should be mapped correctly");

    }

}