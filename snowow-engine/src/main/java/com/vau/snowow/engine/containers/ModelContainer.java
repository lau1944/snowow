package com.vau.snowow.engine.containers;

import com.vau.snowow.engine.core.SnowManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author liuquan
 */
public class ModelContainer implements Container {

    private static volatile Container container;
    private Map<String, Class<? extends Objects>> models = new HashMap<>();

    @Override
    public <T> void push(String key, Class<T> tClass) {
        models.put(Objects.requireNonNull(key), (Class<Objects>) Objects.requireNonNull(tClass));
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
    public <T> Class<T> get(String name) {
        if (!contains(name)) {
            throw new IllegalStateException("Container does not contain with key " + name);
        }
        return (Class<T>) models.get(name);
    }

    @Override
    public Boolean isEmpty() {
        return models.isEmpty();
    }

    @Override
    public void clear() {
        models.clear();
    }

    public static Container newContainer() {
        if (Objects.nonNull(container)) {
            return container;
        }

        synchronized (ModelContainer.class) {
            if (Objects.isNull(container)) {
                container = new ControllerContainer();
            }
        }

        return container;
    }
}
