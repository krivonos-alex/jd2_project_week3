package ru.mail.krivonos.al.controller.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ComponentScan(basePackages = {
        "ru.mail.krivonos.al.service",
        "ru.mail.krivonos.al.repository"
})
@PropertySource("classpath:database.properties")
public class AppConfig {
}
