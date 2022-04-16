package com.vau.snowow.engine.core;

import com.vau.snowow.engine.models.Configuration;
import com.vau.snowow.engine.models.Controller;
import com.vau.snowow.engine.parser.ConfigurationParser;
import com.vau.snowow.engine.parser.HttpParser;
import com.vau.snowow.engine.utils.FileUtil;
import com.vau.snowow.engine.writer.ConfigurationWriter;
import com.vau.snowow.engine.writer.ControllerWriter;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

/**
 * @author liuquan
 */
@Slf4j
public class SnowManager implements SnowEngine {

    static volatile SnowEngine engine;

    private SnowManager() {
    }

    @Override
    public String parse(String jsonPath, String packageName) throws IOException {
        if (Objects.isNull(jsonPath) || Strings.isEmpty(jsonPath)) {
            jsonPath = "snow_app";
        }

        String resPath = FileUtil.getApplicationPath() + "/resources/" + jsonPath;
        // Load the configuration file
        ConfigurationParser configurationParser = new ConfigurationParser();
        Configuration configuration = configurationParser.parse(resPath + "/configuration.json");

        log.info("Configuration file is successfully parsed, jsonPath is on {}", resPath);

        // Write configuration into application.properties
        ConfigurationWriter configurationWriter = (ConfigurationWriter) ConfigurationWriter.newWriter();
        int configResult = configurationWriter.write(configuration, FileUtil.getApplicationPath() + "/resources");
        if (configResult == -1) {
            throw new IllegalStateException("An error occurs when writing configuration file");
        }

        log.info("Configuration file is successfully written to application.properties file, jsonPath is on {}", resPath);

        String apiDir = resPath + "/api";
        // Load API file
        HttpParser httpParser = new HttpParser();
        List<Controller> controllers = httpParser.parse(apiDir);

        log.info("Controller files is successfully parsed, jsonPath is on {}, total {} http files is found", apiDir, controllers.size());

        // Write controller classes into packageName directory
        if (Objects.isNull(packageName) || packageName.isEmpty()) {
            throw new NullPointerException();
        }
        ControllerWriter controllerWriter = (ControllerWriter) ControllerWriter.newWriter();
        int pathResult = controllerWriter.write(controllers, packageName);
        if (pathResult == -1) {
            throw new IllegalStateException("An error occurs when writing Controller files");
        }

        return packageName;
    }

    public static SnowEngine getInstance() {
        if (Objects.nonNull(engine)) {
            return engine;
        }

        synchronized (SnowManager.class) {
            if (Objects.isNull(engine)) {
                engine = new SnowManager();
            }
        }

        return engine;
    }
}
