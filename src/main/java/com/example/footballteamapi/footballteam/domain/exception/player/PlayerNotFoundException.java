package com.example.footballteamapi.footballteam.domain.exception.player;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class PlayerNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 3151992111816185023L;

    public static final HttpStatus STATUS = HttpStatus.NOT_FOUND;

    private static final String DEFAULT_MESSAGE = "Player not found!";

    public PlayerNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public PlayerNotFoundException(final String message) {
        super(DEFAULT_MESSAGE + " " + message);
    }
}
