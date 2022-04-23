import com.vau.snowow.engine.core.SnowContext;
import com.vau.snowow.engine.utils.FileUtil;
import org.testng.annotations.Test;

public class ContainerTest {

    @Test
    public void modelAddTest() throws ClassNotFoundException {
        SnowContext.onStarted();
        SnowContext.addModel("com.vau.snowow.engine.outputs.models.User");
        SnowContext.onFinished();
    }
}
