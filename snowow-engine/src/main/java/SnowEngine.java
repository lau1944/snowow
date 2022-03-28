import core.SnowService;

import java.util.Objects;

/**
 * Engine to start service load
 *
 * @author liuquan
 */
class SnowEngine implements SnowService {

    private static volatile SnowEngine engine;
    private SnowEngine() {}

    @Override
    public void init() {

    }

    @Override
    public String load(String path) {
        return null;
    }

    static SnowEngine getInstance() {
        synchronized(SnowEngine.class) {
            if (Objects.isNull(engine)) {
                engine = new SnowEngine();
            }
        }

        return engine;
    }
}
