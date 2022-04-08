package core;

import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Engine to start service load
 *
 * @author liuquan
 */
public interface SnowEngine {
    /**
     * Load the configuration related JSON files
     * @param path directory path to put all the related files, default is 'snow_json'
     * @param output output directory name, default is 'outputs'
     * @return
     */
    String parse(String path, String output) throws IOException;
}
