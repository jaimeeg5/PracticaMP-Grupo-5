package fileEvents;

import java.io.IOException;
import java.nio.file.*;

public class FileModifyEventNotifier extends FileSystemEventNotifier {

    public FileModifyEventNotifier(String path) {
        super(path);
    }

    public FileModifyEventNotifier(Path path) {
        super(path);
    }

    protected void registerDirectory(WatchService watcher) {
        Path path = getPath();
        Path dir = path.getParent();
        try {
            dir.register(watcher, StandardWatchEventKinds.ENTRY_CREATE);
            dir.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected boolean processEvent(WatchEvent<?> event) {
        Path path = (Path) event.context();
        return path.equals(getPath());
    }
}
