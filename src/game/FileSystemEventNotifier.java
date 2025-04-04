package game;

import java.net.http.WebSocket;
import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.List;

public class FileSystemEventNotifier extends Thread{
    private List<WebSocket.Listener> listeners;

    public void subscribe(FileSystemEventListener listener){

    }

    public void unsubscribe(FileSystemEventListener listener){

    }

    public void notify(Path file){

    }

    public void run(){

    }

    protected void waitForEvent(){

    }

    protected void processEvent(WatchEvent event){

    }
}
