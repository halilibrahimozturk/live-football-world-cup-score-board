package org.ozturk.exception;

public class TeamAlreadyPlayingException extends RuntimeException{
    public TeamAlreadyPlayingException(String message) {
        super(message);
    }
}