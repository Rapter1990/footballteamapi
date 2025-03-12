package com.example.footballteamapi.footballteam.application.service.impl;

import com.example.footballteamapi.base.AbstractBaseServiceTest;
import com.example.footballteamapi.common.application.dto.request.CustomPagingRequest;
import com.example.footballteamapi.common.domain.model.CustomPage;
import com.example.footballteamapi.common.domain.model.CustomPaging;
import com.example.footballteamapi.footballteam.application.dto.request.player.AddPlayerRequest;
import com.example.footballteamapi.footballteam.application.dto.request.player.UpdatePlayerRequest;
import com.example.footballteamapi.footballteam.application.port.out.FootballTeamRepository;
import com.example.footballteamapi.footballteam.application.port.out.PlayerRepository;
import com.example.footballteamapi.footballteam.domain.enums.Position;
import com.example.footballteamapi.footballteam.domain.exception.footballteam.FootballTeamNotFoundException;
import com.example.footballteamapi.footballteam.domain.exception.player.MaxPlayersExceededException;
import com.example.footballteamapi.footballteam.domain.exception.player.PlayerNotFoundException;
import com.example.footballteamapi.footballteam.domain.exception.player.PlayerTeamMismatchException;
import com.example.footballteamapi.footballteam.domain.model.Player;
import com.example.footballteamapi.footballteam.infrastructure.mapper.player.AddPlayerRequestToPlayerEntityMapper;
import com.example.footballteamapi.footballteam.infrastructure.mapper.player.PlayerEntityToPlayerMapper;
import com.example.footballteamapi.footballteam.infrastructure.mapper.player.UpdatePlayerRequestToPlayerEntityMapper;
import com.example.footballteamapi.footballteam.infrastructure.persistence.entity.FootballTeamEntity;
import com.example.footballteamapi.footballteam.infrastructure.persistence.entity.PlayerEntity;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class PlayerServiceImplTest extends AbstractBaseServiceTest {

    @InjectMocks
    private PlayerServiceImpl playerService;

    @Mock
    private FootballTeamRepository footballTeamRepository;

    @Mock
    private PlayerRepository playerRepository;

    private final AddPlayerRequestToPlayerEntityMapper addPlayerRequestToPlayerEntityMapper =
            AddPlayerRequestToPlayerEntityMapper.initialize();

    private final PlayerEntityToPlayerMapper playerEntityToPlayerMapper =
            PlayerEntityToPlayerMapper.initialize();

    private final UpdatePlayerRequestToPlayerEntityMapper updatePlayerRequestToPlayerEntityMapper =
            UpdatePlayerRequestToPlayerEntityMapper.initialize();

    @Test
    void givenValidAddPlayerRequest_whenAddPlayerToTeam_thenReturnPlayer() {

        // Given
        String teamId = UUID.randomUUID().toString();
        String playerName = "Player Name";
        AddPlayerRequest request = AddPlayerRequest.builder()
                .name(playerName)
                .foreignPlayer(false)
                .position(Position.MIDFIELDER)
                .build();

        FootballTeamEntity teamEntity = FootballTeamEntity.builder()
                .id(teamId)
                .players(new ArrayList<>())
                .build();

        PlayerEntity savedPlayerEntity = PlayerEntity.builder()
                .id(UUID.randomUUID().toString())
                .name(playerName)
                .foreignPlayer(false)
                .position(Position.MIDFIELDER)
                .footballTeam(teamEntity)
                .build();

        Player expected = playerEntityToPlayerMapper.map(savedPlayerEntity);

        // When
        when(footballTeamRepository.findById(teamId))
                .thenReturn(Optional.of(teamEntity));
        when(playerRepository.save(any(PlayerEntity.class)))
                .thenReturn(savedPlayerEntity);
        when(footballTeamRepository.save(teamEntity))
                .thenReturn(teamEntity);

        // Then
        Player result = playerService.addPlayerToTeam(teamId, request);

        assertNotNull(result, "Returned player should not be null");
        assertEquals(expected.getName(), result.getName());
        assertEquals(playerName, result.getName(), "Player name should match");
        assertFalse(result.isForeignPlayer(), "Player should not be foreign");
        assertEquals(Position.MIDFIELDER, result.getPosition(), "Player position should be MIDFIELDER");
        assertTrue(teamEntity.getPlayers().contains(savedPlayerEntity), "Team should contain the saved player");
        assertEquals(1, teamEntity.getPlayers().size(), "Team should have exactly 1 player");

        // Verify
        verify(footballTeamRepository).findById(teamId);
        verify(playerRepository).save(any(PlayerEntity.class));
        verify(footballTeamRepository).save(teamEntity);

    }

    @Test
    void givenNonExistentTeam_whenAddPlayerToTeam_thenThrowsTeamNotFoundException() {

        // Given
        String teamId = UUID.randomUUID().toString();
        AddPlayerRequest request = AddPlayerRequest.builder()
                .name("Player Name")
                .foreignPlayer(false)
                .position(Position.MIDFIELDER)
                .build();

        // When
        when(footballTeamRepository.findById(teamId))
                .thenReturn(Optional.empty());

        // Then
        FootballTeamNotFoundException exception = assertThrows(
                FootballTeamNotFoundException.class,
                () -> playerService.addPlayerToTeam(teamId, request)
        );

        assertTrue(exception.getMessage().contains(teamId));

        // Verify
        verify(footballTeamRepository).findById(teamId);

    }

    @Test
    void givenTeamWithMaxPlayers_whenAddPlayerToTeam_thenThrowsMaxPlayersExceededException() {

        // Given
        String teamId = UUID.randomUUID().toString();
        AddPlayerRequest request = AddPlayerRequest.builder()
                .name("Player Name")
                .foreignPlayer(false)
                .position(Position.MIDFIELDER)
                .build();

        // Create a team with exactly 18 players
        List<PlayerEntity> players = new ArrayList<>();
        for (int i = 0; i < 18; i++) {
            players.add(PlayerEntity.builder().id("p" + i).build());
        }

        FootballTeamEntity teamEntity = FootballTeamEntity.builder()
                .id(teamId)
                .players(players)
                .build();

        // When
        when(footballTeamRepository.findById(teamId))
                .thenReturn(Optional.of(teamEntity));

        //Then
        assertThrows(MaxPlayersExceededException.class,
                () -> playerService.addPlayerToTeam(teamId, request));
        assertEquals(18, teamEntity.getPlayers().size(), "Team should still have 18 players");


        // Verify
        verify(footballTeamRepository).findById(teamId);

    }

    @Test
    void givenForeignPlayerAndMaxForeignPlayers_whenAddPlayerToTeam_thenThrowsIllegalStateException() {

        // Given
        String teamId = UUID.randomUUID().toString();
        AddPlayerRequest request = AddPlayerRequest.builder()
                .name("Foreign Player")
                .foreignPlayer(true)
                .position(Position.FORWARD)
                .build();

        // Create a team with 6 foreign players already
        List<PlayerEntity> players = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            players.add(PlayerEntity.builder()
                    .id("fp" + i)
                    .foreignPlayer(true)
                    .position(Position.FORWARD)
                    .build());
        }

        FootballTeamEntity teamEntity = FootballTeamEntity.builder()
                .id(teamId)
                .players(players)
                .build();

        // When
        when(footballTeamRepository.findById(teamId))
                .thenReturn(Optional.of(teamEntity));

        // Then
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> playerService.addPlayerToTeam(teamId, request)
        );

        assertTrue(exception.getMessage().contains("A team can have at most 6 foreign players"));

        // Verify
        verify(footballTeamRepository).findById(teamId);

    }

    @Test
    void givenGoalkeeperAndMaxGoalkeepers_whenAddPlayerToTeam_thenThrowsIllegalStateException() {

        // Given
        String teamId = UUID.randomUUID().toString();
        AddPlayerRequest request = AddPlayerRequest.builder()
                .name("Goalkeeper")
                .foreignPlayer(false)
                .position(Position.GOALKEEPER)
                .build();

        // Create a team with 2 goalkeepers already
        List<PlayerEntity> players = new ArrayList<>();

        players.add(PlayerEntity.builder()
                .id("gk1")
                .foreignPlayer(false)
                .position(Position.GOALKEEPER)
                .build());

        players.add(PlayerEntity.builder()
                .id("gk2")
                .foreignPlayer(false)
                .position(Position.GOALKEEPER)
                .build());

        FootballTeamEntity teamEntity = FootballTeamEntity.builder()
                .id(teamId)
                .players(players)
                .build();

        // When
        when(footballTeamRepository.findById(teamId))
                .thenReturn(Optional.of(teamEntity));

        // Then
        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> playerService.addPlayerToTeam(teamId, request)
        );

        assertTrue(exception.getMessage().contains("A team can have at most 2 goalkeepers"));

        // Verify
        verify(footballTeamRepository).findById(teamId);

    }

    @Test
    void givenValidTeamAndPlayer_whenDeletePlayer_thenDeletePlayer() {

        // Given
        String teamId = UUID.randomUUID().toString();
        String playerId = UUID.randomUUID().toString();

        // Create team entity with a player in its list
        List<PlayerEntity> players = new ArrayList<>();
        FootballTeamEntity teamEntity = FootballTeamEntity.builder()
                .id(teamId)
                .players(players)
                .build();

        PlayerEntity playerEntity = PlayerEntity.builder()
                .id(playerId)
                .footballTeam(teamEntity)
                .build();
        players.add(playerEntity);

        // When
        when(footballTeamRepository.findById(teamId))
                .thenReturn(Optional.of(teamEntity));
        when(playerRepository.findById(playerId))
                .thenReturn(Optional.of(playerEntity));

        // Then
        playerService.deletePlayer(teamId, playerId);

        assertFalse(teamEntity.getPlayers().contains(playerEntity), "Player should be removed from team");
        assertEquals(0, teamEntity.getPlayers().size(), "Team should have 0 players after deletion");

        // Verify
        verify(footballTeamRepository).findById(teamId);
        verify(playerRepository).findById(playerId);
        verify(playerRepository).delete(playerEntity);

    }

    @Test
    void givenNonExistentTeam_whenDeletePlayer_thenThrowsTeamNotFoundException() {

        // Given
        String teamId = UUID.randomUUID().toString();
        String playerId = UUID.randomUUID().toString();

        // When
        when(footballTeamRepository.findById(teamId))
                .thenReturn(Optional.empty());

        // Then
        FootballTeamNotFoundException exception = assertThrows(
                FootballTeamNotFoundException.class,
                () -> playerService.deletePlayer(teamId, playerId)
        );

        assertTrue(exception.getMessage().contains(teamId));

        // Verify
        verify(footballTeamRepository).findById(teamId);

    }

    @Test
    void givenNonExistentPlayer_whenDeletePlayer_thenThrowsPlayerNotFoundException() {

        // Given
        String teamId = UUID.randomUUID().toString();
        String playerId = UUID.randomUUID().toString();

        FootballTeamEntity teamEntity = FootballTeamEntity.builder()
                .id(teamId)
                .players(new ArrayList<>())
                .build();

        // When
        when(footballTeamRepository.findById(teamId))
                .thenReturn(Optional.of(teamEntity));
        when(playerRepository.findById(playerId))
                .thenReturn(Optional.empty());

        // Then
        PlayerNotFoundException exception = assertThrows(
                PlayerNotFoundException.class,
                () -> playerService.deletePlayer(teamId, playerId)
        );

        assertTrue(exception.getMessage().contains(playerId));
        assertTrue(teamEntity.getPlayers().isEmpty(), "Team players list should remain empty");

        // Verify
        verify(footballTeamRepository).findById(teamId);
        verify(playerRepository).findById(playerId);

    }

    @Test
    void givenPlayerNotInTeam_whenDeletePlayer_thenThrowsPlayerTeamMismatchException() {

        // Given
        String teamId = UUID.randomUUID().toString();
        String playerId = UUID.randomUUID().toString();

        // Create team entity with id team1
        FootballTeamEntity teamEntity = FootballTeamEntity.builder()
                .id(teamId)
                .players(new ArrayList<>())
                .build();

        // Create a player that belongs to a different team
        FootballTeamEntity otherTeam = FootballTeamEntity.builder().id("team2").build();
        PlayerEntity playerEntity = PlayerEntity.builder()
                .id(playerId)
                .footballTeam(otherTeam)
                .build();

        // When
        when(footballTeamRepository.findById(teamId))
                .thenReturn(Optional.of(teamEntity));
        when(playerRepository.findById(playerId))
                .thenReturn(Optional.of(playerEntity));

        // Then
        PlayerTeamMismatchException exception = assertThrows(
                PlayerTeamMismatchException.class,
                () -> playerService.deletePlayer(teamId, playerId)
        );

        assertTrue(exception.getMessage().contains("Mismatch"));

        // Verify
        verify(footballTeamRepository).findById(teamId);
        verify(playerRepository).findById(playerId);

    }

    @Test
    void givenValidTeamAndPlayer_whenUpdatePlayer_thenReturnUpdatedPlayer() {

        // Given
        String teamId = UUID.randomUUID().toString();
        String playerId = UUID.randomUUID().toString();
        String updatedName = "Player Name Updated";

        FootballTeamEntity teamEntity = FootballTeamEntity.builder()
                .id(teamId)
                .players(new ArrayList<>())
                .build();

        PlayerEntity playerEntity = PlayerEntity.builder()
                .id(playerId)
                .name("Player Name")
                .footballTeam(teamEntity)
                .build();

        UpdatePlayerRequest updateRequest = UpdatePlayerRequest.builder()
                .name(updatedName)
                .foreignPlayer(playerEntity.isForeignPlayer())
                .position(playerEntity.getPosition() == null ? Position.MIDFIELDER : playerEntity.getPosition())
                .build();

        // Simulate the update mapping (which mutates the player entity)
        updatePlayerRequestToPlayerEntityMapper.mapForUpdating(updateRequest, playerEntity);
        playerEntity.setName(updatedName);

        Player expected = playerEntityToPlayerMapper.map(playerEntity);

        // When
        when(footballTeamRepository.findById(teamId))
                .thenReturn(Optional.of(teamEntity));
        when(playerRepository.findById(playerId))
                .thenReturn(Optional.of(playerEntity));
        when(playerRepository.save(playerEntity))
                .thenReturn(playerEntity);

        // Then
        Player result = playerService.updatePlayer(teamId, playerId, updateRequest);

        assertNotNull(result, "Updated player should not be null");
        assertEquals(updatedName, result.getName(), "Player name should be updated");
        assertEquals(playerId, result.getId(), "Player ID should remain unchanged");
        assertFalse(result.isForeignPlayer(), "Foreign player flag should remain false");
        assertEquals(Position.MIDFIELDER, result.getPosition(), "Player position should remain MIDFIELDER");

        // Verify
        verify(footballTeamRepository).findById(teamId);
        verify(playerRepository).findById(playerId);
        verify(playerRepository).save(playerEntity);

    }

    @Test
    void givenNonExistentTeam_whenUpdatePlayer_thenThrowsTeamNotFoundException() {

        // Given
        String teamId = UUID.randomUUID().toString();
        String playerId = UUID.randomUUID().toString();
        UpdatePlayerRequest updateRequest = UpdatePlayerRequest.builder()
                .name("Player Name Updated")
                .foreignPlayer(false)
                .position(Position.MIDFIELDER)
                .build();

        // When
        when(footballTeamRepository.findById(teamId))
                .thenReturn(Optional.empty());

        // Then
        FootballTeamNotFoundException exception = assertThrows(
                FootballTeamNotFoundException.class,
                () -> playerService.updatePlayer(teamId, playerId, updateRequest)
        );

        assertTrue(exception.getMessage().contains(teamId));

        // Verify
        verify(footballTeamRepository).findById(teamId);

    }

    @Test
    void givenNonExistentPlayer_whenUpdatePlayer_thenThrowsPlayerNotFoundException() {

        // Given
        String teamId = UUID.randomUUID().toString();
        String playerId = UUID.randomUUID().toString();

        FootballTeamEntity teamEntity = FootballTeamEntity.builder()
                .id(teamId)
                .players(new ArrayList<>())
                .build();

        UpdatePlayerRequest updateRequest = UpdatePlayerRequest.builder()
                .name("Player Name Updated")
                .foreignPlayer(false)
                .position(Position.MIDFIELDER)
                .build();

        // When
        when(footballTeamRepository.findById(teamId))
                .thenReturn(Optional.of(teamEntity));
        when(playerRepository.findById(playerId))
                .thenReturn(Optional.empty());

        // Then
        PlayerNotFoundException exception = assertThrows(
                PlayerNotFoundException.class,
                () -> playerService.updatePlayer(teamId, playerId, updateRequest)
        );

        assertTrue(exception.getMessage().contains(playerId));

        // Verify
        verify(footballTeamRepository).findById(teamId);
        verify(playerRepository).findById(playerId);

    }


    @Test
    void givenPlayerNotInTeam_whenUpdatePlayer_thenThrowsPlayerTeamMismatchException() {
        // Given
        String teamId = UUID.randomUUID().toString();
        String playerId = UUID.randomUUID().toString();

        FootballTeamEntity teamEntity = FootballTeamEntity.builder()
                .id(teamId)
                .players(new ArrayList<>())
                .build();

        FootballTeamEntity otherTeam = FootballTeamEntity.builder()
                .id("team2")
                .build();

        PlayerEntity playerEntity = PlayerEntity.builder()
                .id(playerId)
                .footballTeam(otherTeam)
                .build();

        UpdatePlayerRequest updateRequest = UpdatePlayerRequest.builder()
                .name("Player Name Updated")
                .foreignPlayer(false)
                .position(Position.MIDFIELDER)
                .build();

        // When
        when(footballTeamRepository.findById(teamId))
                .thenReturn(Optional.of(teamEntity));
        when(playerRepository.findById(playerId))
                .thenReturn(Optional.of(playerEntity));

        // Then
        PlayerTeamMismatchException exception = assertThrows(
                PlayerTeamMismatchException.class,
                () -> playerService.updatePlayer(teamId, playerId, updateRequest)
        );

        assertTrue(exception.getMessage().contains("Mismatch"));

        // Verify
        verify(footballTeamRepository).findById(teamId);
        verify(playerRepository).findById(playerId);

    }

    @Test
    void givenValidTeamAndNonEmptyPlayerPage_whenGetPlayersByTeamId_thenReturnCustomPage() {

        // Given
        String teamId = UUID.randomUUID().toString();
        FootballTeamEntity teamEntity = FootballTeamEntity.builder()
                .id(teamId)
                .build();

        // Create a sample list of player entities
        List<PlayerEntity> players = new ArrayList<>();
        PlayerEntity playerEntity = PlayerEntity.builder()
                .id(UUID.randomUUID().toString())
                .name("Player Name")
                .build();
        players.add(playerEntity);

        // Build the paging request; note that pageNumber=1 becomes page index 0
        CustomPaging customPaging = CustomPaging.builder()
                .pageNumber(1)
                .pageSize(10)
                .build();
        CustomPagingRequest pagingRequest = CustomPagingRequest.builder()
                .pagination(customPaging)
                .build();

        Pageable pageable = pagingRequest.toPageable();
        Page<PlayerEntity> page = new PageImpl<>(players, pageable, players.size());

        // When
        when(footballTeamRepository.findById(teamId))
                .thenReturn(Optional.of(teamEntity));
        when(playerRepository.findByFootballTeam_Id(teamId, pageable))
                .thenReturn(page);

        // Then
        CustomPage<Player> resultPage = playerService.getPlayersByTeamId(teamId, pagingRequest);

        assertNotNull(resultPage, "Returned page should not be null");
        assertFalse(resultPage.getContent().isEmpty(), "Content should not be empty");
        assertEquals(1, resultPage.getContent().size(), "Content size should be 1");
        assertEquals("Player Name", resultPage.getContent().get(0).getName(), "Player name should match");

        // Verify
        verify(footballTeamRepository).findById(teamId);
        verify(playerRepository).findByFootballTeam_Id(eq(teamId), eq(pageable));
    }


    @Test
    void givenNonExistentTeam_whenGetPlayersByTeamId_thenThrowsTeamNotFoundException() {

        // Given
        String teamId = UUID.randomUUID().toString();
        CustomPaging customPaging = CustomPaging.builder()
                .pageNumber(1)
                .pageSize(10)
                .build();
        CustomPagingRequest pagingRequest = CustomPagingRequest.builder()
                .pagination(customPaging)
                .build();

        // When
        when(footballTeamRepository.findById(teamId))
                .thenReturn(Optional.empty());

        // Then
        FootballTeamNotFoundException exception = assertThrows(
                FootballTeamNotFoundException.class,
                () -> playerService.getPlayersByTeamId(teamId, pagingRequest)
        );

        assertTrue(exception.getMessage().contains(teamId));

        // Verify
        verify(footballTeamRepository).findById(teamId);

    }

    @Test
    void givenEmptyPlayerPage_whenGetPlayersByTeamId_thenThrowsPlayerNotFoundException() {

        // Given
        String teamId = UUID.randomUUID().toString();
        FootballTeamEntity teamEntity = FootballTeamEntity.builder()
                .id(teamId)
                .build();

        Pageable pageable = PageRequest.of(0, 10);
        Page<PlayerEntity> emptyPage = new PageImpl<>(new ArrayList<>(), pageable, 0);

        CustomPaging customPaging = CustomPaging.builder()
                .pageNumber(1)
                .pageSize(10)
                .build();
        CustomPagingRequest pagingRequest = CustomPagingRequest.builder()
                .pagination(customPaging)
                .build();

        // When
        when(footballTeamRepository.findById(teamId))
                .thenReturn(Optional.of(teamEntity));
        when(playerRepository.findByFootballTeam_Id(teamId, pageable))
                .thenReturn(emptyPage);

        // Then
        PlayerNotFoundException exception = assertThrows(
                PlayerNotFoundException.class,
                () -> playerService.getPlayersByTeamId(teamId, pagingRequest)
        );

        assertTrue(exception.getMessage().contains(teamId));

        // Verify
        verify(footballTeamRepository).findById(teamId);
        verify(playerRepository).findByFootballTeam_Id(eq(teamId), any(Pageable.class));

    }

}