package com.example.footballteamapi.footballteam.domain.exception.footballteam;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class FootballTeamNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1194678543892890668L;

    public static final HttpStatus STATUS = HttpStatus.NOT_FOUND;

    private static final String DEFAULT_MESSAGE = "Football team not found!";

    public FootballTeamNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public FootballTeamNotFoundException(final String message) {
        super(DEFAULT_MESSAGE + " " + message);
    }
}
