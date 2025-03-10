package com.example.footballteamapi.footballteam.domain.model;

import com.example.footballteamapi.common.domain.model.BaseDomainModel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class FootballTeam extends BaseDomainModel {

    private String id;
    private String teamName;
    private List<Player> players;

}
