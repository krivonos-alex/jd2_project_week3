package ru.mail.krivonos.al.repository.exceptions;

public class DatabaseIntialisingQueryException extends RuntimeException {

    public DatabaseIntialisingQueryException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
