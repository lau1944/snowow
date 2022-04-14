package writer;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author liuquan
 */
public abstract class BaseWriter<T> implements Writer<T> {
    public Lock lock;
    public BaseWriter() {
        this.lock = new ReentrantLock(true);
    }

    protected void writeToFile(String content, File targetFile) throws IOException {
        FileWriter writer = new FileWriter(targetFile, false);
        writer.write(content);
        writer.close();
    }
}
