package models;

import com.google.gson.annotations.SerializedName;
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
     * Response data type name
     */
    @SerializedName("data_type")
    private String dataType;
    /**
     * Optional, response data
     */
    private Object data;
}
