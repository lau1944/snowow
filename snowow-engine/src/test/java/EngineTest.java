import com.vau.snowow.engine.core.SnowEngine;
import com.vau.snowow.engine.core.SnowManager;
import org.testng.annotations.Test;

import java.io.IOException;

public class EngineTest {

    SnowEngine engine = SnowManager.getInstance();

    @Test
    public void parseTest() throws IOException, ClassNotFoundException {
        engine.parse("snow_app", "com.vau.app");
    }
}
