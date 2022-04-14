package com.vau.snowow.engine.core;

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
     * @param packageName package name for your application repo, ex: com.vau.snowow.snowow.platform
     * default is the same directory path as Application.java
     * @return
     */
    String parse(String path, String packageName) throws IOException;
}
