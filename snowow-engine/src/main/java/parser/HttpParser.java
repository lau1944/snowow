package parser;

import lombok.extern.slf4j.Slf4j;
import models.Controller;
import utils.Deserializer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Parsing API JSON file into Controller object
 *
 * @author liuquan
 */
@Slf4j
public class HttpParser extends BaseParser<List<Controller>> {

    private static final String ERROR_PATH = "HttpParser";
    private static final String API_FILE_POSTFIX = ".http.json";

    /**
     * @param path Directory contains API
     * @return
     * @throws IllegalArgumentException,FileNotFoundException
     */
    @Override
    public List<Controller> parse(String path) throws IllegalArgumentException, FileNotFoundException {
        if (path == null) {
            throw new IllegalArgumentException("Api files path should not be empty");
        }

        List<Controller> controllers = new ArrayList<>();
        File dir = new File(path);
        if (!dir.exists()) {
            return controllers;
        }

        if (!dir.isDirectory()) {
            throw new IllegalStateException("File structure is incorrect, please follow the instructions to create files");
        }

        for (final File file : Objects.requireNonNull(dir.listFiles())) {
            if (!file.getPath().endsWith(API_FILE_POSTFIX)) {
                continue;
            }

            Controller controller = Deserializer.deserialize(file, Controller.class);
            controllers.add(controller);
        }

        return controllers;
    }
}
