package com.example.footballteamapi.footballteam.infrastructure.mapper.player;

import com.example.footballteamapi.common.application.dto.response.CustomPagingResponse;
import com.example.footballteamapi.common.domain.model.CustomPage;
import com.example.footballteamapi.footballteam.application.dto.response.PlayerResponse;
import com.example.footballteamapi.footballteam.domain.enums.Position;
import com.example.footballteamapi.footballteam.domain.model.Player;
import org.junit.jupiter.api.Test;
import org.mapstruct.factory.Mappers;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class CustomPagePlayerToCustomPagingPlayerResponseMapperTest {

    private final CustomPagePlayerToCustomPagingPlayerResponseMapper mapper =
            CustomPagePlayerToCustomPagingPlayerResponseMapper.initialize();

    // Define the playerToPlayerResponseMapper instance used in the mapper
    private final PlayerToPlayerResponseMapper pMapper =
            Mappers.getMapper(PlayerToPlayerResponseMapper.class);

    @Test
    void testToPagingResponse_Null() {
        CustomPagingResponse<PlayerResponse> result = mapper.toPagingResponse(null);
        assertNull(result, "Mapping null CustomPage should return null");
    }

    @Test
    void testToPagingResponse_ValidMapping() {
        // Create two sample Player domain models
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

        // Use Mockito to create a mock CustomPage<Player>
        CustomPage<Player> customPage = mock(CustomPage.class);
        when(customPage.getContent()).thenReturn(players);
        when(customPage.getTotalElementCount()).thenReturn((long) players.size());
        when(customPage.getTotalPageCount()).thenReturn(1);
        when(customPage.getPageNumber()).thenReturn(0);
        when(customPage.getPageSize()).thenReturn(10);

        CustomPagingResponse<PlayerResponse> response = mapper.toPagingResponse(customPage);

        assertNotNull(response, "Mapping valid CustomPage should not return null");
        assertEquals(players.size(), response.getContent().size(), "Content size should match the source list size");
        assertEquals((long) players.size(), response.getTotalElementCount(), "Total element count should match");
        assertEquals(1, response.getTotalPageCount(), "Total page count should be 1");
        assertEquals(0, response.getPageNumber(), "Page number should match");
        assertEquals(10, response.getPageSize(), "Page size should match");

        // Generate expected mapping for each Player using pMapper
        PlayerResponse expectedResp1 = pMapper.map(player1);
        PlayerResponse expectedResp2 = pMapper.map(player2);

        PlayerResponse resp1 = response.getContent().get(0);
        PlayerResponse resp2 = response.getContent().get(1);

        assertEquals(expectedResp1.id(), resp1.id(), "Player1 id should be mapped correctly");
        assertEquals(expectedResp1.name(), resp1.name(), "Player1 name should be mapped correctly");
        assertEquals(expectedResp1.foreignPlayer(), resp1.foreignPlayer(), "Player1 foreign flag should be mapped correctly");
        assertEquals(expectedResp1.position(), resp1.position(), "Player1 position should be mapped correctly");

        assertEquals(expectedResp2.id(), resp2.id(), "Player2 id should be mapped correctly");
        assertEquals(expectedResp2.name(), resp2.name(), "Player2 name should be mapped correctly");
        assertEquals(expectedResp2.foreignPlayer(), resp2.foreignPlayer(), "Player2 foreign flag should be mapped correctly");
        assertEquals(expectedResp2.position(), resp2.position(), "Player2 position should be mapped correctly");
    }

    @Test
    void testToPlayerResponseList_Null() {
        List<PlayerResponse> result = mapper.toPlayerResponseList(null);
        assertNull(result, "Mapping null list should return null");
    }

    @Test
    void testToPlayerResponseList_Empty() {
        List<PlayerResponse> result = mapper.toPlayerResponseList(Collections.emptyList());
        assertNotNull(result, "Mapping an empty list should not return null");
        assertTrue(result.isEmpty(), "Mapping an empty list should return an empty list");
    }

    @Test
    void testToPlayerResponseList_WithNullElements() {
        // Create one valid Player and one null element
        Player validPlayer = Player.builder()
                .id("p1")
                .name("Player One")
                .foreignPlayer(true)
                .position(Position.FORWARD)
                .build();
        List<Player> sources = Arrays.asList(validPlayer, null);
        List<PlayerResponse> result = mapper.toPlayerResponseList(sources);
        assertNotNull(result, "Mapping a list with null elements should not return null");
        assertEquals(2, result.size(), "The resulting list should have the same size as the input");
        assertNotNull(result.get(0), "The first element should be mapped correctly");
        assertNull(result.get(1), "The second element (null input) should be null in the result");
    }

    @Test
    void testToPlayerResponseList_SingleMapping() {
        Player player = Player.builder()
                .id("p1")
                .name("Player One")
                .foreignPlayer(true)
                .position(Position.FORWARD)
                .build();
        List<Player> players = Collections.singletonList(player);
        List<PlayerResponse> result = mapper.toPlayerResponseList(players);
        assertNotNull(result, "Mapping valid list should not return null");
        assertEquals(1, result.size(), "Result list should have one element");

        // Use pMapper to generate expected mapping result
        PlayerResponse expected = pMapper.map(player);
        PlayerResponse response = result.get(0);
        assertEquals(expected.id(), response.id(), "Player id should be mapped correctly");
        assertEquals(expected.name(), response.name(), "Player name should be mapped correctly");
        assertEquals(expected.foreignPlayer(), response.foreignPlayer(), "Player foreign flag should be mapped correctly");
        assertEquals(expected.position(), response.position(), "Player position should be mapped correctly");
    }
}