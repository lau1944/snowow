import com.vau.snowow.engine.utils.FileUtil;
import org.testng.annotations.Test;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;

public class FileTest {

    @Test
    public void rootFile() throws IOException {
        String path = FileUtil.getApplicationPath();
        System.out.println(path);
    }

    @Test
    public void testEnginePath() {
        System.out.println(FileUtil.getEnginePath());
    }
    /*
    @Test
    public void testFileToClass() throws MalformedURLException, ClassNotFoundException {
        String path = FileUtil.getRepoPath();
        System.out.println(path);
        Class tClass = FileUtil.readClassFromFile(path, "Application");
    }*/

}
