package com.example.footballteamapi.builder;

import com.example.footballteamapi.auth.domain.enums.UserStatus;
import com.example.footballteamapi.auth.domain.enums.UserType;
import com.example.footballteamapi.auth.infrastructure.persistence.entity.UserEntity;

import java.util.UUID;

/**
 * A builder class for creating instances of {@link UserEntity} with specific properties set.
 */
public class UserBuilder extends BaseBuilder<UserEntity> {

    public UserBuilder() {
        super(UserEntity.class);
    }

    public UserBuilder withValidFields() {
        return this
                .withId(UUID.randomUUID().toString())
                .withEmail("userexample@example.com")
                .withPassword("userpassword")
                .withFirstName("User FirstName")
                .withLastName("User LastName")
                .withPhoneNumber("1234567890")
                .withUserType(UserType.USER)
                .withUserStatus(UserStatus.ACTIVE);
    }

    public UserBuilder withId(String id) {
        data.setId(id);
        return this;
    }

    public UserBuilder withEmail(String email) {
        data.setEmail(email);
        return this;
    }

    public UserBuilder withPassword(String password) {
        data.setPassword(password);
        return this;
    }

    public UserBuilder withFirstName(String firstName) {
        data.setFirstName(firstName);
        return this;
    }

    public UserBuilder withLastName(String lastName) {
        data.setLastName(lastName);
        return this;
    }

    public UserBuilder withPhoneNumber(String phoneNumber) {
        data.setPhoneNumber(phoneNumber);
        return this;
    }

    public UserBuilder withUserType(UserType userType) {
        data.setUserType(userType);
        return this;
    }

    public UserBuilder withUserStatus(UserStatus userStatus) {
        data.setUserStatus(userStatus);
        return this;
    }

}
