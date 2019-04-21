package ru.mail.krivonos.al.service.exceptions;

public class ItemAddException extends RuntimeException {

    public ItemAddException(String s, Exception e) {
        super(s, e);
    }
}
