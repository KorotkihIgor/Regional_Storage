package ru.netology.exception;

public class PasswordBadRequest extends RuntimeException {
    public PasswordBadRequest(String message) {
        super(message);
    }
}
