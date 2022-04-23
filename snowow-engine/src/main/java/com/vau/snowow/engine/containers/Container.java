package com.vau.snowow.engine.containers;

/**
 * State of generated object
 * @author liuquan
 */
public interface Container {
    /**
     * Push object into container
     * @param key class name
     * @param tClass class object
     */
    <T> void push(String key, Class<T> tClass);

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
    <T> Class<T> get(String name);

    /**
     * Check if container is empty
     */
    Boolean isEmpty();

    /**
     * Clear all objects in container
     */
    void clear();
}
