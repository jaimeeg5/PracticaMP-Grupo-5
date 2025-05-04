package game;

import fileEvents.FileSystemEventListener;
import fileEvents.FileSystemEventNotifier;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.*;
import java.util.LinkedList;
import java.util.List;

public abstract class User implements FileSystemEventListener, Jsonable {
    private String name;
    private String nick;
    private FileSystemEventNotifier notifier;
    private final List<Path> pendingNotifications;

    public void setNotifier(FileSystemEventNotifier notifier) {
        this.notifier = notifier;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNick(String nick) {
        this.nick = nick;
    }

    public void logout(){
        if (notifier != null) {
            notifier.unsubscribe(this);
            if (notifier.isEmpty()) {
                notifier.interrupt();
            }
            System.out.println(nick + " se ha desconectado.");
        }
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

    public User() {
        this.pendingNotifications = new LinkedList<>();
    }

    public User(String nick, String name){
        this.nick = nick;
        this.name = name;
        this.pendingNotifications = new LinkedList<>();
        this.setupNotifier();
    }

    public abstract void setupNotifier();

    public List<Path> getNotifications() {
        return pendingNotifications;
    }

    public void removeNotification(Path path) {
        pendingNotifications.remove(path);
        FileManager.delete(path);
    }

    @Override
    public void update(WatchEvent<?> event) {
        Path path = (Path) event.context();
        pendingNotifications.add(path);
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.put("nick", getNick());
        json.put("name", getName());
        return json;
    }

    @Override
    public void fromJSONObject(JSONObject json) {
        setNick(json.getString("nick"));
        setName(json.getString("name"));
    }

    protected void loadNotifications(String notificationsPath) {
        Path dirPath = Paths.get(notificationsPath);
        pendingNotifications.clear();
        try (DirectoryStream<Path> dir = Files.newDirectoryStream(dirPath)) {
            for (Path file: dir) {
                pendingNotifications.add(file);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
