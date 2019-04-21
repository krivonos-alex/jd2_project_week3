package ru.mail.krivonos.al.repository.connection;

import java.sql.Connection;

public interface ConnectionHandler {

    Connection getConnection();
}
