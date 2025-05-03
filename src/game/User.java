package game;

import fileEvents.FileSystemEventListener;
import fileEvents.FileSystemEventNotifier;
import org.json.JSONObject;

import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.List;

public abstract class User implements FileSystemEventListener, Jsonable {
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
        notifier.unsubscribe(this);
        if (notifier.isEmpty()) {
            notifier.interrupt();
        }
        System.out.println(nick + " se ha desconectado.");
    }

    public void dropout(){
        logout();
        GameData data = GameData.getInstance();
        data.deleteUser(this);
        System.out.println(nick + " se ha dado de baja.");
    }

    public abstract void operate();

    public String getName(){
        return name;
    }

    public String getNick(){
        return nick;
    }

    public FileSystemEventNotifier getNotifier() {
        return notifier;
    }

    public User(String nick, String name){
        this.nick = nick;
        this.name = name;
    }

    public List<Path> getNotifications() {
        return pendingNotifications;
    }

    public void removeNotification(Path path) {
        pendingNotifications.remove(path);
    }

    @Override
    public void update(WatchEvent<?> event) {
        Path path = (Path) event;
        pendingNotifications.add(path);
    }

    @Override
    public JSONObject toJSONObject() {
        return null;
    }

    @Override
    public void fromJSONObject(JSONObject json) {

    }
}
