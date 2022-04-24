package com.vau.app;

import com.vau.snowow.engine.core.SnowManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

import java.io.IOException;

/**
 * Run with ./mvnw install && ./mvnw spring-boot:run -pl application
 */
@SpringBootApplication(exclude={DataSourceAutoConfiguration.class})
public class Application {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // Parse JSON files into java file
        SnowManager.getInstance().defaultParse("com.vau.app");
        SpringApplication.run(Application.class, args);
    }

}