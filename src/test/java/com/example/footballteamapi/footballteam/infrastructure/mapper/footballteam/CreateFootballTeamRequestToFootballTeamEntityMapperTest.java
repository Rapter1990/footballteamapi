package com.example.footballteamapi.footballteam.infrastructure.mapper.footballteam;

import com.example.footballteamapi.footballteam.application.dto.request.footballteam.CreateFootballTeamRequest;
import com.example.footballteamapi.footballteam.infrastructure.persistence.entity.FootballTeamEntity;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CreateFootballTeamRequestToFootballTeamEntityMapperTest {

    private final CreateFootballTeamRequestToFootballTeamEntityMapper mapper = CreateFootballTeamRequestToFootballTeamEntityMapper.initialize();

    @Test
    void testMapCreateFootballTeamRequestNull() {
        FootballTeamEntity result = mapper.map((CreateFootballTeamRequest) null);
        assertNull(result, "Mapping null CreateFootballTeamRequest should return null");
    }

    @Test
    void testMapCreateFootballTeamRequestCollectionNull() {
        List<FootballTeamEntity> result = mapper.map((Collection<CreateFootballTeamRequest>) null);
        assertNull(result, "Mapping a null collection should return null");
    }

    @Test
    void testMapCreateFootballTeamRequestListEmpty() {
        List<FootballTeamEntity> result = mapper.map(Collections.emptyList());
        assertNotNull(result, "Mapping an empty list should not return null");
        assertTrue(result.isEmpty(), "Mapping an empty list should return an empty list");
    }

    @Test
    void testMapCreateFootballTeamRequestListWithNullElements() {
        List<CreateFootballTeamRequest> requests = Arrays.asList(
                CreateFootballTeamRequest.builder()
                        .teamName("Team A")
                        .build(),
                null
        );

        List<FootballTeamEntity> result = mapper.map(requests);

        assertNotNull(result, "Mapping a list with null elements should not return null");
        assertEquals(2, result.size(), "The resulting list should have the same size as the input list");
        assertNotNull(result.get(0), "The first element should be mapped correctly");
        assertNull(result.get(1), "The second element (null input) should be null in the result");
    }

    @Test
    void testMapSingleCreateFootballTeamRequest() {
        CreateFootballTeamRequest request = CreateFootballTeamRequest.builder()
                .teamName("Team A")
                .build();

        FootballTeamEntity result = mapper.map(request);

        assertNotNull(result, "Mapping a valid CreateFootballTeamRequest should not return null");
        assertEquals(request.teamName(), result.getTeamName(), "Team name should be mapped correctly");
    }

}