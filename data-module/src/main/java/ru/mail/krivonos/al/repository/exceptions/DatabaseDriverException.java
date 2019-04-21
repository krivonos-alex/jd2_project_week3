package ru.mail.krivonos.al.repository.exceptions;

public class DatabaseDriverException extends RuntimeException {

    public DatabaseDriverException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
