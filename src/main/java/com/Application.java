package com;

import com.ulisesbocchio.jasyptspringboot.annotation.EnableEncryptableProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.telegram.telegrambots.ApiContextInitializer;

@SpringBootApplication
@EnableScheduling
@EnableEncryptableProperties
public class Application {
    public static void main(String[] args) {
        ApiContextInitializer.init();
        SpringApplication.run(Application.class, args);
    }
}
