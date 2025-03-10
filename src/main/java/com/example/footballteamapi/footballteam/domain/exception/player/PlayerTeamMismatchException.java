package com.example.footballteamapi.footballteam.domain.exception.player;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class PlayerTeamMismatchException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -4977090309602391420L;

    public static final HttpStatus STATUS = HttpStatus.BAD_REQUEST;

    private static final String DEFAULT_MESSAGE = "Player does not belong to the given team";

    public PlayerTeamMismatchException() {
        super(DEFAULT_MESSAGE);
    }

    public PlayerTeamMismatchException(final String message) {
        super(DEFAULT_MESSAGE + " " + message);
    }
}
