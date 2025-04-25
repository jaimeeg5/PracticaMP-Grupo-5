package fileEvents;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.nio.file.WatchService;
import java.nio.file.StandardWatchEventKinds;

public class AddFileEventNotifier extends FileSystemEventNotifier {
    public AddFileEventNotifier(String dir) {
        super(dir);
    }

    protected void registerDirectory(WatchService watcher) {
        Path path = Paths.get(getDir());
        try {
            path.register(watcher, StandardWatchEventKinds.ENTRY_CREATE);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected boolean processEvent(WatchEvent<?> event){
        // TODO
        return true;
    }

}
