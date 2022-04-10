package writer;

import lombok.extern.slf4j.Slf4j;
import models.Controller;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * Writer class for writing controller classes
 *
 * @author liuquan
 */
@Slf4j
public class ControllerWriter extends BaseWriter<List<Controller>> {

    @Override
    public int write(List<Controller> controllers, String targetPath) throws IOException {
        if (controllers.isEmpty() || StringUtils.hasLength(targetPath)) {
            return 0;
        }

        File controllersDir = new File(targetPath + "/controllers");
        if (controllersDir.mkdirs()) {
            log.info("Controller directory was not found, controller directory is created");
        }

        for (final Controller controller : controllers) {
            File controllerFile = new File(controllersDir.getPath() + "/" + controller.getName() + "Controller.java");
            if (controllerFile.createNewFile()) {
                log.info("{} is created", controllerFile.getAbsolutePath());
            }

        }

        return 0;
    }
}
