package fileEvents;

import java.nio.file.Path;

public interface FileSystemEventListener {
    void update(Path file);
}
