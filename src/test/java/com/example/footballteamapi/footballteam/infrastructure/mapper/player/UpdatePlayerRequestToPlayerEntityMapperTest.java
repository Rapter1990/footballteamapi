package com.example.footballteamapi.footballteam.infrastructure.mapper.player;

import com.example.footballteamapi.footballteam.application.dto.request.player.UpdatePlayerRequest;
import com.example.footballteamapi.footballteam.domain.enums.Position;
import com.example.footballteamapi.footballteam.infrastructure.persistence.entity.PlayerEntity;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UpdatePlayerRequestToPlayerEntityMapperTest {

    private final UpdatePlayerRequestToPlayerEntityMapper mapper = UpdatePlayerRequestToPlayerEntityMapper.initialize();

    @Test
    void testMapNull() {
        PlayerEntity result = mapper.map((UpdatePlayerRequest) null);
        assertNull(result, "Mapping null UpdatePlayerRequest should return null");
    }

    @Test
    void testMapCollectionNull() {
        List<PlayerEntity> result = mapper.map((Collection<UpdatePlayerRequest>) null);
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
        UpdatePlayerRequest validRequest = UpdatePlayerRequest.builder()
                .name("Updated Name")
                .foreignPlayer(false)
                .position(Position.MIDFIELDER)
                .build();
        List<UpdatePlayerRequest> requests = Arrays.asList(validRequest, null);
        List<PlayerEntity> result = mapper.map(requests);
        assertNotNull(result, "Mapping a list with null elements should not return null");
        assertEquals(2, result.size(), "Result list should have the same size as the input");
        assertNotNull(result.get(0), "The first element should be mapped correctly");
        assertNull(result.get(1), "The second element (null input) should be null in the result");
    }

    @Test
    void testMapSingleUpdatePlayerRequest() {
        UpdatePlayerRequest request = UpdatePlayerRequest.builder()
                .name("Updated Name")
                .foreignPlayer(true)
                .position(Position.FORWARD)
                .build();
        PlayerEntity result = mapper.map(request);
        assertNotNull(result, "Mapping a valid UpdatePlayerRequest should not return null");
        assertEquals("Updated Name", result.getName(), "Name should be mapped correctly");
        assertTrue(result.isForeignPlayer(), "Foreign player flag should be mapped correctly");
        assertEquals(Position.FORWARD, result.getPosition(), "Position should be mapped correctly");
    }

    @Test
    void testMapForUpdating() {
        // Given: an existing PlayerEntity with initial values.
        PlayerEntity existingEntity = PlayerEntity.builder()
                .name("Old Name")
                .foreignPlayer(false)
                .position(Position.MIDFIELDER)
                .build();
        // And an update request with new values.
        UpdatePlayerRequest updateRequest = UpdatePlayerRequest.builder()
                .name("New Name")
                .foreignPlayer(true)
                .position(Position.FORWARD)
                .build();

        // When updating the existing entity.
        PlayerEntity updatedEntity = mapper.mapForUpdating(updateRequest, existingEntity);

        // Then: the updated entity should reflect the new values.
        assertNotNull(updatedEntity, "Updated entity should not be null");
        assertEquals("New Name", updatedEntity.getName(), "Name should be updated correctly");
        assertTrue(updatedEntity.isForeignPlayer(), "Foreign player flag should be updated correctly");
        assertEquals(Position.FORWARD, updatedEntity.getPosition(), "Position should be updated correctly");
        // Also ensure that the same instance was updated.
        assertSame(existingEntity, updatedEntity, "Mapping for updating should modify the existing instance");
    }

}