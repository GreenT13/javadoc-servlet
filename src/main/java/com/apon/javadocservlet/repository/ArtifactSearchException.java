package com.apon.javadocservlet.repository;

/**
 * Exception that can be thrown by implementations of {@link ArtifactStorage}.
 */
public class ArtifactSearchException extends Exception {
    public ArtifactSearchException(String message) {
        super(message);
    }
}
