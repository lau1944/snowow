package com.vau.app;

import com.vau.snowow.engine.core.SnowManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.IOException;

@SpringBootApplication
public class Application {

    public static void main(String[] args) throws IOException {
        // Parse JSON files into java file
        SnowManager.getInstance().parse("json_path", "com.vau.app");
        SpringApplication.run(Application.class, args);
    }

}