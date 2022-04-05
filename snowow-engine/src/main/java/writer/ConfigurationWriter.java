package writer;

import lombok.extern.slf4j.Slf4j;
import models.Configuration;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Writer class for writing configuration into application.properties
 *
 * @author liuquan
 */
@Slf4j
public class ConfigurationWriter implements Writer<Configuration> {

    private Lock lock;
    public ConfigurationWriter() {
        this.lock = new ReentrantLock(true);
    }

    /**
     * Write configuration into application property files
     * return value-  1: success -1: error
     */
    @Override
    public int write(Configuration configuration, String targetPath) throws IOException {
        File defaultPropertyFile = new File(targetPath + File.separator + "application.properties");
        log.info(defaultPropertyFile.getPath());

        // Check configuration profiles field, if not empty, create application-{profiles}.properties file.
        String profiles = configuration.getProfiles();
        boolean hasProfiles = StringUtils.hasLength(profiles);
        File propertyFile = hasProfiles ?
                new File(targetPath + File.separator + "application-" + profiles + ".properties")
                : defaultPropertyFile;

        lock.lock();
        try {
            String configStr = decompose(configuration);
            propertyFile.createNewFile();
            // Write configuration to config file
            writeToFile(configStr, propertyFile);

            // If it has profile, modify default property file
            if (hasProfiles) {
                writeToFile("spring.profiles.active=" + profiles, defaultPropertyFile);
            }
        } catch (Exception e) {
            log.error(e.toString());
            return -1;
        } finally {
            lock.unlock();
        }

        return 1;
    }

    private void writeToFile(String content, File targetFile) throws IOException {
        FileWriter writer = new FileWriter(targetFile, false);
        writer.write(content);
        writer.close();
    }

    /**
     * Deconstruct configuration objects into Spring properties string
     *
     * @param configuration
     * @return
     */
    private String decompose(Configuration configuration) {
        return configuration.toString();
    }
}
