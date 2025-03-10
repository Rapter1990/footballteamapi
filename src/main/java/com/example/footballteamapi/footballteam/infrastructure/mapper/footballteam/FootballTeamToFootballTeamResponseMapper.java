package com.example.footballteamapi.footballteam.infrastructure.mapper.footballteam;

import com.example.footballteamapi.common.infrastructure.mapper.BaseMapper;
import com.example.footballteamapi.footballteam.application.dto.response.FootballTeamResponse;
import com.example.footballteamapi.footballteam.application.dto.response.PlayerResponse;
import com.example.footballteamapi.footballteam.domain.model.FootballTeam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface FootballTeamToFootballTeamResponseMapper extends BaseMapper<FootballTeam, FootballTeamResponse> {

    @Override
    default FootballTeamResponse map(FootballTeam team) {
        List<PlayerResponse> playerResponses = team.getPlayers() == null ? null :
                team.getPlayers().stream()
                        .map(player -> new PlayerResponse(
                                player.getId(),
                                player.getName(),
                                player.isForeignPlayer(),
                                player.getPosition()))
                        .collect(Collectors.toList());
        return new FootballTeamResponse(team.getId(), team.getTeamName(), playerResponses);
    }

    static FootballTeamToFootballTeamResponseMapper initialize() {
        return Mappers.getMapper(FootballTeamToFootballTeamResponseMapper.class);
    }

}
