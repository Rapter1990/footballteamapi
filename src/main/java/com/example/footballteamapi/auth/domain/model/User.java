package com.example.footballteamapi.auth.domain.model;

import com.example.footballteamapi.auth.domain.enums.UserStatus;
import com.example.footballteamapi.auth.domain.enums.UserType;
import com.example.footballteamapi.common.domain.model.BaseDomainModel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@EqualsAndHashCode(callSuper = true)
public class User extends BaseDomainModel {

    private String id;
    private String email;
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private UserType userType;
    private UserStatus userStatus;

}
