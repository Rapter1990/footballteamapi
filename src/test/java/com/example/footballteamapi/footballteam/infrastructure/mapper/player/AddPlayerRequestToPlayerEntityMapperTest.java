package com.example.footballteamapi.footballteam.infrastructure.mapper.player;

import com.example.footballteamapi.footballteam.application.dto.request.player.AddPlayerRequest;
import com.example.footballteamapi.footballteam.domain.enums.Position;
import com.example.footballteamapi.footballteam.infrastructure.persistence.entity.PlayerEntity;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class AddPlayerRequestToPlayerEntityMapperTest {

    private final AddPlayerRequestToPlayerEntityMapper mapper = AddPlayerRequestToPlayerEntityMapper.initialize();

    @Test
    void testMapNull() {
        PlayerEntity result = mapper.map((AddPlayerRequest) null);
        assertNull(result, "Mapping null AddPlayerRequest should return null");
    }

    @Test
    void testMapCollectionNull() {
        List<PlayerEntity> result = mapper.map((Collection<AddPlayerRequest>) null);
        assertNull(result, "Mapping a null collection should return null");
    }

    @Test
    void testMapEmptyList() {
        List<PlayerEntity> result = mapper.map(Collections.emptyList());
        assertNotNull(result, "Mapping an empty list should not return null");
        assertTrue(result.isEmpty(), "Mapping an empty list should return an empty list");
    }

    @Test
    void testMapListWithNullElements() {
        AddPlayerRequest validRequest = AddPlayerRequest.builder()
                .name("Player Name")
                .foreignPlayer(true)
                .position(Position.FORWARD)
                .build();
        List<AddPlayerRequest> requests = Arrays.asList(validRequest, null);
        List<PlayerEntity> result = mapper.map(requests);
        assertNotNull(result, "Mapping a list with null elements should not return null");
        assertEquals(2, result.size(), "The resulting list should have the same size as the input");
        assertNotNull(result.get(0), "The first element should be mapped correctly");
        assertNull(result.get(1), "The second element (null input) should be null in the result");
    }

    @Test
    void testMapSingleAddPlayerRequest() {
        AddPlayerRequest request = AddPlayerRequest.builder()
                .name("Player Name")
                .foreignPlayer(true)
                .position(Position.FORWARD)
                .build();
        PlayerEntity result = mapper.map(request);
        assertNotNull(result, "Mapping a valid AddPlayerRequest should not return null");
        assertEquals("Player Name", result.getName(), "Name should be mapped correctly");
        assertTrue(result.isForeignPlayer(), "ForeignPlayer flag should be mapped correctly");
        assertEquals(Position.FORWARD, result.getPosition(), "Position should be mapped correctly");
    }

}