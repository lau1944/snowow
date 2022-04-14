import com.vau.snowow.engine.utils.FileUtil;
import org.testng.annotations.Test;

import java.io.IOException;

public class FileTest {

    @Test
    public void rootFile() throws IOException {
        String path = FileUtil.getApplicationPath();
        System.out.println(path);
    }
}
