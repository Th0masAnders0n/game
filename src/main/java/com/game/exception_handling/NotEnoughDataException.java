package com.game.exception_handling;

public class NotEnoughDataException extends RuntimeException {
    public NotEnoughDataException(String message) {
        super(message);
    }
}
