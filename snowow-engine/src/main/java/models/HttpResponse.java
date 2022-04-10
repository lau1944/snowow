package models;

import lombok.*;

/**
 * @author liuquan
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class HttpResponse {
    /**
     * http response status
     */
    private Integer status;
    /**
     * Response type ex: application/json
     */
    private String type;
    /**
     * Optional, response data
     */
    private Object data;
}
