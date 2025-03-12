package com.example.footballteamapi.footballteam.application.service.impl;

import com.example.footballteamapi.base.AbstractBaseServiceTest;
import com.example.footballteamapi.common.application.dto.request.CustomPagingRequest;
import com.example.footballteamapi.common.domain.model.CustomPage;
import com.example.footballteamapi.common.domain.model.CustomPaging;
import com.example.footballteamapi.footballteam.application.dto.request.footballteam.CreateFootballTeamRequest;
import com.example.footballteamapi.footballteam.application.dto.request.footballteam.UpdateFootballTeamRequest;
import com.example.footballteamapi.footballteam.application.port.out.FootballTeamRepository;
import com.example.footballteamapi.footballteam.domain.exception.footballteam.FootballTeamAlreadyExistException;
import com.example.footballteamapi.footballteam.domain.exception.footballteam.FootballTeamNotFoundException;
import com.example.footballteamapi.footballteam.domain.model.FootballTeam;
import com.example.footballteamapi.footballteam.infrastructure.mapper.footballteam.FootballTeamEntityToFootballTeamMapper;
import com.example.footballteamapi.footballteam.infrastructure.mapper.footballteam.UpdateFootballTeamRequestToFootballTeamEntityMapper;
import com.example.footballteamapi.footballteam.infrastructure.persistence.entity.FootballTeamEntity;
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
import static org.mockito.Mockito.*;

class FootballTeamServiceImplTest extends AbstractBaseServiceTest {

    @InjectMocks
    private FootballTeamServiceImpl footballTeamService;

    @Mock
    private FootballTeamRepository footballTeamRepository;

    private final UpdateFootballTeamRequestToFootballTeamEntityMapper updateFootballTeamRequestToFootballTeamEntityMapper =
            UpdateFootballTeamRequestToFootballTeamEntityMapper.initialize();

    private final FootballTeamEntityToFootballTeamMapper footballTeamEntityToFootballTeamMapper =
            FootballTeamEntityToFootballTeamMapper.initialize();

    @Test
    void givenUniqueTeamName_whenCreateTeam_thenReturnTeam() {

        // Given
        String teamName = "Unique FC";
        CreateFootballTeamRequest request = CreateFootballTeamRequest.builder()
                .teamName(teamName)
                .build();

        FootballTeamEntity savedEntity = FootballTeamEntity.builder()
                .teamName(request.teamName())
                .build();

        FootballTeam expected = footballTeamEntityToFootballTeamMapper.map(savedEntity);

        // When
        when(footballTeamRepository.existsByTeamName(teamName))
                .thenReturn(false);
        when(footballTeamRepository.save(any(FootballTeamEntity.class)))
                .thenReturn(savedEntity);

        // Then
        FootballTeam team = footballTeamService.createTeam(request);

        assertNotNull(team, "Returned team should not be null");
        assertEquals(expected.getTeamName(),team.getTeamName());

        // Verify
        verify(footballTeamRepository).existsByTeamName(teamName);
        verify(footballTeamRepository).save(any(FootballTeamEntity.class));
        
    }

    @Test
    void givenExistingTeamName_whenCreateTeam_thenThrowsException() {

        // Given
        String teamName = "Existing FC";
        CreateFootballTeamRequest request = CreateFootballTeamRequest.builder()
                .teamName(teamName)
                .build();

        // When
        when(footballTeamRepository.existsByTeamName(teamName))
                .thenReturn(true);

        // Then
        FootballTeamAlreadyExistException exception = assertThrows(
                FootballTeamAlreadyExistException.class,
                () -> footballTeamService.createTeam(request)
        );

        assertTrue(exception.getMessage().contains(teamName));

        // Verify
        verify(footballTeamRepository).existsByTeamName(teamName);
        verify(footballTeamRepository, never()).save(any(FootballTeamEntity.class));

    }

    @Test
    void givenExistingTeam_whenUpdateTeam_thenReturnUpdatedTeam() {

        // Given
        String teamId = UUID.randomUUID().toString();
        String newTeamName = "Updated FC";

        UpdateFootballTeamRequest request = UpdateFootballTeamRequest.builder()
                .teamName(newTeamName)
                .build();

        FootballTeamEntity existingEntity = FootballTeamEntity.builder()
                .id(teamId)
                .teamName("Old FC")
                .build();

        FootballTeamEntity updatedEntity = FootballTeamEntity.builder()
                .id(teamId)
                .teamName(newTeamName)
                .build();

        FootballTeamEntity footballTeamEntityToBeUpdated = updateFootballTeamRequestToFootballTeamEntityMapper.mapForUpdating(request, existingEntity);

        FootballTeam expected = footballTeamEntityToFootballTeamMapper.map(updatedEntity);

        // When
        when(footballTeamRepository.findById(teamId))
                .thenReturn(Optional.of(existingEntity));
        when(footballTeamRepository.save(any(FootballTeamEntity.class)))
                .thenReturn(footballTeamEntityToBeUpdated);

        // Then
        FootballTeam updatedTeam = footballTeamService.updateTeam(teamId, request);

        assertNotNull(updatedTeam, "Updated team should not be null");
        assertEquals(expected.getTeamName(), updatedTeam.getTeamName());

        // Verify
        verify(footballTeamRepository).findById(teamId);
        verify(footballTeamRepository).save(existingEntity);

    }

    @Test
    void givenNonExistentTeam_whenUpdateTeam_thenThrowsException() {

        String teamId = "non-existent-id";
        UpdateFootballTeamRequest request = UpdateFootballTeamRequest.builder()
                .teamName("Any FC")
                .build();

        // When
        when(footballTeamRepository.findById(teamId))
                .thenReturn(Optional.empty());

        // Then
        FootballTeamNotFoundException exception = assertThrows(
                FootballTeamNotFoundException.class,
                () -> footballTeamService.updateTeam(teamId, request)
        );

        assertTrue(exception.getMessage().contains(teamId));

        // Verify
        verify(footballTeamRepository).findById(teamId);
        verify(footballTeamRepository, never()).save(any(FootballTeamEntity.class));

    }

    @Test
    void givenExistingTeam_whenGetTeamById_thenReturnTeam() {

        // Given
        String teamId = UUID.randomUUID().toString();
        FootballTeamEntity entity = FootballTeamEntity.builder()
                .id(teamId)
                .teamName("Team FC")
                .build();

        FootballTeam expected = footballTeamEntityToFootballTeamMapper.map(entity);

        // When
        when(footballTeamRepository.findById(teamId))
                .thenReturn(Optional.of(entity));

        // Then
        FootballTeam team = footballTeamService.getTeamById(teamId);

        assertNotNull(team, "Returned team should not be null");
        assertEquals(expected.getTeamName(),team.getTeamName());

        // Verify
        verify(footballTeamRepository).findById(teamId);

    }

    @Test
    void givenNonExistentTeam_whenGetTeamById_thenThrowsException() {

        // Given
        String teamId = UUID.randomUUID().toString();

        // When
        when(footballTeamRepository.findById(teamId))
                .thenReturn(Optional.empty());

        // Then
        FootballTeamNotFoundException exception = assertThrows(
                FootballTeamNotFoundException.class,
                () -> footballTeamService.getTeamById(teamId)
        );

        assertTrue(exception.getMessage().contains(teamId));

        // Verify
        verify(footballTeamRepository).findById(teamId);

    }

    @Test
    void givenExistingTeam_whenDeleteTeam_thenDeleteTeam() {

        // Given
        String teamId = UUID.randomUUID().toString();
        FootballTeamEntity entity = FootballTeamEntity.builder()
                .id(teamId)
                .teamName("Team FC")
                .build();

        // When
        when(footballTeamRepository.findById(teamId))
                .thenReturn(Optional.of(entity));

        // Then
        footballTeamService.deleteTeam(teamId);

        // Verify
        verify(footballTeamRepository).findById(teamId);
        verify(footballTeamRepository).delete(entity);

    }

    @Test
    void givenNonExistentTeam_whenDeleteTeam_thenThrowsException() {

        // Given
        String teamId = UUID.randomUUID().toString();

        // When
        when(footballTeamRepository.findById(teamId))
                .thenReturn(Optional.empty());

        // Then
        FootballTeamNotFoundException exception = assertThrows(
                FootballTeamNotFoundException.class,
                () -> footballTeamService.deleteTeam(teamId)
        );

        assertTrue(exception.getMessage().contains(teamId));

        // Verify
        verify(footballTeamRepository).findById(teamId);
        verify(footballTeamRepository, never()).delete(any(FootballTeamEntity.class));

    }

    @Test
    void givenNonEmptyTeamPage_whenGetAllTeamsWithPageable_thenReturnPage() {

        // Given
        CustomPaging customPaging = CustomPaging.builder()
                .pageNumber(2)   // Note: the getter subtracts 1, so page "2" becomes page index 1.
                .pageSize(10)
                .build();

        CustomPagingRequest pagingRequest = CustomPagingRequest.builder()
                .pagination(customPaging)
                .build();

        // Given: a list with one team entity and a corresponding Page instance
        FootballTeamEntity entity = FootballTeamEntity.builder()
                .id(UUID.randomUUID().toString())
                .teamName("Team FC")
                .build();

        List<FootballTeamEntity> entities = new ArrayList<>();
        entities.add(entity);
        Pageable pageable = PageRequest.of(customPaging.getPageNumber(), customPaging.getPageSize());
        Page<FootballTeamEntity> page = new PageImpl<>(entities, pageable, entities.size());

        // When
        when(footballTeamRepository.findAll(any(Pageable.class)))
                .thenReturn(page);

        // Then
        CustomPage<FootballTeam> resultPage = footballTeamService.getAllTeamsWithPageable(pagingRequest);

        // Then: the returned page is not null, contains content, and the repository was queried
        assertNotNull(resultPage, "Returned page should not be null");
        assertFalse(resultPage.getContent().isEmpty(), "Content should not be empty");

        // Verify
        verify(footballTeamRepository).findAll(any(Pageable.class));

    }

    @Test
    void givenEmptyTeamPage_whenGetAllTeamsWithPageable_thenThrowsException() {

        // Given
        CustomPaging customPaging = CustomPaging.builder()
                .pageNumber(1)
                .pageSize(10)
                .build();

        CustomPagingRequest pagingRequest = CustomPagingRequest.builder()
                .pagination(customPaging)
                .build();

        List<FootballTeamEntity> emptyList = new ArrayList<>();
        Pageable pageable = PageRequest.of(customPaging.getPageNumber(), customPaging.getPageSize());
        Page<FootballTeamEntity> emptyPage = new PageImpl<>(emptyList, pageable, 0);

        // When
        when(footballTeamRepository.findAll(any(Pageable.class)))
                .thenReturn(emptyPage);

        // Then
        FootballTeamNotFoundException exception = assertThrows(
                FootballTeamNotFoundException.class,
                () -> footballTeamService.getAllTeamsWithPageable(pagingRequest)
        );

        assertTrue(exception.getMessage().toLowerCase().contains("football team"));

        // Verify
        verify(footballTeamRepository).findAll(any(Pageable.class));

    }

}