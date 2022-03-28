package core;

/**
 * Service method to load JSON files into target objects
 *
 * @author liuquan
 */
public interface SnowService {
    /**
     * Initialize the service engine
     */
    void init();

    /**
     * Load the JSON files into target objects
     *
     * @param path service JSON files' directories
     * @return Output the controller class file path if it contains
     */
    String load(String path);
}
