package ru.mail.krivonos.al.controller;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import ru.mail.krivonos.al.controller.config.AppConfig;

@SpringBootApplication
@ComponentScan(basePackages = {
        "ru.mail.krivonos.al.controller",
        "ru.mail.krivonos.al.service",
        "ru.mail.krivonos.al.repository"
})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}
