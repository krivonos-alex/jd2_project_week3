package ru.mail.krivonos.al.repository.connection.impl;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;
import ru.mail.krivonos.al.repository.connection.ConnectionHandler;
import ru.mail.krivonos.al.repository.exceptions.DatabaseConnectionException;
import ru.mail.krivonos.al.repository.exceptions.DatabaseDriverException;
import ru.mail.krivonos.al.repository.exceptions.DatabaseInitialFileReadingException;
import ru.mail.krivonos.al.repository.exceptions.DatabaseIntialisingQueryException;
import ru.mail.krivonos.al.repository.properties.DatabaseProperties;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import java.util.stream.Stream;

@Component("connectionHandler")
public class ConnectionHandlerImpl implements ConnectionHandler {

    private static final String INITIALISING_QUERY_EXCEPTION_ERROR_MESSAGE = "Something goes wrong while executing " +
            "query for database initialisation.";
    private static final String DATABASE_CONNECTION_ERROR_MESSAGE = "Can't create connection to database.";
    private static final String DATABASE_DRIVER_ERROR_MESSAGE = "Database driver class not found.";
    private static final String INITIAL_FILE_ERROR_MESSAGE = "Error while reading database initial file.";

    private static final Logger logger = LoggerFactory.getLogger(ConnectionHandlerImpl.class);

    private final DatabaseProperties databaseProperties;

    @Autowired
    public ConnectionHandlerImpl(
            @Qualifier("databaseProperties") DatabaseProperties databaseProperties) {
        this.databaseProperties = databaseProperties;
        try {
            Class.forName(databaseProperties.getDatabaseDriverName());
        } catch (ClassNotFoundException e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseDriverException(DATABASE_DRIVER_ERROR_MESSAGE, e);
        }
    }

    @Override
    public Connection getConnection() {
        try {
            Properties properties = new Properties();
            properties.setProperty("user", databaseProperties.getDatabaseUsername());
            properties.setProperty("password", databaseProperties.getDatabasePassword());
            properties.setProperty("useUnicode", "true");
            return DriverManager.getConnection(databaseProperties.getDatabaseURL(), properties);
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseConnectionException(DATABASE_CONNECTION_ERROR_MESSAGE, e);
        }
    }

    @PostConstruct
    public void initializeDatabase() {
        String initialFileName = getClass().getResource("/" + databaseProperties.getDatabaseInitialFile()).getPath();
        String[] databaseInitialQueries = getQueries(initialFileName);
        try (Connection connection = getConnection()) {
            connection.setAutoCommit(false);
            try (Statement statement = connection.createStatement()) {
                for (String databaseInitialQuery : databaseInitialQueries) {
                    statement.addBatch(databaseInitialQuery);
                }
                statement.executeBatch();
                connection.commit();
            } catch (Exception e) {
                connection.rollback();
                logger.error(e.getMessage(), e);
                throw new DatabaseIntialisingQueryException(INITIALISING_QUERY_EXCEPTION_ERROR_MESSAGE, e);
            }
        } catch (SQLException e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseConnectionException(DATABASE_CONNECTION_ERROR_MESSAGE, e);
        }
    }

    private String[] getQueries(String initialFileName) {
        try (Stream<String> fileStream = Files.lines(Paths.get(initialFileName))) {
            return fileStream.reduce(String::concat).orElse("").split(";");
        } catch (IOException e) {
            logger.error(e.getMessage(), e);
            throw new DatabaseInitialFileReadingException(INITIAL_FILE_ERROR_MESSAGE, e);
        }
    }
}
