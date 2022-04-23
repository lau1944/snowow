package com.vau.snowow.engine.core;

import com.vau.snowow.engine.containers.ControllerContainer;
import com.vau.snowow.engine.containers.ModelContainer;
import com.vau.snowow.engine.models.Controller;
import com.vau.snowow.engine.models.Model;
import com.vau.snowow.engine.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.util.Objects;

/**
 * State of Snow engine
 * @author liuquan
 */
@Slf4j
public final class SnowContext {

    private static final String OUTPUT_PATH = FileUtil.getEngineRepo() + "/outputs";
    private static volatile SnowContext context;
    private static ModelContainer modelContainer;
    private static ControllerContainer controllerContainer;

    public static void onStarted() {
        modelContainer = new ModelContainer();
        controllerContainer = new ControllerContainer();

        createBuildFile(OUTPUT_PATH);
    }

    public static void onFinished() {
        if (Objects.isNull(modelContainer) || Objects.isNull(controllerContainer)) {
            throw new IllegalStateException("onStarted method should be called before finished");
        }

        modelContainer.clear();
        controllerContainer.clear();
    }

    public static String getOutputPath() {
        return OUTPUT_PATH;
    }

    public static void addModel(String name) throws ClassNotFoundException {
        Class modelClass = Class.forName(name);
        modelContainer.push(name, modelClass);
    }

    public static void addController(String name) throws ClassNotFoundException {
        Class controllerClass = Class.forName(name);
        controllerContainer.push(name, controllerClass);
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
     * Get controller class object
     *
     * @return
     */
    public static Class<Controller> getControllerClass(String name) {
        return controllerContainer.get(name);
    }

    /**
     * Get Model class object
     *
     * @return
     */
    public static Class<Model> getModelClass(String name) {
        return modelContainer.get(name);
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
