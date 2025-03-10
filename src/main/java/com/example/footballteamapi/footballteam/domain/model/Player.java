package com.example.footballteamapi.footballteam.domain.model;

import com.example.footballteamapi.common.domain.model.BaseDomainModel;
import com.example.footballteamapi.footballteam.domain.enums.Position;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class Player extends BaseDomainModel {

    private String id;
    private String name;
    private boolean foreignPlayer;
    private Position position;

}
