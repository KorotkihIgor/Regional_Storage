package ru.netology.exception;

public class FileBadRequestException extends RuntimeException {
    public FileBadRequestException(String msg) {
        super(msg);
    }
}
