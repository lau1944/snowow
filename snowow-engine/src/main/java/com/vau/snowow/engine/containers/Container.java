package com.vau.snowow.engine.containers;

/**
 * State of generated object
 * @author liuquan
 */
public interface Container<T> {
    /**
     * Push object into container
     * @param key class name
     * @param tClass class object
     */
    void push(String key, T model);

    /**
     * Remove certain object with key
     * @param key
     */
    Object remove(String key);

    /**
     * Check for entry existence
     */
    Boolean contains(String name);

    /**
     * Get target class object
     */
    T get(String name);

    /**
     * Check if container is empty
     */
    Boolean isEmpty();

    /**
     * Clear all objects in container
     */
    void clear();
}
