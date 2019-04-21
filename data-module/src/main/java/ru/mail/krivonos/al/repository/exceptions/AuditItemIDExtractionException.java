package ru.mail.krivonos.al.repository.exceptions;

public class AuditItemIDExtractionException extends RuntimeException {

    public AuditItemIDExtractionException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
