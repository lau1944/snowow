package parser;

import java.io.FileNotFoundException;

/**
 * Use as a JSON parser
 * @author liuquan
 */
public interface Parser<T> {
    /**
     * Parse the current JSON file into java object
     *
     * @param path
     * @return
     */
    T parse(String path) throws IllegalArgumentException, FileNotFoundException;
}
