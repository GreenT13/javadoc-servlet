package com.apon.javadocservlet.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Exception that can be used to encapsulate other exceptions. This exception can bubble up in Spring Boot and is
 * automatically handled correctly.
 */
@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class ApplicationException extends RuntimeException {
    public ApplicationException(String message) {
        super(message);
    }

    public ApplicationException(Throwable cause) {
        super(cause);
    }
}
