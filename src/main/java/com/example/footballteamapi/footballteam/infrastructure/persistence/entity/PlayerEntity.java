package com.example.footballteamapi.footballteam.infrastructure.persistence.entity;

import com.example.footballteamapi.common.infrastructure.persistence.entity.BaseEntity;
import com.example.footballteamapi.footballteam.domain.enums.Position;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "FOOTBALL_PLAYERS")
public class PlayerEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID")
    private String id;

    private String name;

    private boolean foreignPlayer;

    @Enumerated(EnumType.STRING)
    private Position position;

    @ManyToOne
    @JoinColumn(name = "FOOTBALL_TEAM_ID")
    private FootballTeamEntity footballTeam;

}
