package fileEvents;

import java.io.IOException;
import java.nio.file.*;
import java.util.LinkedList;
import java.util.List;

public abstract class FileSystemEventNotifier extends Thread{
    private final List<FileSystemEventListener> listeners;
    private final Path path;

    public List<FileSystemEventListener> getListeners() {
        return listeners;
    }

    public Path getPath() {
        return path;
    }

    public FileSystemEventNotifier(String path) {
        this(Paths.get(path));
    }

    public FileSystemEventNotifier(Path path) {
        listeners = new LinkedList<>();
        this.path = path;
    }

    public void subscribe(FileSystemEventListener listener){
        listeners.add(listener);
    }

    public void unsubscribe(FileSystemEventListener listener){
        listeners.remove(listener);
    }

    public boolean isEmpty() {
        return listeners.isEmpty();
    }

    public void notify(WatchEvent<?> event) {
        for (FileSystemEventListener l: listeners) {
            l.update(event);
        }
    }

    public void run() {
        try (WatchService watcher = FileSystems.getDefault().newWatchService()) {
            registerDirectory(watcher);
            boolean valid;
            do {
                valid = waitForEvent(watcher);
            } while (valid);
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    protected abstract void registerDirectory(WatchService watcher);

    private boolean waitForEvent(WatchService watcher) {
        try {
            WatchKey key = watcher.take();
            for (WatchEvent<?> event: key.pollEvents()) {
                if (processEvent(event)) {
                    notify(event);
                }
            }
            return key.reset(); // devuelve false si key ya no es valida
        }
        catch (InterruptedException e) {
            return false;
        }
    }

    protected abstract boolean processEvent(WatchEvent<?> event);
}
