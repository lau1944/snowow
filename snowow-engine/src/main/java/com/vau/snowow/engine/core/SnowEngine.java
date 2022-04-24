package com.vau.snowow.engine.core;

import java.io.IOException;

/**
 * Engine to start service load
 *
 * @author liuquan
 */
public interface SnowEngine {
    /**
     * Default parse method
     * @param packageName
     * @return
     * @throws IOException
     */
    String defaultParse(String packageName) throws IOException;
    /**
     * Load the configuration related JSON files
     * @param jsonPath directory path to put all the related files, default is 'snow_json'
     * @param packageName package name for your application repo, ex: com.vau.snowow.snowow.platform
     * @param outputPath output dir for classes
     * default is the same directory path as Application.java
     * @return
     */
    String parse(String jsonPath, String packageName, String outputPath) throws IOException;
}
