package com.example.footballteamapi.footballteam.infrastructure.mapper.player;

import com.example.footballteamapi.common.application.dto.response.CustomPagingResponse;
import com.example.footballteamapi.common.domain.model.CustomPage;
import com.example.footballteamapi.footballteam.application.dto.response.PlayerResponse;
import com.example.footballteamapi.footballteam.domain.model.Player;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface CustomPagePlayerToCustomPagingPlayerResponseMapper {

    PlayerToPlayerResponseMapper playerToPlayerResponseMapper = Mappers.getMapper(PlayerToPlayerResponseMapper.class);

    /**
     * Converts a {@link CustomPage} of {@link Player} domain models to a {@link CustomPagingResponse}
     * containing {@link PlayerResponse} DTOs.
     *
     * @param playerPage the {@link CustomPage} containing a list of {@link Player} domain models.
     * @return a {@link CustomPagingResponse} with the mapped {@link PlayerResponse} list,
     *         or {@code null} if {@code playerPage} is {@code null}.
     */
    default CustomPagingResponse<PlayerResponse> toPagingResponse(CustomPage<Player> playerPage) {
        if (playerPage == null) {
            return null;
        }
        return CustomPagingResponse.<PlayerResponse>builder()
                .content(toPlayerResponseList(playerPage.getContent()))
                .totalElementCount(playerPage.getTotalElementCount())
                .totalPageCount(playerPage.getTotalPageCount())
                .pageNumber(playerPage.getPageNumber())
                .pageSize(playerPage.getPageSize())
                .build();
    }

    /**
     * Converts a list of {@link Player} domain models to a list of {@link PlayerResponse} DTOs.
     *
     * @param players the list of {@link Player} domain models.
     * @return a list of {@link PlayerResponse} DTOs, or {@code null} if {@code players} is {@code null}.
     */
    default List<PlayerResponse> toPlayerResponseList(List<Player> players) {
        if (players == null) {
            return null;
        }
        return players.stream()
                .map(playerToPlayerResponseMapper::map)
                .collect(Collectors.toList());
    }

    /**
     * Initializes and returns an instance of {@link CustomPagePlayerToCustomPagingPlayerResponseMapper}.
     *
     * @return an instance of the mapper.
     */
    static CustomPagePlayerToCustomPagingPlayerResponseMapper initialize() {
        return Mappers.getMapper(CustomPagePlayerToCustomPagingPlayerResponseMapper.class);
    }

}
