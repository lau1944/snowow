package com.vau.snowow.engine.models;

import com.google.gson.JsonElement;
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
     * Path name
     */
    private String name;
    /**
     * API path
     */
    private String path;
    /**
     * API method ex: POST, GET
     */
    private String method;
    /**
     * API action
     */
    private JsonElement action;
    /**
     * Response format
     */
    private HttpResponse response;
}
