package com.vau.snowow.engine.parser;

import java.io.FileNotFoundException;

/**
 * Use as a JSON parser
 * @author liuquan
 */
public interface IParser<T> {
    /**
     * Parse the current JSON file into java object
     *
     * @param path
     * @return
     */
    T parse(String path) throws IllegalArgumentException, FileNotFoundException;
}
