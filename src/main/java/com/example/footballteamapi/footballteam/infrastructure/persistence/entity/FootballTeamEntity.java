package com.example.footballteamapi.footballteam.infrastructure.persistence.entity;

import com.example.footballteamapi.common.infrastructure.persistence.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Table(name = "FOOTBALL_TEAMS")
public class FootballTeamEntity extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID")
    private String id;

    private String teamName;

    @OneToMany(mappedBy = "footballTeam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PlayerEntity> players = new ArrayList<>();

}
