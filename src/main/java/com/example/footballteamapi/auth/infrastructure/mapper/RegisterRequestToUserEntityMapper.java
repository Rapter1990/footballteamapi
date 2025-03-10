package com.example.footballteamapi.auth.infrastructure.mapper;

import com.example.footballteamapi.auth.application.dto.request.RegisterRequest;
import com.example.footballteamapi.auth.infrastructure.persistence.entity.UserEntity;
import com.example.footballteamapi.common.infrastructure.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Named;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RegisterRequestToUserEntityMapper extends BaseMapper<RegisterRequest, UserEntity> {

    /**
     * Maps a {@link RegisterRequest} to a {@link UserEntity} for saving purposes.
     *
     * @param registerRequest The {@link RegisterRequest} object to be mapped.
     * @return A {@link UserEntity} populated with the data from the given {@link RegisterRequest}.
     */
    @Named("mapForSaving")
    default UserEntity mapForSaving(RegisterRequest registerRequest) {
        return UserEntity.builder()
                .email(registerRequest.getEmail())
                .firstName(registerRequest.getFirstName())
                .lastName(registerRequest.getLastName())
                .phoneNumber(registerRequest.getPhoneNumber())
                .userType(registerRequest.getUserType())
                .build();
    }

    /**
     * Initializes and returns an instance of {@link RegisterRequestToUserEntityMapper}.
     *
     * @return An initialized {@link RegisterRequestToUserEntityMapper} instance.
     */
    static RegisterRequestToUserEntityMapper initialize() {
        return Mappers.getMapper(RegisterRequestToUserEntityMapper.class);
    }

}
