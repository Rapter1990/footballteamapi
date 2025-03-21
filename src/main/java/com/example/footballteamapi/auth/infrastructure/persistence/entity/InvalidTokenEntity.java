package com.example.footballteamapi.auth.infrastructure.persistence.entity;

import com.example.footballteamapi.common.infrastructure.persistence.entity.BaseEntity;
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
@Table(name = "INVALID_TOKEN")
public class InvalidTokenEntity extends BaseEntity {

    @Id
    @Column(name = "ID")
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "TOKEN_ID")
    private String tokenId;

}
