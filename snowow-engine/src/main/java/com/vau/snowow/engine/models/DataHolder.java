package com.vau.snowow.engine.models;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 *
 * @author liuquan
 */
@Data
@AllArgsConstructor
public class DataHolder<T> {
    /**
     * Data object type
     */
    private String type;
    /**
     * Data value
     */
    private T data;
}
