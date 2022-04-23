package com.vau.snowow.engine.containers;

import com.vau.snowow.engine.models.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ControllerContainer implements Container<Controller> {

    private Map<String, Class<Controller>> buffer = new HashMap<>();

    @Override
    public void push(String key, Class aClass) {
            buffer.put(Objects.requireNonNull(key), Objects.requireNonNull(aClass));
    }

    @Override
    public Object remove(String key) {
        if (!buffer.containsKey(key)) {
            throw new IllegalStateException("Remove controller with key " + key + " failed, controller container does not contain this object");
        }

        return buffer.remove(key);
    }

    @Override
    public Boolean contains(String name) {
        return buffer.containsKey(name);
    }

    @Override
    public Class<Controller> get(String name) {
        if (!contains(name)) {
            throw new IllegalStateException("Container does not contain with key " + name);
        }
        return buffer.get(name);
    }

    @Override
    public Boolean isEmpty() {
        return buffer.isEmpty();
    }

    @Override
    public void clear() {
        buffer.clear();
    }
}
