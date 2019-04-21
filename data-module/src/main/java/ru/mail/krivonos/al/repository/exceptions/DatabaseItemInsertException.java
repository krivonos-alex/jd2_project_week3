package ru.mail.krivonos.al.repository.exceptions;

public class DatabaseItemInsertException extends Exception {

    public DatabaseItemInsertException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
