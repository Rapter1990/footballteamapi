package com.example.footballteamapi.auth.infrastructure.mapper;

import com.example.footballteamapi.auth.domain.model.User;
import com.example.footballteamapi.auth.infrastructure.persistence.entity.UserEntity;
import com.example.footballteamapi.common.infrastructure.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface UserEntityToUserMapper extends BaseMapper<UserEntity, User> {

    @Override
    User map(UserEntity source);

    static UserEntityToUserMapper initialize() {
        return Mappers.getMapper(UserEntityToUserMapper.class);
    }

}
