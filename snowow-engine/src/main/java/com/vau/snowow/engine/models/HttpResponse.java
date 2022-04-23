package com.vau.snowow.engine.models;

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
     * Optional, response data
     */
    private DataHolder data;
}
