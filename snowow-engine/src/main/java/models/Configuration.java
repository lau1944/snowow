package models;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

/**
 * Spring application configuration file
 *
 * @author liuquan
 */
@Data
@Setter
@Getter
public class Configuration {
    /**
     * Application mode
     */
    private Boolean debug;
    /**
     * Application name
     */
    private String appName;
    /**
     * Server name
     */
    private String port;
    /**
     * Server profiles
     * ex: dev
     * Will generate application-dev.properties file
     */
    private String profiles;
    /**
     *
     */
    private Map<String, Object> env;
}
