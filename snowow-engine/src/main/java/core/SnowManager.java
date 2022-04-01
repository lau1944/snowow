package core;

import lombok.extern.log4j.Log4j;
import org.apache.logging.log4j.util.Strings;
import java.util.Objects;

@Log4j
public class SnowManager implements SnowEngine {

    static volatile SnowEngine engine;

    private SnowManager(){
    }

    @Override
    public String parse(String path, String output) {
        if (Objects.isNull(path) || Strings.isEmpty(path)) {
            path = "snow_json";
        }

        if (Objects.isNull(output) || Strings.isEmpty(output)) {
            output = "outputs";
        }

    }

    static SnowEngine getInstance() {
        if (Objects.nonNull(engine)) {
            return engine;
        }

        synchronized(SnowManager.class) {
            if (Objects.isNull(engine)) {
                engine = new SnowManager();
            }
        }

        return engine;
    }
}
