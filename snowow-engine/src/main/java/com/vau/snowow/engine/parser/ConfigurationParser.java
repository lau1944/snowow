package com.vau.snowow.engine.parser;

import com.vau.snowow.engine.exceptions.ParserException;
import com.vau.snowow.engine.models.Configuration;
import com.vau.snowow.engine.utils.Deserializer;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Map;

/**
 * Parsing configuration JSON file into #Configuration object
 *
 * @author liuquan
 */
@Slf4j
public class ConfigurationParser extends BaseParser<Configuration> {

    private static final String ERROR_PATH = "ConfigurationParser";

    @Override
    public synchronized Configuration parse(String path) {
        if (path == null) {
            throw new IllegalArgumentException("Configuration file path should not be empty");
        }

        File jsonFile = new File(path);
        try {
            Configuration configuration = Deserializer.deserializeTo(jsonFile, Configuration.class);
            configuration.setElements(Deserializer.deserialize(jsonFile));
            return configuration;
        } catch (FileNotFoundException e) {
            log.error("Server exception happens in {}, please make sure your configuration.json is present in your path: {}", ERROR_PATH, path);
            throw new ParserException("configuration.json is not found in your path");
        }
    }
}
