package com.vau.app.service;

import java.util.Map;

/**
 * @author liuquan
 */
public interface IRemoteService {
    /**
     * Get method to call remote api
     * @param url
     * @param params
     * @param headers
     * @return
     */
    Object GET(String url, Map<String, Object> params, Map<String, Object> headers);
}
