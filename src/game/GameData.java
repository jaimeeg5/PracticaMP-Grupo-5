package game;

import fileEvents.FileModifyEventNotifier;
import fileEvents.FileSystemEventListener;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.WatchEvent;
import java.util.*;

public class GameData implements FileSystemEventListener {
    public final Path gameDataPath = Paths.get("data/gameData.json");
    private static volatile GameData instance = null;
    private final Map<String, String> passwords;
    private final Set<String> bannedUsers;
    private final Map<String, String> registeredNumbers;
    private boolean updated;
    private final FileModifyEventNotifier notifier;

    private GameData() {
        FileManager.setup();
        passwords = new HashMap<>();
        bannedUsers = new TreeSet<>();
        registeredNumbers = new HashMap<>();
        notifier = new FileModifyEventNotifier(gameDataPath);
        notifier.subscribe(this);
        notifier.start();
        loadFromDisk();
    }

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

    public boolean userExists(String user) {
        if (updated) {
            loadFromDisk();
        }
        return passwords.containsKey(user);
    }

    public boolean checkPassword(String user, String password) {
        if (updated) {
            loadFromDisk();
        }
        return passwords.get(user).equals(password);
    }

    public void banUser(String user) {
        if (updated) {
            loadFromDisk();
        }
        bannedUsers.add(user);
        saveToDisk();
    }

    public void unbanUser(String user) {
        if (updated) {
            loadFromDisk();
        }
        bannedUsers.remove(user);
        saveToDisk();
    }

    public Set<String> getBannedUsers() {
        return bannedUsers;
    }

    public boolean isBanned(String user) {
        if (updated) {
            loadFromDisk();
        }
        return bannedUsers.contains(user);
    }

    private void loadFromDisk() {
        FileManager manager = new FileManager();
        JSONObject json = manager.load(gameDataPath);
        if (json != null) {
            JSONArray arr = json.getJSONArray("passwords");
            for (int i = 0; i < arr.length(); i++) {
                JSONArray pw = arr.getJSONArray(i);
                passwords.put(pw.getString(0), pw.getString(1));
            }
            arr = json.getJSONArray("bannedUsers");
            for (int i = 0; i < arr.length(); i++) {
                bannedUsers.add(arr.getString(i));
            }
            arr = json.getJSONArray("passwords");
            for (int i = 0; i < arr.length(); i++) {
                JSONArray rn = arr.getJSONArray(i);
                registeredNumbers.put(rn.getString(0), rn.getString(1));
            }
            updated = false;
        }
    }

    private void saveToDisk() {
        try (FileChannel fc = FileChannel.open(gameDataPath)) {
            try (FileLock lock = fc.lock()) {
                JSONObject json = new JSONObject();
                JSONArray passwordsArray = new JSONArray();
                for (String key: passwords.keySet()) {
                    JSONArray pw =  new JSONArray();
                    pw.put(0, key);
                    pw.put(1, passwords.get(key));
                    passwordsArray.put(pw);
                }
                JSONArray bannedUsersArray = new JSONArray();
                for (String user: bannedUsers) {
                    bannedUsersArray.put(0, user);
                }
                JSONArray registeredNumbersArray = new JSONArray();
                for (String key: registeredNumbers.keySet()) {
                    JSONArray rn =  new JSONArray();
                    rn.put(0, key);
                    rn.put(1, registeredNumbers.get(key));
                    passwordsArray.put(rn);
                }
                json.put("passwords", passwordsArray);
                json.put("bannedUsers", bannedUsersArray);
                json.put("registeredNumbers", registeredNumbersArray);

                FileManager m = new FileManager();
                m.save(gameDataPath, json);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        updated = false;
    }

    public void update(WatchEvent<?> event) {
        updated = true;
    }

    public User newUser(UserType type, String nick, String name, String password) {
        if (updated) {
            loadFromDisk();
        }

        JSONObject json = new JSONObject();
        UserBuilder builder = new UserBuilder(type);
        json.put("type", type);

        builder.buildNick(nick);
        json.put("nick", nick);

        builder.buildName(name);
        json.put("name", name);

        if (type == UserType.PLAYER) {
            String num = generateRegisterNumber();
            builder.buildRegisterNumber(num);
            json.put("registerNumber", num);
            registeredNumbers.put(num, nick);
        }

        passwords.put(nick, password);
        User u = builder.build();

        FileManager fileManager = new FileManager();
        fileManager.save("data/users" + nick + ".json", json);

        saveToDisk();
        return u;
    }

    public User getUser(String nick) {
        FileManager manager = new FileManager();
        JSONObject json = manager.load("data/users/" + nick + ".json");
        UserType type = UserType.valueOf(json.getString("type"));
        UserBuilder builder = new UserBuilder(type);
        builder.buildNick(json.getString("nick"));
        builder.buildName(json.getString("name"));
        if (type == UserType.PLAYER) {
            builder.buildRegisterNumber(json.getString("registerNumber"));
        }
        return builder.build();
    }

    private String generateRegisterNumber() {
        Random rand = new Random();
        String result;
        do {
            StringBuilder str = new StringBuilder();
            char l = (char) (rand.nextInt(26) + 'A');
            str.append(l);
            int n = rand.nextInt(100);
            if (n < 10) {
                str.append("0");
            }
            str.append(n);
            for (int i = 0; i<2;i++) {
                l = (char) (rand.nextInt(26) + 'A');
                str.append(l);
            }
            result = str.toString();
        } while (registeredNumbers.containsKey(result));
        return result;
    }

    public List<String> getUserList() {
        return registeredNumbers.values().stream().toList();
    }

    public void deleteUser(User user) {
        if (updated) {
            loadFromDisk();
        }
        String nick = user.getNick();
        passwords.remove(nick);
        bannedUsers.remove(nick);

        FileManager fm = new FileManager();
        fm.delete("data/users/" + nick + ".json");
        for (Path path: user.getNotifications()) {
            fm.delete(path);
        }
        fm.delete("data/notifications/" + nick);
        saveToDisk();
    }

    public void removeRegisterNumber(String registerNumber) {
        registeredNumbers.remove(registerNumber);
    }
}
