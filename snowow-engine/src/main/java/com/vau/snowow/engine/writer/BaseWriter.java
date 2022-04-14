package com.vau.snowow.engine.writer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Objects;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author liuquan
 */
public abstract class BaseWriter<T> implements Writer<T> {
    public Lock lock;
    private FileWriter writer;
    private File file;
    private Boolean isAppend;
    public BaseWriter() {
        this.lock = new ReentrantLock(true);
    }

    protected void writeToFile(String content, File targetFile, Boolean append) throws IOException {
        if (targetFile != file || !append.equals(isAppend)) {
            if (Objects.nonNull(writer)) {
                writer.close();
            }
            file = targetFile;
            writer = new FileWriter(file, append);
        }
        isAppend = append;
        writer.write(content);
    }

    protected void close() throws IOException {
        if (Objects.isNull(writer)) {
            return;
        }
        writer.close();
        writer = null;
        file = null;
    }
}
