package com.vau.snowow.engine.models;

import lombok.*;

/**
 * Field from Model object
 * @author liuquan
 */
@Data
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Field {
    /**
     * Field name
     */
    @NonNull
    private String name;
    /**
     * Field type
     */
    @NonNull
    private String type;
    /**
     * Nullable indicator (0 or 1)
     */
    @Builder.Default
    private Integer nullable = 1;
}