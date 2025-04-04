package users;

import game.FileSystemEventNotifier;
import java.nio.file.Path;
import java.util.List;

public class User {
    private String name;
    private String nick;
    private FileSystemEventNotifier notifier;
    private List<Path> pendingNotifications;

    public void logout(){

    }

    public void dropout(){

    }

    public void operate(){

    }

    public String getName(){

        return "";
    }

    public String getNick(){

        return "";
    }

    public void update(Path file){

    }

    public FileSystemEventNotifier getNotifier() {
        return notifier;
    }

    public User(String name, String nick){

    }

    public User(){

    }
}
