import org.junit.jupiter.api.Test;
import utils.FileUtil;

import java.io.IOException;

public class FileTest {

    @Test
    public void rootFile() throws IOException {
        String path = FileUtil.getApplicationPath();
        System.out.println(path);
    }
}
