package game;

import java.nio.file.WatchEvent;

public class FileModifyEventNotifier extends FileSystemEventNotifier{
    private String dir;
    private String path;

    public void FileModifyEvent(String dir, String filename){

    }

    protected void processEvent(WatchEvent event){

    }
}
