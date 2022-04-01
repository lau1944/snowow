package parser;

import java.io.FileNotFoundException;

/**
 * Use as a JSON parser
 */
public interface Parser {
    /**
     * Parse the current JSON file into java object
     *
     * @param path
     * @return
     */
    <T> T parse(String path) throws IllegalArgumentException, FileNotFoundException;
}
