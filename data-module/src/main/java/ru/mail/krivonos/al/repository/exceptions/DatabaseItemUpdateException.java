package ru.mail.krivonos.al.repository.exceptions;

public class DatabaseItemUpdateException extends Exception {

    public DatabaseItemUpdateException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
