package fileEvents;

import java.nio.file.WatchEvent;

public interface FileSystemEventListener {
    void update(WatchEvent<?> event);
}
