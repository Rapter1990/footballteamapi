package com.example.footballteamapi.footballteam.infrastructure.mapper.footballteam;

import com.example.footballteamapi.common.infrastructure.mapper.BaseMapper;
import com.example.footballteamapi.footballteam.application.dto.request.footballteam.CreateFootballTeamRequest;
import com.example.footballteamapi.footballteam.infrastructure.persistence.entity.FootballTeamEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface CreateFootballTeamRequestToFootballTeamEntityMapper extends BaseMapper<CreateFootballTeamRequest, FootballTeamEntity> {

    /**
     * Maps a {@link CreateFootballTeamRequest} to a {@link FootballTeamEntity} for saving purposes.
     *
     * @param request The {@link CreateFootballTeamRequest} object to be mapped.
     * @return A {@link FootballTeamEntity} populated with the data from the given {@link CreateFootballTeamRequest}.
     */
    @Named("mapForSaving")
    default FootballTeamEntity mapForSaving(CreateFootballTeamRequest request) {
        return FootballTeamEntity.builder()
                .teamName(request.teamName())
                .build();
    }

    /**
     * Initializes and returns an instance of {@link CreateFootballTeamRequestToFootballTeamEntityMapper}.
     *
     * @return An initialized {@link CreateFootballTeamRequestToFootballTeamEntityMapper} instance.
     */
    static CreateFootballTeamRequestToFootballTeamEntityMapper initialize() {
        return Mappers.getMapper(CreateFootballTeamRequestToFootballTeamEntityMapper.class);
    }

}
