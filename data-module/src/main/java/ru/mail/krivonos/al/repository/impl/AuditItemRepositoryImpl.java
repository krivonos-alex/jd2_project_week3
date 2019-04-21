package ru.mail.krivonos.al.repository.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;
import ru.mail.krivonos.al.repository.AuditItemRepository;
import ru.mail.krivonos.al.repository.exceptions.AuditItemIDExtractionException;
import ru.mail.krivonos.al.repository.exceptions.DatabaseAuditItemInsertException;
import ru.mail.krivonos.al.repository.model.AuditItem;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

@Repository("auditItemRepository")
public class AuditItemRepositoryImpl implements AuditItemRepository {

    private static final String QUERY_EXCEPTION_ERROR_MESSAGE = "Can't execute query: \"%s\".";
    private static final String ID_EXTRACTION_ERROR_MESSAGE = "Something goes wrong while extracting generated ID.";

    private static final Logger logger = LoggerFactory.getLogger(AuditItemRepositoryImpl.class);

    @Override
    public AuditItem add(Connection connection, AuditItem auditItem) {
        String sql = "INSERT INTO AuditItem (item_id, action, date) VALUES (?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            preparedStatement.setLong(1, auditItem.getItemID());
            preparedStatement.setString(2, auditItem.getAction().name());
            preparedStatement.setTimestamp(3, new Timestamp(auditItem.getDate().getTime()));
            int added = preparedStatement.executeUpdate();
            try (ResultSet resultSet = preparedStatement.getGeneratedKeys()) {
                return getAuditItemWithID(resultSet, auditItem);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseAuditItemInsertException(String.format(QUERY_EXCEPTION_ERROR_MESSAGE, sql), e);
        }
    }

    private AuditItem getAuditItemWithID(ResultSet resultSet, AuditItem auditItem) throws AuditItemIDExtractionException {
        try {
            if (resultSet.next()) {
                AuditItem returningAuditItem = new AuditItem();
                returningAuditItem.setItemID(auditItem.getItemID());
                returningAuditItem.setAction(auditItem.getAction());
                returningAuditItem.setDate(auditItem.getDate());
                returningAuditItem.setId(resultSet.getLong(1));
                return returningAuditItem;
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new AuditItemIDExtractionException(ID_EXTRACTION_ERROR_MESSAGE, e);
        }
        return null;
    }
}
