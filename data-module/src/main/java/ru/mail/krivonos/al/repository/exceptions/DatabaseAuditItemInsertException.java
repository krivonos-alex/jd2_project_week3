package ru.mail.krivonos.al.repository.exceptions;

public class DatabaseAuditItemInsertException extends RuntimeException {

    public DatabaseAuditItemInsertException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
