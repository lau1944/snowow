

import com.vau.snowow.engine.models.Configuration;
import com.vau.snowow.engine.utils.FileUtil;
import com.vau.snowow.engine.writer.ConfigurationWriter;
import com.vau.snowow.engine.writer.ModelWriter;
import org.testng.annotations.Test;

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
