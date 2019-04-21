package ru.mail.krivonos.al.service.exceptions;

public class ItemsGetException extends RuntimeException {

    public ItemsGetException(String s, Exception e) {
        super(s, e);
    }
}
