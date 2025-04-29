package fileEvents;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.nio.file.WatchService;
import java.nio.file.StandardWatchEventKinds;

public class AddFileEventNotifier extends FileSystemEventNotifier {

    public AddFileEventNotifier(String path) {
        super(path);
    }

    protected void registerDirectory(WatchService watcher) {
        Path path = getPath();
        try {
            path.register(watcher, StandardWatchEventKinds.ENTRY_CREATE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected boolean processEvent(WatchEvent<?> event) {
        return true;
    }

}
