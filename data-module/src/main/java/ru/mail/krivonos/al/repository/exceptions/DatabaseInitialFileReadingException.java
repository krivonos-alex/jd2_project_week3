package ru.mail.krivonos.al.repository.exceptions;

public class DatabaseInitialFileReadingException extends RuntimeException {

    public DatabaseInitialFileReadingException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
