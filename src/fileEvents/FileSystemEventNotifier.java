package fileEvents;

import java.io.IOException;
import java.nio.file.*;
import java.util.LinkedList;
import java.util.List;

public abstract class FileSystemEventNotifier extends Thread{
    private final List<FileSystemEventListener> listeners;
    private String dir;
    private boolean stopped;

    public List<FileSystemEventListener> getListeners() {
        return listeners;
    }

    public String getDir() {
        return dir;
    }

    public FileSystemEventNotifier(String dir) {
        listeners = new LinkedList<>();
        this.dir = dir;
    }

    public void subscribe(FileSystemEventListener listener){
        listeners.add(listener);
    }

    public void unsubscribe(FileSystemEventListener listener){
        listeners.remove(listener);
    }

    public void notify(Path file){
        for (FileSystemEventListener l: listeners) {
            l.update(file);
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
                    for (FileSystemEventListener l: listeners) {
                        Path path = null;  // TODO: event -> path
                        l.update(path);
                    }
                }
            }
            return key.reset(); // devuelve false si key ya no es valida
        }
        catch (InterruptedException e) {
            System.out.print("take interrumpido");   // es normal pero puse el print para revisar que funciona como creo y tal
            return false;
        }
    }

    protected abstract boolean processEvent(WatchEvent<?> event);
}
