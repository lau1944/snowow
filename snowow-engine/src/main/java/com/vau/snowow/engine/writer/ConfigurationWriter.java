package com.vau.snowow.engine.writer;

import com.vau.snowow.engine.models.Configuration;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;

/**
 * Writer class for writing configuration into application.properties
 *
 * @author liuquan
 */
@Slf4j
public class ConfigurationWriter extends BaseWriter<Configuration> {

    /**
     * Write configuration into application property files
     * return value-  1: success -1: error
     */
    @Override
    public int write(Configuration configuration, String targetPath, String packageName) throws IOException {
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
            writeToFile(configStr, propertyFile, false);

            // If it has profile, modify default property file
            if (hasProfiles) {
                writeToFile("spring.profiles.active=" + profiles, defaultPropertyFile, false);
            }
        } catch (Exception e) {
            log.error(e.toString());
            return -1;
        } finally {
            close();
            lock.unlock();
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
        return configuration.toString();
    }

    public static BaseWriter newWriter() {
        return new ConfigurationWriter();
    }
}
