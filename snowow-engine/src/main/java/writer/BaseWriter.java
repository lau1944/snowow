package writer;

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
}
