package game;

import users.User;

import java.nio.file.Path;
import java.util.Map;
import java.util.Set;

public class GameData {
    private static  volatile GameData instance = null;
    private String dir;
    private String filename;
    private Map<String, String> passwords;
    private Set<String> bannedUsers;

    private GameData() { }

    public static GameData getInstance() {
        if (instance == null) {
            synchronized(GameData.class) {
                if (instance == null) {
                    instance = new GameData();
                }
            }
        }
        return instance;
    }

    public void addUser(User user, String password) {

    }

    public boolean checkPassword() {
        return false;
    }

    public User getUser(String user) {
        return new User();
    }

    public void banUser(String user) {

    }

    public void unbanUser(String user) {

    }

    public void loadFromDisk() {

    }

    public void saveToDisk() {

    }

    public void update(Path file) {

    }
}
