package com.example.footballteamapi.footballteam.infrastructure.mapper.footballteam;

import com.example.footballteamapi.footballteam.domain.enums.Position;
import com.example.footballteamapi.footballteam.domain.model.FootballTeam;
import com.example.footballteamapi.footballteam.domain.model.Player;
import com.example.footballteamapi.footballteam.infrastructure.persistence.entity.FootballTeamEntity;
import com.example.footballteamapi.footballteam.infrastructure.persistence.entity.PlayerEntity;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FootballTeamEntityToFootballTeamMapperTest {

    private final FootballTeamEntityToFootballTeamMapper mapper = FootballTeamEntityToFootballTeamMapper.initialize();

    @Test
    void testMapFootballTeamEntityNull() {
        FootballTeam result = mapper.map((FootballTeamEntity) null);
        assertNull(result, "Mapping null FootballTeamEntity should return null");
    }

    @Test
    void testMapFootballTeamEntityCollectionNull() {
        List<FootballTeam> result = mapper.map((Collection<FootballTeamEntity>) null);
        assertNull(result, "Mapping a null collection should return null");
    }

    @Test
    void testMapFootballTeamEntityListEmpty() {
        List<FootballTeam> result = mapper.map(Collections.emptyList());
        assertNotNull(result, "Mapping an empty list should not return null");
        assertTrue(result.isEmpty(), "Mapping an empty list should return an empty list");
    }

    @Test
    void testMapFootballTeamEntityListWithNullElements() {
        FootballTeamEntity validEntity = FootballTeamEntity.builder()
                .id("1")
                .teamName("Team A")
                .players(Collections.emptyList())
                .build();
        List<FootballTeamEntity> sources = Arrays.asList(validEntity, null);
        List<FootballTeam> result = mapper.map(sources);
        assertNotNull(result, "Mapping a list with null elements should not return null");
        assertEquals(2, result.size(), "The resulting list should have the same size as the input");
        assertNotNull(result.get(0), "The first element should be mapped correctly");
        assertNull(result.get(1), "The second element (null input) should be null in the result");
    }

    @Test
    void testMapSingleFootballTeamEntity() {
        // Create sample player entities
        PlayerEntity player1 = PlayerEntity.builder()
                .id("p1")
                .name("Player One")
                .foreignPlayer(true)
                .position(Position.FORWARD)
                .build();
        PlayerEntity player2 = PlayerEntity.builder()
                .id("p2")
                .name("Player Two")
                .foreignPlayer(false)
                .position(Position.MIDFIELDER)
                .build();

        List<PlayerEntity> players = Arrays.asList(player1, player2);

        FootballTeamEntity entity = FootballTeamEntity.builder()
                .id("team1")
                .teamName("Team A")
                .players(players)
                .build();

        FootballTeam result = mapper.map(entity);
        assertNotNull(result, "Mapped FootballTeam should not be null");
        assertEquals("team1", result.getId(), "Team id should be mapped correctly");
        assertEquals("Team A", result.getTeamName(), "Team name should be mapped correctly");

        // Assert the players mapping
        List<Player> mappedPlayers = result.getPlayers();
        assertNotNull(mappedPlayers, "Mapped players list should not be null");
        assertEquals(2, mappedPlayers.size(), "Mapped players list size should match the source");

        Player mappedPlayer1 = mappedPlayers.get(0);
        assertEquals("p1", mappedPlayer1.getId(), "Player id should be mapped correctly");
        assertEquals("Player One", mappedPlayer1.getName(), "Player name should be mapped correctly");
        assertTrue(mappedPlayer1.isForeignPlayer(), "Foreign flag should be mapped correctly");
        assertEquals(Position.FORWARD, mappedPlayer1.getPosition(), "Player position should be mapped correctly");

        Player mappedPlayer2 = mappedPlayers.get(1);
        assertEquals("p2", mappedPlayer2.getId(), "Player id should be mapped correctly");
        assertEquals("Player Two", mappedPlayer2.getName(), "Player name should be mapped correctly");
        assertFalse(mappedPlayer2.isForeignPlayer(), "Foreign flag should be mapped correctly");
        assertEquals(Position.MIDFIELDER, mappedPlayer2.getPosition(), "Player position should be mapped correctly");

    }

}