package com.example.footballteamapi.footballteam.infrastructure.mapper.footballteam;

import com.example.footballteamapi.common.infrastructure.mapper.BaseMapper;
import com.example.footballteamapi.footballteam.application.dto.request.footballteam.UpdateFootballTeamRequest;
import com.example.footballteamapi.footballteam.infrastructure.persistence.entity.FootballTeamEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UpdateFootballTeamRequestToFootballTeamEntityMapper extends BaseMapper<UpdateFootballTeamRequest, FootballTeamEntity> {

    /**
     * Updates the given {@link FootballTeamEntity} using data from the {@link UpdateFootballTeamRequest}.
     *
     * @param request The {@link UpdateFootballTeamRequest} object containing updated data.
     * @param entity  The {@link FootballTeamEntity} to be updated.
     * @return The updated {@link FootballTeamEntity}.
     */
    @Named("mapForUpdating")
    default FootballTeamEntity mapForUpdating(UpdateFootballTeamRequest request, @MappingTarget FootballTeamEntity entity) {
        entity.setTeamName(request.teamName());
        return entity;
    }

    static UpdateFootballTeamRequestToFootballTeamEntityMapper initialize() {
        return Mappers.getMapper(UpdateFootballTeamRequestToFootballTeamEntityMapper.class);
    }

}
