package com.vau.snowow.engine.writer;

import java.io.IOException;

/**
 * Method for writing file
 *
 * @author liuquan
 */
public interface IWriter<T> {
    /**
     * Write string into target file
     * @param obj object elements for the file properties
     * @param targetPath target output file
     * @return
     */
    int write(T obj, String targetPath, String packageName) throws IOException, ClassNotFoundException;
}
