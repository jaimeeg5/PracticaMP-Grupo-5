package fileEvents;

import java.io.IOException;
import java.nio.file.*;

public class FileModifyEventNotifier extends FileSystemEventNotifier {
    private String dir;
    private String path;

    public FileModifyEventNotifier(String dir) {
        super(dir);
    }

    protected void registerDirectory(WatchService watcher) {
        Path path = Paths.get(getDir());
        try {
            path.register(watcher, StandardWatchEventKinds.ENTRY_MODIFY);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected boolean processEvent(WatchEvent<?> event){
        // TODO
        return true;
    }
}
