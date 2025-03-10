package com.example.footballteamapi.footballteam.infrastructure.mapper.player;

import com.example.footballteamapi.common.infrastructure.mapper.BaseMapper;
import com.example.footballteamapi.footballteam.application.dto.request.player.UpdatePlayerRequest;
import com.example.footballteamapi.footballteam.infrastructure.persistence.entity.PlayerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UpdatePlayerRequestToPlayerEntityMapper extends BaseMapper<UpdatePlayerRequest, PlayerEntity> {

    /**
     * Updates the given {@link PlayerEntity} using data from the {@link UpdatePlayerRequest}.
     *
     * @param request The {@link UpdatePlayerRequest} object containing updated data.
     * @param entity  The {@link PlayerEntity} to be updated.
     * @return The updated {@link PlayerEntity}.
     */
    @Named("mapForUpdating")
    default PlayerEntity mapForUpdating(UpdatePlayerRequest request, @MappingTarget PlayerEntity entity) {
        entity.setName(request.name());
        entity.setForeignPlayer(request.foreignPlayer());
        entity.setPosition(request.position());
        return entity;
    }

    /**
     * Initializes and returns an instance of {@link UpdatePlayerRequestToPlayerEntityMapper}.
     *
     * @return An initialized {@link UpdatePlayerRequestToPlayerEntityMapper} instance.
     */
    static UpdatePlayerRequestToPlayerEntityMapper initialize() {
        return Mappers.getMapper(UpdatePlayerRequestToPlayerEntityMapper.class);
    }
}
