package com.vau.snowow.engine.containers;

import com.vau.snowow.engine.models.Model;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author liuquan
 */
public class ModelContainer implements Container<Model> {

    private Map<String, Class<Model>> models = new HashMap<>();

    @Override
    public void push(String key, Class<Model> modelClass) {
        models.put(Objects.requireNonNull(key), Objects.requireNonNull(modelClass));
    }

    @Override
    public Object remove(String key) {
        if (!models.containsKey(key)) {
            throw new IllegalStateException("Remove model with key " + key + " failed, model container does not contain this object");
        }

        return models.remove(key);
    }

    @Override
    public Boolean contains(String name) {
        return models.containsKey(name);
    }

    @Override
    public Class<Model> get(String name) {
        if (!contains(name)) {
            throw new IllegalStateException("Container does not contain with key " + name);
        }
        return models.get(name);
    }

    @Override
    public Boolean isEmpty() {
        return models.isEmpty();
    }

    @Override
    public void clear() {
        models.clear();
    }
}
