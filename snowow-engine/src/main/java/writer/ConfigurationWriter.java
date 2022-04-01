package writer;

import lombok.extern.slf4j.Slf4j;
import models.Configuration;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;

@Slf4j
public class ConfigurationWriter implements Writer<Configuration> {

    @Override
    public int write(Configuration configuration, String targetPath) throws IOException {
        File defaultPropertyFile = new File(targetPath + File.pathSeparator + "application.properties");

        // Check configuration profiles field, if not empty, create application-{profiles}.properties file.
        String profiles = configuration.getProfiles();
        Boolean hasProfiles = StringUtils.hasLength(profiles);
        File propertyFile = hasProfiles ?
                new File(targetPath + File.pathSeparator + "application-" + profiles + ".properties")
                : defaultPropertyFile;

        if (propertyFile.createNewFile()) {
            // File does not exist, create a new file
        } else {
            // File does exist
        }

        return 1;
    }

    /**
     * Deconstruct configuration objects into Spring properties string
     *
     * @param configuration
     * @return
     */
    private String decompose(Configuration configuration) {

    }
}
