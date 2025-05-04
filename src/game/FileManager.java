package game;

import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileManager {

    public static void setup() {
        if (!Files.exists(Paths.get("data"))) {
            try {
                Files.createDirectories(Paths.get("data/notifications/admin"));
                Files.createDirectory(Paths.get("data/users"));
                Files.createDirectory(Paths.get("data/combats"));
                Files.createDirectory(Paths.get("data/characters"));
                Files.createDirectory(Paths.get("data/modifiers"));
                Files.createDirectory(Paths.get("data/weapons"));
                Files.createDirectory(Paths.get("data/armors"));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void save(Path path, JSONObject json) {
        try {
            Files.writeString(path, json.toString(4));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void save(String path, JSONObject json) {
        save(Paths.get(path), json);
    }

    public static void save(String path, Jsonable obj) {
        save(path, obj.toJSONObject());
    }

    public static void save(Path path, Jsonable obj) {
        save(path, obj.toJSONObject());
    }

    public static JSONObject load(Path path) {
        if (Files.exists(path)) {
            try {
                String s = Files.readString(path);
                return new JSONObject(s);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        return null;
    }

    public static JSONObject load(String path) {
        return load(Paths.get(path));
    }

    public static void delete(Path path) {
        try {
            Files.delete(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void delete(String path) {
        delete(Paths.get(path));
    }

    public static void createDirectory(String path) {
        createDirectory(Paths.get(path));
    }

    public static void createDirectory(Path path) {
        try {
            Files.createDirectory(path);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
