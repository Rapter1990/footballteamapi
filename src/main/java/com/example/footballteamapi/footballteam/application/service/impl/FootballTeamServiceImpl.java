package com.example.footballteamapi.footballteam.application.service.impl;

import com.example.footballteamapi.common.application.dto.request.CustomPagingRequest;
import com.example.footballteamapi.common.domain.model.CustomPage;
import com.example.footballteamapi.footballteam.application.dto.request.footballteam.CreateFootballTeamRequest;
import com.example.footballteamapi.footballteam.application.dto.request.footballteam.UpdateFootballTeamRequest;
import com.example.footballteamapi.footballteam.application.port.out.FootballTeamRepository;
import com.example.footballteamapi.footballteam.application.service.FootballTeamService;
import com.example.footballteamapi.footballteam.domain.exception.footballteam.FootballTeamAlreadyExistException;
import com.example.footballteamapi.footballteam.domain.exception.footballteam.FootballTeamNotFoundException;
import com.example.footballteamapi.footballteam.domain.model.FootballTeam;
import com.example.footballteamapi.footballteam.infrastructure.mapper.footballteam.CreateFootballTeamRequestToFootballTeamEntityMapper;
import com.example.footballteamapi.footballteam.infrastructure.mapper.footballteam.FootballTeamEntityToFootballTeamMapper;
import com.example.footballteamapi.footballteam.infrastructure.mapper.footballteam.UpdateFootballTeamRequestToFootballTeamEntityMapper;
import com.example.footballteamapi.footballteam.infrastructure.persistence.entity.FootballTeamEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class FootballTeamServiceImpl implements FootballTeamService {

    private final FootballTeamRepository footballTeamRepository;
    private final CreateFootballTeamRequestToFootballTeamEntityMapper createFootballTeamRequestToFootballTeamEntityMapper =
            CreateFootballTeamRequestToFootballTeamEntityMapper.initialize();

    private final UpdateFootballTeamRequestToFootballTeamEntityMapper updateFootballTeamRequestToFootballTeamEntityMapper =
            UpdateFootballTeamRequestToFootballTeamEntityMapper.initialize();

    private final FootballTeamEntityToFootballTeamMapper footballTeamEntityToFootballTeamMapper =
            FootballTeamEntityToFootballTeamMapper.initialize();

    @Override
    @Transactional
    public FootballTeam createTeam(CreateFootballTeamRequest request) {

        this.checkAirportNameUniqueness(request.teamName());

        FootballTeamEntity entity = createFootballTeamRequestToFootballTeamEntityMapper.mapForSaving(request);
        FootballTeamEntity savedEntity = footballTeamRepository.save(entity);
        return footballTeamEntityToFootballTeamMapper.map(savedEntity);
    }

    @Override
    @Transactional
    public FootballTeam updateTeam(String teamId, UpdateFootballTeamRequest request) {
        FootballTeamEntity entity = footballTeamRepository.findById(teamId)
                .orElseThrow(() -> new FootballTeamNotFoundException("Team with id " + teamId + " does not exist"));
        FootballTeamEntity footballTeamEntityToBeUpdated = updateFootballTeamRequestToFootballTeamEntityMapper.mapForUpdating(request, entity);
        FootballTeamEntity updatedEntity = footballTeamRepository.save(footballTeamEntityToBeUpdated);
        return footballTeamEntityToFootballTeamMapper.map(updatedEntity);
    }

    @Override
    @Transactional(readOnly = true)
    public FootballTeam getTeamById(String teamId) {
        FootballTeamEntity entity = footballTeamRepository.findById(teamId)
                .orElseThrow(() -> new FootballTeamNotFoundException("Team with id " + teamId + " does not exist"));
        return footballTeamEntityToFootballTeamMapper.map(entity);
    }

    @Override
    @Transactional
    public void deleteTeam(String teamId) {
        FootballTeamEntity entity = footballTeamRepository.findById(teamId)
                .orElseThrow(() -> new FootballTeamNotFoundException("Team with id " + teamId + " does not exist"));
        footballTeamRepository.delete(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public CustomPage<FootballTeam> getAllTeamsWithPageable(CustomPagingRequest request) {
        Page<FootballTeamEntity> teamEntitiesPage = footballTeamRepository.findAll(request.toPageable());

        if (teamEntitiesPage.getContent().isEmpty()) {
            throw new FootballTeamNotFoundException("Couldn't find any football team");
        }

        List<FootballTeam> domainModels = teamEntitiesPage.getContent().stream()
                .map(footballTeamEntityToFootballTeamMapper::map)
                .collect(Collectors.toList());

        return CustomPage.of(domainModels, teamEntitiesPage);
    }

    public void checkAirportNameUniqueness(final String footballTeamName) {
        if (footballTeamRepository.existsByTeamName(footballTeamName)) {
            throw new FootballTeamAlreadyExistException("With given team name = " + footballTeamName);
        }
    }


}
