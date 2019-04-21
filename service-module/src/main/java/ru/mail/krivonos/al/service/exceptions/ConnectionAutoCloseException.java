package ru.mail.krivonos.al.service.exceptions;

public class ConnectionAutoCloseException extends RuntimeException {

    public ConnectionAutoCloseException(String s, Exception e) {
        super(s, e);
    }
}
