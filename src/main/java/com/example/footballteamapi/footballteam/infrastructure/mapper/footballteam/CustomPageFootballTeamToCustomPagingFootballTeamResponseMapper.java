package com.example.footballteamapi.footballteam.infrastructure.mapper.footballteam;

import com.example.footballteamapi.common.application.dto.response.CustomPagingResponse;
import com.example.footballteamapi.common.domain.model.CustomPage;
import com.example.footballteamapi.footballteam.application.dto.response.FootballTeamResponse;
import com.example.footballteamapi.footballteam.domain.model.FootballTeam;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.stream.Collectors;

@Mapper
public interface CustomPageFootballTeamToCustomPagingFootballTeamResponseMapper {

    FootballTeamToFootballTeamResponseMapper footballTeamToFootballTeamResponseMapper =
            Mappers.getMapper(FootballTeamToFootballTeamResponseMapper.class);

    /**
     * Converts a {@link CustomPage} of {@link FootballTeam} domain models to a {@link CustomPagingResponse}
     * containing {@link FootballTeamResponse} DTOs.
     *
     * @param footballTeamPage the {@link CustomPage} containing a list of {@link FootballTeam} domain models
     * @return a {@link CustomPagingResponse} with the mapped {@link FootballTeamResponse} list,
     *         or {@code null} if {@code footballTeamPage} is {@code null}
     */
    default CustomPagingResponse<FootballTeamResponse> toPagingResponse(CustomPage<FootballTeam> footballTeamPage) {
        if (footballTeamPage == null) {
            return null;
        }
        return CustomPagingResponse.<FootballTeamResponse>builder()
                .content(toFootballTeamResponseList(footballTeamPage.getContent()))
                .totalElementCount(footballTeamPage.getTotalElementCount())
                .totalPageCount(footballTeamPage.getTotalPageCount())
                .pageNumber(footballTeamPage.getPageNumber())
                .pageSize(footballTeamPage.getPageSize())
                .build();
    }

    /**
     * Converts a list of {@link FootballTeam} domain models to a list of {@link FootballTeamResponse} DTOs.
     *
     * @param footballTeams the list of {@link FootballTeam} domain models
     * @return a list of {@link FootballTeamResponse} DTOs, or {@code null} if {@code footballTeams} is {@code null}
     */
    default List<FootballTeamResponse> toFootballTeamResponseList(List<FootballTeam> footballTeams) {
        if (footballTeams == null) {
            return null;
        }
        return footballTeams.stream()
                .map(footballTeamToFootballTeamResponseMapper::map)
                .collect(Collectors.toList());
    }

    /**
     * Initializes and returns an instance of the {@link CustomPageFootballTeamToCustomPagingFootballTeamResponseMapper}.
     *
     * @return an instance of the mapper
     */
    static CustomPageFootballTeamToCustomPagingFootballTeamResponseMapper initialize() {
        return Mappers.getMapper(CustomPageFootballTeamToCustomPagingFootballTeamResponseMapper.class);
    }
}
