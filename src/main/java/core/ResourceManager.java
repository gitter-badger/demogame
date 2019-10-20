package core;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;

/**
 * The ResourceManager is the resource store where to load all needed resources.
 * 
 * Sample usage : ```Java // in a next version you will be able to // provide a
 * listener to implement for example // a Gauge to track loading status.
 * ResourceManager.AddListener(new MyListener()); // at initialization time :
 * ResourceManager.add("MyFile.json"); ResourceManager.add("image.png"); //
 * later to use resource : String str =
 * ResourceManager.getString("MyFile.json"); BufferedImage img =
 * ResourceManager.getString("image.png"); ```
 */
public class ResourceManager {

    private static ResourceManager instance = new ResourceManager();

    public Map<String, Object> resources = new ConcurrentHashMap<>();

    public static BufferedImage getImage(String path) {
        return (BufferedImage) instance.resources.get(path);
    }

    public static String getString(String path) {
        return (String) instance.resources.get(path);
    }

    public static void add(String[] paths) {
        for (String path : paths) {
            add(path);
        }
    }

    public static void add(String path) {
        try {
            if (path.endsWith(".jpg") || path.contains(".png")) {
                BufferedImage o;
                o = ImageIO.read(instance.getClass().getResourceAsStream(path));
                if (o != null) {
                    instance.resources.put(path, o);
                }
            }
            if (path.contains(".json")) {
                InputStream stream = ResourceManager.class.getResourceAsStream(path);
                String json = new BufferedReader(new InputStreamReader(stream)).lines().parallel()
                        .collect(Collectors.joining("\n"));
                if (json != null && !json.equals("")) {
                    instance.resources.put(path, json);
                }
            }
        } catch (IOException e) {
            System.out.println("Unable to read the resource :" + path + ":" + e.getMessage());
        }
    }

    public static void remove(String path) {
        if (instance.resources.containsKey(path)) {
            instance.resources.remove(path);
        }
    }

    public static void dispose() {
        instance.resources.clear();
    }
}