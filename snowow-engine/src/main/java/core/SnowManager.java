package core;

import lombok.extern.log4j.Log4j;
import models.Configuration;
import org.apache.logging.log4j.util.Strings;
import parser.ConfigurationParser;

import java.util.Objects;

@Log4j
public class SnowManager implements SnowEngine {

    static volatile SnowEngine engine;

    private SnowManager(){
    }

    @Override
    public String parse(String path, String output) {
        if (Objects.isNull(path) || Strings.isEmpty(path)) {
            path = "snow_json";
        }

        if (Objects.isNull(output) || Strings.isEmpty(output)) {
            output = "outputs";
        }

        // Load the configuration file
        ConfigurationParser configurationParser = new ConfigurationParser();
        Configuration configuration = configurationParser.parse(path + "configuration.json");
    }

    static SnowEngine getInstance() {
        if (Objects.nonNull(engine)) {
            return engine;
        }

        synchronized(SnowManager.class) {
            if (Objects.isNull(engine)) {
                engine = new SnowManager();
            }
        }

        return engine;
    }
}
