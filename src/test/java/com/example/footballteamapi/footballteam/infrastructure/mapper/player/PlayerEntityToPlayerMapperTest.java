package com.example.footballteamapi.footballteam.infrastructure.mapper.player;

import com.example.footballteamapi.footballteam.domain.enums.Position;
import com.example.footballteamapi.footballteam.domain.model.Player;
import com.example.footballteamapi.footballteam.infrastructure.persistence.entity.PlayerEntity;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PlayerEntityToPlayerMapperTest {

    private final PlayerEntityToPlayerMapper mapper = PlayerEntityToPlayerMapper.initialize();

    @Test
    void testMapNull() {
        Player result = mapper.map((PlayerEntity) null);
        assertNull(result, "Mapping null PlayerEntity should return null");
    }

    @Test
    void testMapCollectionNull() {
        List<Player> result = mapper.map((Collection<PlayerEntity>) null);
        assertNull(result, "Mapping a null collection should return null");
    }

    @Test
    void testMapEmptyList() {
        List<Player> result = mapper.map(Collections.emptyList());
        assertNotNull(result, "Mapping an empty list should not return null");
        assertTrue(result.isEmpty(), "Mapping an empty list should return an empty list");
    }

    @Test
    void testMapListWithNullElements() {
        PlayerEntity validEntity = PlayerEntity.builder()
                .id("p1")
                .name("Player Name")
                .foreignPlayer(true)
                .position(Position.FORWARD)
                .build();
        List<PlayerEntity> sources = Arrays.asList(validEntity, null);
        List<Player> result = mapper.map(sources);
        assertNotNull(result, "Mapping a list with null elements should not return null");
        assertEquals(2, result.size(), "The resulting list should have the same size as the input");
        assertNotNull(result.get(0), "The first element should be mapped correctly");
        assertNull(result.get(1), "The second element (null input) should be null in the result");
    }

    @Test
    void testMapSinglePlayerEntity() {
        PlayerEntity entity = PlayerEntity.builder()
                .id("p1")
                .name("Player Name")
                .foreignPlayer(true)
                .position(Position.FORWARD)
                .build();
        Player result = mapper.map(entity);
        assertNotNull(result, "Mapping a valid PlayerEntity should not return null");
        assertEquals("p1", result.getId(), "ID should be mapped correctly");
        assertEquals("Player Name", result.getName(), "Name should be mapped correctly");
        assertTrue(result.isForeignPlayer(), "Foreign flag should be mapped correctly");
        assertEquals(Position.FORWARD, result.getPosition(), "Position should be mapped correctly");
    }

}