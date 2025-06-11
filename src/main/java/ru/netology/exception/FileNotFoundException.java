package ru.netology.exception;

public class FileNotFoundException extends RuntimeException{
    public FileNotFoundException (String msg){
        super(msg);
    }
}
