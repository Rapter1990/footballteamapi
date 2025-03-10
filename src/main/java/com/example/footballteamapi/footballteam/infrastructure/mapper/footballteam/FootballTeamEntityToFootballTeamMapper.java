package com.example.footballteamapi.footballteam.infrastructure.mapper.footballteam;

import com.example.footballteamapi.common.infrastructure.mapper.BaseMapper;
import com.example.footballteamapi.footballteam.domain.model.FootballTeam;
import com.example.footballteamapi.footballteam.domain.model.Player;
import com.example.footballteamapi.footballteam.infrastructure.persistence.entity.FootballTeamEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.stream.Collectors;

@Mapper
public interface FootballTeamEntityToFootballTeamMapper extends BaseMapper<FootballTeamEntity, FootballTeam> {

    /**
     * Converts a {@link FootballTeamEntity} to a {@link FootballTeam} domain model.
     *
     * @param entity The {@link FootballTeamEntity} to be converted.
     * @return A {@link FootballTeam} populated with data from the given entity.
     */
    @Override
    default FootballTeam map(FootballTeamEntity entity) {
        if (entity == null) {
            return null;
        }
        return FootballTeam.builder()
                .id(entity.getId())
                .teamName(entity.getTeamName())
                .players(entity.getPlayers() == null ? null :
                        entity.getPlayers().stream().map(playerEntity -> Player.builder()
                                .id(playerEntity.getId())
                                .name(playerEntity.getName())
                                .foreignPlayer(playerEntity.isForeignPlayer())
                                .position(playerEntity.getPosition())
                                .build()).collect(Collectors.toList()))
                .build();
    }

    /**
     * Initializes and returns an instance of {@link FootballTeamEntityToFootballTeamMapper}.
     *
     * @return An initialized {@link FootballTeamEntityToFootballTeamMapper} instance.
     */
    static FootballTeamEntityToFootballTeamMapper initialize() {
        return Mappers.getMapper(FootballTeamEntityToFootballTeamMapper.class);
    }

}
