package core;

import lombok.extern.slf4j.Slf4j;
import models.Configuration;
import org.apache.logging.log4j.util.Strings;
import parser.ConfigurationParser;
import utils.FileUtil;
import writer.ConfigurationWriter;

import java.io.IOException;
import java.util.Objects;

@Slf4j
public class SnowManager implements SnowEngine {

    static volatile SnowEngine engine;

    private SnowManager() {
    }

    @Override
    public String parse(String path, String output) throws IOException {
        if (Objects.isNull(path) || Strings.isEmpty(path)) {
            path = "snow_app";
        }

        if (Objects.isNull(output) || Strings.isEmpty(output)) {
            output = "outputs";
        }

        String resPath = FileUtil.getApplicationPath() + "/resources/" + path;
        // Load the configuration file
        ConfigurationParser configurationParser = new ConfigurationParser();
        Configuration configuration = configurationParser.parse(resPath + "/configuration.json");

        log.info("Configuration file is successfully parsed, path is on {}", resPath);

        // Write configuration into application.properties
        ConfigurationWriter writer = new ConfigurationWriter();
        int result = writer.write(configuration, FileUtil.getApplicationPath() + "/resources");
        if (result == -1) {
            throw new IllegalStateException("An error occurs when writing configuration file");
        }

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
