package com.vau.snowow.engine.utils;

import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;

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
}
