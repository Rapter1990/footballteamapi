package com.example.footballteamapi.auth.infrastructure.mapper;

import com.example.footballteamapi.auth.application.dto.response.TokenResponse;
import com.example.footballteamapi.auth.domain.model.Token;
import com.example.footballteamapi.common.infrastructure.mapper.BaseMapper;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface TokenToTokenResponseMapper extends BaseMapper<Token, TokenResponse> {

    @Override
    TokenResponse map(Token source);

    static TokenToTokenResponseMapper initialize() {
        return Mappers.getMapper(TokenToTokenResponseMapper.class);
    }

}
