package game;

import java.nio.file.Path;
import java.util.List;

public abstract class User {
    private String name;
    private String nick;
    private FileSystemEventNotifier notifier;
    private List<Path> pendingNotifications;

    public void setName(String name) {
        this.name = name;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public void logout(){

    }

    public void dropout(){

    }

    public abstract void operate();

    public String getName(){
        return name;
    }

    public String getNick(){
        return nick;
    }

    public abstract void update(Path file);
    public FileSystemEventNotifier getNotifier() {
        return notifier;
    }

    public User(String nick, String name){
        this.nick = nick;
        this.name = name;
    }
}
