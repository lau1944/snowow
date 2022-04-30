package com.vau.snowow.engine.parser;

import com.google.gson.Gson;

/**
 * @author liuquan
 */
public abstract class BaseParser<T> implements IParser<T> {

    final Gson gson;

    public BaseParser() {
       this.gson = new Gson();
    }
}
