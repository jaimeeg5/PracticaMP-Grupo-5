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
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void save(Path path, JSONObject json) {
        try {
            Files.writeString(path, json.toString(4));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void save(String path, JSONObject json) {
        save(Paths.get(path), json);
    }

    public JSONObject load(Path path) {
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

    public JSONObject load(String path) {
        return load(Paths.get(path));
    }
}
