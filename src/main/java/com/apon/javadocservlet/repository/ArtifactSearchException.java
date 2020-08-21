package com.apon.javadocservlet.repository;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class ArtifactSearchException extends Exception {
    public ArtifactSearchException() {
    }

    public ArtifactSearchException(String message) {
        super(message);
    }
}
