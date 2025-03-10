package com.example.footballteamapi.footballteam.application.dto.request.footballteam;

import com.example.footballteamapi.common.application.dto.request.CustomPagingRequest;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
public class FootballTeamPagingRequest extends CustomPagingRequest {
}
