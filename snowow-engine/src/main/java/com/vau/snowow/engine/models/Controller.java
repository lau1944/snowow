package com.vau.snowow.engine.models;

import lombok.*;

import java.util.List;

/**
 * sample:
 * {
 *   "name": "User",
 *   "version": "1.0",
 *   "path": "/user",
 *   "paths": [
 *     {
 *       "name": "getUserInfo",
 *       "path": "/info",
 *       "method": "GET",
 *       "header": "",
 *       "params": "",
 *       "action": "",
 *       "response": {
 *         "status": 200,
 *         "type": "application/json",
 *         "data": {
 *  *             "name": "Jimmy",
 *  *             "age": 22,
 *  *             "school": {
 *  *               "name": "NYU"
 *  *             }
 *  *           }
 *       }
 *     }
 *   ]
 * }
 *
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
    private int version;
    /**
     * Request path, global mapping
     */
    private String path;
    /**
     * API paths
     */
    private List<Path> paths;
}
