package com.example.footballteamapi.auth.domain.exception;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class UnAuthorizeAttemptException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -8316154874513165963L;

    public static final HttpStatus STATUS = HttpStatus.UNAUTHORIZED;

    private static final String DEFAULT_MESSAGE = """
            You do not have permission to create a to-do item.
            """;

    public UnAuthorizeAttemptException() {
        super(DEFAULT_MESSAGE);
    }

}
