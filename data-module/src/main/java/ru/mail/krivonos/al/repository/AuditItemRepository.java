package ru.mail.krivonos.al.repository;

import ru.mail.krivonos.al.repository.model.AuditItem;

import java.sql.Connection;

public interface AuditItemRepository {

    AuditItem add(Connection connection, AuditItem auditItem);
}
