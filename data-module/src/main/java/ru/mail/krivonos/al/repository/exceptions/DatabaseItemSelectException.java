package ru.mail.krivonos.al.repository.exceptions;

public class DatabaseItemSelectException extends Exception {

    public DatabaseItemSelectException(String s, Throwable throwable) {
        super(s, throwable);
    }
}
