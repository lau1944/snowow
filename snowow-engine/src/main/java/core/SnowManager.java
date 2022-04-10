package core;

import lombok.extern.slf4j.Slf4j;
import models.Configuration;
import models.Controller;
import org.apache.logging.log4j.util.Strings;
import parser.ConfigurationParser;
import parser.HttpParser;
import utils.FileUtil;
import writer.ConfigurationWriter;
import writer.ControllerWriter;

import java.io.IOException;
import java.util.List;
import java.util.Objects;

@Slf4j
public class SnowManager implements SnowEngine {

    static volatile SnowEngine engine;

    private SnowManager() {
    }

    @Override
    public String parse(String jsonPath, String output) throws IOException {
        if (Objects.isNull(jsonPath) || Strings.isEmpty(jsonPath)) {
            jsonPath = "snow_app";
        }

        String resPath = FileUtil.getApplicationPath() + "/resources/" + jsonPath;
        // Load the configuration file
        ConfigurationParser configurationParser = new ConfigurationParser();
        Configuration configuration = configurationParser.parse(resPath + "/configuration.json");

        log.info("Configuration file is successfully parsed, jsonPath is on {}", resPath);

        // Write configuration into application.properties
        ConfigurationWriter configurationWriter = new ConfigurationWriter();
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

        // Write controller classes into output directory
        if (Objects.isNull(output) || output.isEmpty()) {
            output = FileUtil.getRepoPath();
        }
        ControllerWriter controllerWriter = new ControllerWriter();
        int pathResult = controllerWriter.write(controllers, output);

        return output;
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
