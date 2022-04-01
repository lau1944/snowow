package parser;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import exceptions.ParserException;
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
    public Configuration parse(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Configuration file path should not be empty");
        }

        File jsonFile = new File(path);
        try {
            JsonObject jsonObject = (JsonObject) JsonParser.parseReader(new FileReader(jsonFile));
            Configuration configuration = this.gson.fromJson(jsonObject, Configuration.class);
            return configuration;
        } catch (FileNotFoundException e) {
            log.error("Server exception happens in {}, please make sure your configuration.json is present in your path", ERROR_PATH);
            throw new ParserException("configuration.json is not found in your path");
        }
    }
}
