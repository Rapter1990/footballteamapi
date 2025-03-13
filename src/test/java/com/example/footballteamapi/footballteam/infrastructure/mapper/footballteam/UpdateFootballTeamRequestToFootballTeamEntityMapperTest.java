package com.example.footballteamapi.footballteam.infrastructure.mapper.footballteam;

import com.example.footballteamapi.footballteam.application.dto.request.footballteam.UpdateFootballTeamRequest;
import com.example.footballteamapi.footballteam.infrastructure.persistence.entity.FootballTeamEntity;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UpdateFootballTeamRequestToFootballTeamEntityMapperTest {

    private final UpdateFootballTeamRequestToFootballTeamEntityMapper mapper = UpdateFootballTeamRequestToFootballTeamEntityMapper.initialize();

    @Test
    void testMapNull() {
        FootballTeamEntity result = mapper.map((UpdateFootballTeamRequest) null);
        assertNull(result, "Mapping a null UpdateFootballTeamRequest should return null");
    }

    @Test
    void testMapCollectionNull() {
        List<FootballTeamEntity> result = mapper.map((Collection<UpdateFootballTeamRequest>) null);
        assertNull(result, "Mapping a null collection should return null");
    }

    @Test
    void testMapEmptyList() {
        List<FootballTeamEntity> result = mapper.map(Collections.emptyList());
        assertNotNull(result, "Mapping an empty list should not return null");
        assertTrue(result.isEmpty(), "Mapping an empty list should return an empty list");
    }

    @Test
    void testMapListWithNullElements() {
        UpdateFootballTeamRequest validRequest = UpdateFootballTeamRequest.builder()
                .teamName("Team Updated")
                .build();
        List<UpdateFootballTeamRequest> requests = Arrays.asList(validRequest, null);
        List<FootballTeamEntity> result = mapper.map(requests);
        assertNotNull(result, "Mapping a list with null elements should not return null");
        assertEquals(2, result.size(), "The resulting list should have the same size as the input");
        assertNotNull(result.get(0), "The first element should be mapped correctly");
        assertNull(result.get(1), "The second element (null input) should be null in the result");
    }

    @Test
    void testMapSingleUpdateFootballTeamRequest() {
        UpdateFootballTeamRequest request = UpdateFootballTeamRequest.builder()
                .teamName("Team Updated")
                .build();
        FootballTeamEntity entity = mapper.map(request);
        assertNotNull(entity, "Mapping a valid UpdateFootballTeamRequest should not return null");
        assertEquals("Team Updated", entity.getTeamName(), "Team name should be mapped correctly");
    }

    @Test
    void testMapForUpdating() {
        // Given an existing entity with an old team name.
        FootballTeamEntity existingEntity = FootballTeamEntity.builder()
                .teamName("Old Team Name")
                .build();
        UpdateFootballTeamRequest updateRequest = UpdateFootballTeamRequest.builder()
                .teamName("New Team Name")
                .build();

        // When updating the existing entity using mapForUpdating.
        FootballTeamEntity updatedEntity = mapper.mapForUpdating(updateRequest, existingEntity);

        // Then, the updated entity should have the new team name.
        assertNotNull(updatedEntity, "Updated entity should not be null");
        assertEquals("New Team Name", updatedEntity.getTeamName(), "Team name should be updated correctly");
        assertSame(existingEntity, updatedEntity, "Mapping for updating should modify the existing instance");

    }

}