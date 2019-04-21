package ru.mail.krivonos.al.service.exceptions;

public class ItemsUpdateException extends RuntimeException {

    public ItemsUpdateException(String s, Exception e) {
        super(s, e);
    }
}
