package models;

import lombok.*;

import java.util.List;

/**
 * @author liuquan
 */
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Controller {
    /**
     * Name of the API
     */
    private String name;
    /**
     * API version
     */
    private String version;
    /**
     * API paths
     */
    private List<Path> paths;
}
