package com.vau.snowow.engine.models;

import lombok.*;

/**
 * Model class for model generation
 * @author liuquan
 */
@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Model {
    /**
     * Model name
     */
    private String name;
    /**
     * Fields value
     */
    private Field[] fields;
}
