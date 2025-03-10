package com.example.footballteamapi.auth.application.service;

import java.util.Set;

public interface InvalidTokenService {

    void invalidateTokens(final Set<String> tokenIds);

    void checkForInvalidityOfToken(final String tokenId);

}
