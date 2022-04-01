package models;

import lombok.*;

import java.util.Map;

/**
 * Spring application configuration file
 *
 * @author liuquan
 */
@Data
@Setter
@Getter
@Builder
@AllArgsConstructor
public class Configuration {
    /**
     * Application mode
     */
    @Builder.Default
    private Boolean debug = true;
    /**
     * Application name
     */
    @Builder.Default
    private String appName = "snow_app";
    /**
     * Server name
     */
    @Builder.Default
    private String port = "80";
    /**
     * Server profiles
     * ex: dev
     * Will generate application-dev.properties file
     */
    @Builder.Default
    private String profiles = "dev";
    /**
     * Addition environment variables
     */
    private Map<String, Object> env;
}
