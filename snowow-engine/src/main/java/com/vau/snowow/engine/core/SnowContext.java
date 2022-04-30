package com.vau.snowow.engine.core;

import com.vau.snowow.engine.containers.Container;
import com.vau.snowow.engine.containers.ControllerContainer;
import com.vau.snowow.engine.containers.ModelContainer;
import com.vau.snowow.engine.models.Configuration;
import com.vau.snowow.engine.models.Controller;
import com.vau.snowow.engine.models.Model;
import com.vau.snowow.engine.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;

import java.io.File;
import java.util.Objects;

/**
 * State of Snow engine
 * @author liuquan
 */
@Slf4j
public final class SnowContext {
    private static volatile String outputPath = "com.vau.app";
    private static volatile SnowContext context;
    private static volatile Configuration configuration;
    private static Container modelContainer = ModelContainer.newContainer();
    private static Container controllerContainer = ControllerContainer.newContainer();

    public static void onStarted() {
        //createBuildFile(OUTPUT_PATH);
    }

    public static void onFinished() {
        if (Objects.isNull(modelContainer) || Objects.isNull(controllerContainer)) {
            throw new IllegalStateException("onStarted method should be called before finished");
        }

        modelContainer.clear();
        controllerContainer.clear();
    }

    public static void setConfiguration(Configuration config) {
        configuration = config;
    }

    public static Configuration loadConfiguration() {
        if (Objects.isNull(configuration)) {
            throw new NullPointerException("Configuration object is null, please wait for config completion");
        }

        return configuration;
    }

    public static void setOutputPath(String path) {
        outputPath = path;
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static String getOutputPath() {
        return outputPath;
    }

    public static void addModel(String packageName, Model model) {
        modelContainer.push(packageName, model);
    }

    public static void addController(String name, Controller controller) {
        controllerContainer.push(name, controller);
    }

    /**
     * Check if the model class exists
     *
     * @param name
     * @return
     */
    public static Boolean containsModel(String name) {
        return modelContainer.contains(name);
    }

    /**
     * Check if controller class exists
     *
     * @param name
     * @return
     */
    public static Boolean containsController(String name) {
        return controllerContainer.contains(name);
    }

    /**
     * Get controller object
     *
     * @return
     */
    public static Controller getController(String name) {
        return (Controller) controllerContainer.get(name);
    }

    /**
     * Get Model object
     *
     * @return
     */
    public static Model getModel(String name) {
        return (Model) modelContainer.get(name);
    }

    private static void createBuildFile(String path) {
        File file = new File(path);
        if (file.exists() && file.isDirectory()) {
            for (final File subFile : file.listFiles()) {
                subFile.delete();
            }
            return;
        }

        file.mkdirs();
    }
}
