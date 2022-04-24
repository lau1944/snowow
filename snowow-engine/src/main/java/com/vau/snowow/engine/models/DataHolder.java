package com.vau.snowow.engine.models;

import com.google.gson.JsonElement;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author liuquan
 */
@Data
@AllArgsConstructor
public class DataHolder {
    /**
     * Data object type
     */
    private String type;
    /**
     * Data value
     */
    private JsonElement value;
}
