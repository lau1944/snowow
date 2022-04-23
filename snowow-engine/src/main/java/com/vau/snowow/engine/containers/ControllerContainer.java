package com.vau.snowow.engine.containers;

import com.vau.snowow.engine.core.SnowManager;
import com.vau.snowow.engine.models.Controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author liuquan
 */
public class ControllerContainer implements Container {

    private static volatile Container container;
    private Map<String, Class<? extends Objects>> buffer = new HashMap<>();

    @Override
    public <T> void push(String key, Class<T> aClass) {
            buffer.put(Objects.requireNonNull(key), (Class<? extends Objects>) Objects.requireNonNull(aClass));
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
    public <T> Class<T> get(String name) {
        if (!contains(name)) {
            throw new IllegalStateException("Container does not contain with key " + name);
        }
        return (Class<T>) buffer.get(name);
    }

    @Override
    public Boolean isEmpty() {
        return buffer.isEmpty();
    }

    @Override
    public void clear() {
        buffer.clear();
    }

    public static Container newContainer() {
        if (Objects.nonNull(container)) {
            return container;
        }

        synchronized (ControllerContainer.class) {
            if (Objects.isNull(container)) {
                container = new ControllerContainer();
            }
        }

        return container;
    }
}
