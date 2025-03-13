package com.example.footballteamapi.footballteam.infrastructure.mapper.player;

import com.example.footballteamapi.footballteam.application.dto.response.PlayerResponse;
import com.example.footballteamapi.footballteam.domain.enums.Position;
import com.example.footballteamapi.footballteam.domain.model.Player;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerToPlayerResponseMapperTest {

    private final PlayerToPlayerResponseMapper mapper = PlayerToPlayerResponseMapper.initialize();

    @Test
    void testMapNull() {
        PlayerResponse result = mapper.map((Player) null);
        assertNull(result, "Mapping null Player should return null");
    }

    @Test
    void testMapCollectionNull() {
        List<PlayerResponse> result = mapper.map((Collection<Player>) null);
        assertNull(result, "Mapping a null collection should return null");
    }

    @Test
    void testMapEmptyList() {
        List<PlayerResponse> result = mapper.map(Collections.emptyList());
        assertNotNull(result, "Mapping an empty list should not return null");
        assertTrue(result.isEmpty(), "Mapping an empty list should return an empty list");
    }

    @Test
    void testMapListWithNullElements() {
        // Create a valid Player object
        Player validPlayer = Player.builder()
                .id("p1")
                .name("Player Name")
                .foreignPlayer(true)
                .position(Position.FORWARD)
                .build();
        // Create a list containing a valid Player and a null element
        List<Player> sources = Arrays.asList(validPlayer, null);
        List<PlayerResponse> result = mapper.map(sources);
        assertNotNull(result, "Mapping a list with null elements should not return null");
        assertEquals(2, result.size(), "The resulting list should have the same size as the input");
        assertNotNull(result.get(0), "The first element should be mapped correctly");
        assertNull(result.get(1), "The second element (null input) should be null in the result");
    }

    @Test
    void testMapSinglePlayer() {
        // Create a sample Player object
        Player player = Player.builder()
                .id("p1")
                .name("Player Name")
                .foreignPlayer(true)
                .position(Position.FORWARD)
                .build();
        PlayerResponse result = mapper.map(player);
        assertNotNull(result, "Mapping a valid Player should not return null");
        assertEquals("p1", result.id(), "ID should be mapped correctly");
        assertEquals("Player Name", result.name(), "Name should be mapped correctly");
        assertTrue(result.foreignPlayer(), "Foreign flag should be mapped correctly");
        assertEquals(Position.FORWARD, result.position(), "Position should be mapped correctly");

    }

}