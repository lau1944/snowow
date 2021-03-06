package com.vau.snowow.engine.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Paths;

/**
 * @author liuquan
 */
public class FileUtil {
    /**
     * Get application resource
     *
     * @param resource
     * @return
     */
    public static final Resource getResource(String resource) {
        return new ClassPathResource(resource);
    }

    /**
     * Get snow engine path
     */
    public static final String getEnginePath() {
        return new FileSystemResource("").getFile().getAbsolutePath();
    }

    public static final String getEngineRepo() {
        return getEnginePath() + "/src/main/java/com/vau/snowow/engine";
    }

    /**
     * Get application path
     */
    public static final String getApplicationPath() {
        String enginePath = getEnginePath();
        String lastElement = Paths.get(enginePath).getFileName().toString();
        return enginePath.replace(lastElement, "application/src/main");
    }

    /**
     * Java code path
     */
    public static final String getRepoPath() {
        return getApplicationPath() + "/java/com/vau/app";
    }

    public static final Class<?> readClassFromFile(String path, String className) throws MalformedURLException, ClassNotFoundException {
        File file = new File(path);
        URLClassLoader loader = new URLClassLoader(new URL[]{
                file.toURI().toURL()
        });
        return loader.loadClass(className);
    }
}
