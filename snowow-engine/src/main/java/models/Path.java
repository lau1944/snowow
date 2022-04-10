package models;

import lombok.*;

/**
 * @author liuquan
 */
@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Path {
    /**
     * API path
     */
    private String path;
    /**
     * API method ex: POST, GET
     */
    private String method;
    /**
     * Method request header
     */
    private String header;
    /**
     * Request parameters
     */
    private String params;
    /**
     * API action
     */
    private String action;
    /**
     * Response format
     */
    private HttpResponse response;
}
