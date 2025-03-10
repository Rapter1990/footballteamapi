package com.example.footballteamapi.footballteam.domain.exception.player;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class MaxPlayersExceededException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 2142749596866493599L;

    public static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    private static final String DEFAULT_MESSAGE = "A team can have at most 18 players";

    public MaxPlayersExceededException() {
        super(DEFAULT_MESSAGE);
    }

    public MaxPlayersExceededException(String message) {
        super(DEFAULT_MESSAGE + " " + message);
    }
}
