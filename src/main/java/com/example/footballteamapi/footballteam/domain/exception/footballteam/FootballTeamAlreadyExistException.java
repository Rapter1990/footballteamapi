package com.example.footballteamapi.footballteam.domain.exception.footballteam;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class FootballTeamAlreadyExistException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 2205862961122113538L;

    public static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    private static final String DEFAULT_MESSAGE = "A team with the given name already exists.";

    public FootballTeamAlreadyExistException() {
        super(DEFAULT_MESSAGE);
    }

    public FootballTeamAlreadyExistException(String message) {
        super(DEFAULT_MESSAGE + " " + message);
    }

}
