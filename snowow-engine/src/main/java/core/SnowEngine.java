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
     * @param output output directory name, for instance, your parent directory's name for your generated controller classes directory,
     * default is the same directory path as Application.java
     * @return
     */
    String parse(String path, String output) throws IOException;
}
