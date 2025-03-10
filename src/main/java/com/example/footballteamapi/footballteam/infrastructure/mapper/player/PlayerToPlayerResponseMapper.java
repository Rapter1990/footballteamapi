package com.example.footballteamapi.footballteam.infrastructure.mapper.player;

import com.example.footballteamapi.common.infrastructure.mapper.BaseMapper;
import com.example.footballteamapi.footballteam.application.dto.response.PlayerResponse;
import com.example.footballteamapi.footballteam.domain.model.Player;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PlayerToPlayerResponseMapper extends BaseMapper<Player, PlayerResponse> {

    /**
     * Converts a {@link Player} domain model to a {@link PlayerResponse} DTO.
     *
     * @param player the domain model to convert.
     * @return a {@link PlayerResponse} DTO populated with data from the given domain model.
     */
    @Override
    default PlayerResponse map(Player player) {
        if (player == null) {
            return null;
        }
        return new PlayerResponse(
                player.getId(),
                player.getName(),
                player.isForeignPlayer(),
                player.getPosition()
        );
    }

    /**
     * Initializes and returns an instance of {@link PlayerToPlayerResponseMapper}.
     *
     * @return an instance of the mapper.
     */
    static PlayerToPlayerResponseMapper initialize() {
        return Mappers.getMapper(PlayerToPlayerResponseMapper.class);
    }

}
