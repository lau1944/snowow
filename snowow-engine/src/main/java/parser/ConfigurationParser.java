package parser;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import models.Configuration;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Parsing configuration JSON file into #Configuration object
 */
@Slf4j
public class ConfigurationParser extends BaseParser {

    private static final String ERROR_PATH = "ConfigurationParser";

    @Override
    public Configuration parse(String path) throws FileNotFoundException {
        if (path == null) {
            throw new IllegalArgumentException("Configuration file path should not be empty");
        }

        File jsonFile = new File(path);
        try {
            JsonObject jsonObject = (JsonObject) JsonParser.parseReader(new FileReader(jsonFile));
            Configuration configuration = this.gson.fromJson(jsonObject, Configuration.class);
            return configuration;
        } catch (Exception e) {
            log.error("Server exception happens in {}, please check that out before the production", ERROR_PATH);
            throw e;
        }
    }
}
