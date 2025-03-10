package com.example.footballteamapi.footballteam.infrastructure.mapper.player;

import com.example.footballteamapi.common.infrastructure.mapper.BaseMapper;
import com.example.footballteamapi.footballteam.application.dto.request.player.AddPlayerRequest;
import com.example.footballteamapi.footballteam.infrastructure.persistence.entity.PlayerEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface AddPlayerRequestToPlayerEntityMapper extends BaseMapper<AddPlayerRequest, PlayerEntity> {

    /**
     * Maps an {@link AddPlayerRequest} to a {@link PlayerEntity} for saving purposes.
     *
     * @param request The {@link AddPlayerRequest} object to be mapped.
     * @return A {@link PlayerEntity} populated with data from the given {@link AddPlayerRequest}.
     */
    @Named("mapForSaving")
    default PlayerEntity mapForSaving(AddPlayerRequest request) {
        return PlayerEntity.builder()
                .name(request.name())
                .foreignPlayer(request.foreignPlayer())
                .position(request.position())
                .build();
    }

    /**
     * Initializes and returns an instance of {@link AddPlayerRequestToPlayerEntityMapper}.
     *
     * @return An initialized {@link AddPlayerRequestToPlayerEntityMapper} instance.
     */
    static AddPlayerRequestToPlayerEntityMapper initialize() {
        return Mappers.getMapper(AddPlayerRequestToPlayerEntityMapper.class);
    }

}
