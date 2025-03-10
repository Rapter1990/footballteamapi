package com.example.footballteamapi.footballteam.infrastructure.mapper.player;

import com.example.footballteamapi.common.infrastructure.mapper.BaseMapper;
import com.example.footballteamapi.footballteam.domain.model.Player;
import com.example.footballteamapi.footballteam.infrastructure.persistence.entity.PlayerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface PlayerEntityToPlayerMapper extends BaseMapper<PlayerEntity, Player> {

    /**
     * Converts a {@link PlayerEntity} to a {@link Player} domain model.
     *
     * @param entity The {@link PlayerEntity} to be converted.
     * @return A {@link Player} populated with data from the given entity.
     */
    @Override
    default Player map(PlayerEntity entity) {
        if (entity == null) {
            return null;
        }
        return Player.builder()
                .id(entity.getId())
                .name(entity.getName())
                .foreignPlayer(entity.isForeignPlayer())
                .position(entity.getPosition())
                .build();
    }

    /**
     * Initializes and returns an instance of {@link PlayerEntityToPlayerMapper}.
     *
     * @return An initialized {@link PlayerEntityToPlayerMapper} instance.
     */
    static PlayerEntityToPlayerMapper initialize() {
        return Mappers.getMapper(PlayerEntityToPlayerMapper.class);
    }

}
