package com.example.footballteamapi.auth.domain.exception;

import org.springframework.http.HttpStatus;

import java.io.Serial;

public class RoleNotFoundException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 5861011004511044126L;

    public static final HttpStatus STATUS = HttpStatus.NOT_FOUND;

    private static final String DEFAULT_MESSAGE = """
            Role not found!
            """;

    public RoleNotFoundException() {
        super(DEFAULT_MESSAGE);
    }

    public RoleNotFoundException(final String message) {
        super(DEFAULT_MESSAGE + " " + message);
    }

}
