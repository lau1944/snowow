package com.vau.snowow.engine.writer;

import com.vau.snowow.engine.core.SnowContext;
import com.vau.snowow.engine.models.Constant;
import com.vau.snowow.engine.models.Controller;
import com.vau.snowow.engine.models.Path;
import com.vau.snowow.engine.utils.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

/**
 * Writer class for writing controller classes
 *
 * @author liuquan
 */
@Slf4j
public class ControllerWriter extends BaseWriter<List<Controller>> {

    @Override
    public int write(List<Controller> controllers, String targetPath) throws IOException {
        if (controllers.isEmpty() || !StringUtils.hasLength(targetPath)) {
            return -1;
        }

        int writeResult = 1;
        // Format package path into file path
        String packageName = Constant.ENGINE_PACKAGE_NAME + ".outputs";

        File controllersDir = new File(targetPath + "/controllers");
        if (!controllersDir.exists()) {
            controllersDir.mkdirs();
        }

        lock.lock();
        try {
            for (final Controller controller : controllers) {
                String className = controller.getName() + "Controller";
                File controllerFile = new File(controllersDir.getPath() + "/" + className + ".java");
                if (controllerFile.createNewFile()) {
                    log.info("{} is created", controllerFile.getAbsolutePath());
                }

                Map<String, Object> requestMap = new HashMap(5);
                requestMap.put("value", controller.getPath());
                ClassWriter.Annotation[] classAnnotation = new ClassWriter.Annotation[]{
                        new ClassWriter.Annotation("RestController"),
                        new ClassWriter.Annotation("RequestMapping", requestMap)
                };
                ClassWriter classWriter = new ClassWriter(controllerFile, className, Arrays.asList(classAnnotation), true);
                List<String> dependencies = new ArrayList<>(List.of(
                        "org.springframework.beans.factory.annotation.Autowired",
                        "org.springframework.web.bind.annotation.*",
                        "org.springframework.http.MediaType"
                ));

                File modelDir = new File(targetPath + "/models");
                if (modelDir.exists()) {
                    dependencies.add(packageName + ".models.*");
                }

                classWriter.wrap(packageName + ".controllers", classComponents -> {
                    for (final Path path : controller.getPaths()) {
                        Map<String, Object> methodMap = new HashMap(8);
                        // push request path
                        methodMap.put("value", path.getPath());
                        // push response type
                        methodMap.put("produces", path.getResponse().getType());
                        ClassWriter.Annotation[] methodAnnotation = new ClassWriter.Annotation[]{
                                new ClassWriter.Annotation(mapMethodIntoAnnotation(path.getMethod()), methodMap)
                        };
                        List<ClassWriter.Field> params = new ArrayList<>();
                        // Add header and params

                        ClassWriter.ClassComponents components = new ClassWriter.Method(
                                path.getName(),
                                true,
                                path.getResponse().getData().getType(),
                                Arrays.asList(methodAnnotation),
                                ""
                        );
                        classComponents.add(components);
                    }
                }, dependencies);

                // Add to container
                SnowContext.addController(packageName + "." + className);
            }
        } catch (Exception e) {
            log.error(e.toString());
            writeResult = -1;
        } finally {
            // close writer
            close();
            lock.unlock();
        }

        return writeResult;
    }

    /**
     * Convert method GET, POST into GetMapping, PostMapping
     *
     * @return
     */
    private String mapMethodIntoAnnotation(String method) {
        switch (method) {
            case "GET":
                return "GetMapping";
            case "POST":
                return "PostMapping";
            case "PUT":
                return "PutMapping";
            case "DELETE":
                return "DeleteMapping";
            default:
                throw new IllegalArgumentException("Please pass a valid API method (ex. GET, POST");
        }
    }

    public static BaseWriter newWriter() {
        return new ControllerWriter();
    }
}
