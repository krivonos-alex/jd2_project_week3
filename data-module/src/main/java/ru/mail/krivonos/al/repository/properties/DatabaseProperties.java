package ru.mail.krivonos.al.repository.properties;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component("databaseProperties")
public class DatabaseProperties {

    @Value("${spring.datasource.driver-class-name}")
    private String databaseDriverName;
    @Value("${spring.datasource.url}")
    private String databaseURL;
    @Value("${spring.datasource.username:}")
    private String databaseUsername;
    @Value("${spring.datasource.password:}")
    private String databasePassword;
    @Value("${database.initial.file}")
    private String databaseInitialFile;

    public String getDatabaseDriverName() {
        return databaseDriverName;
    }

    public String getDatabaseURL() {
        return databaseURL;
    }

    public String getDatabaseUsername() {
        return databaseUsername;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }

    public String getDatabaseInitialFile() {
        return databaseInitialFile;
    }
}
