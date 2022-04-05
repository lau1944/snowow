package models;

import lombok.*;

import java.util.Map;
import java.util.Objects;
import java.util.Set;

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
@NoArgsConstructor
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

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("debug=");
        builder.append(debug.toString());
        builder.append("\n");

        builder.append("spring.application.name=");
        builder.append(appName);
        builder.append("\n");

        builder.append("server.port=");
        builder.append(port);
        builder.append("\n");

        if (Objects.nonNull(env)) {
            Set<Map.Entry<String, Object>> envSet = env.entrySet();
            for (Map.Entry<String, Object> entry : envSet) {
                builder.append(entry.getKey()).append("=");
                builder.append(entry.getValue());
                builder.append("\n");
            }
        }
        return builder.toString();
    }
}
