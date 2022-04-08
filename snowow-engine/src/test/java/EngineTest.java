import core.SnowEngine;
import core.SnowManager;
import org.junit.jupiter.api.Test;

import java.io.IOException;

public class EngineTest {

    SnowEngine snowEngine = SnowManager.getInstance();

    @Test
    public void parseTest() throws IOException {
        snowEngine.parse("snow_app", "outputs");
    }
}
