import models.Configuration;
import org.junit.jupiter.api.Test;
import utils.FileUtil;
import writer.ConfigurationWriter;

import java.io.IOException;

public class WriterTest {

    @Test
    public void ConfigurationWriterTest() throws IOException {
        ConfigurationWriter writer = new ConfigurationWriter();
        Configuration configuration = new Configuration();
        int result = writer.write(configuration, FileUtil.getApplicationPath() + "/resources");
        assert result == 1;
    }

}
