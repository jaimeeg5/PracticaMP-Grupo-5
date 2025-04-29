package game;

import fileEvents.FileSystemEventListener;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.WatchEvent;
import java.util.*;

public class GameData implements FileSystemEventListener {
    private static volatile GameData instance = null;
    private final String path = "/gamedata";
    private final Map<String, String> passwords;
    private final Set<String> bannedUsers;
    private final Set<String> registeredNumbers;
    private boolean updated;

    private GameData() {
        passwords = new HashMap<>();
        bannedUsers = new TreeSet<>();
        registeredNumbers = new HashSet<>();
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
            updated = false;
        }
        return passwords.containsKey(user);
    }

    public boolean checkPassword(String user, String password) {
        if (updated) {
            loadFromDisk();
            updated = false;
        }
        return passwords.get(user).equals(password);
    }

    public User getUser(String user) {
        if (updated) {
            loadFromDisk();
            updated = false;
        }
        UserBuilder builder = new UserBuilder(UserType.PLAYER);
        return builder.build();
    }

    public void banUser(String user) {
        if (updated) {
            loadFromDisk();
            updated = false;
        }
        bannedUsers.add(user);
        saveToDisk();
    }

    public void unbanUser(String user) {
        if (updated) {
            loadFromDisk();
            updated = false;
        }
        bannedUsers.remove(user);
        saveToDisk();
    }

    public boolean isBanned(String user) {
        if (updated) {
            loadFromDisk();
            updated = false;
        }
        return bannedUsers.contains(user);
    }

    private void loadFromDisk() {
        JSONObject obj;
        try {
            obj = new JSONObject(new JSONTokener(new FileInputStream(path)));
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
        JSONArray arr = obj.getJSONArray("passwords");
        for (int i = 0; i < arr.length(); i++) {
            JSONArray pw = arr.getJSONArray(i);
            passwords.put(pw.getString(0), pw.getString(1));
        }
        arr = obj.getJSONArray("bannedUsers");
        for (int i = 0; i < arr.length(); i++) {
            bannedUsers.add(arr.getString(i));
        }
        arr = obj.getJSONArray("registeredNumbers");
        for (int i = 0; i< arr.length(); i++) {
            registeredNumbers.add(arr.getString(i));
        }
        updated = false;
    }

    private void saveToDisk() {
        JSONObject obj = new JSONObject();
        JSONArray passwordsArray = new JSONArray();
        for (String key: passwords.keySet()) {
            JSONArray pw =  new JSONArray();
            pw.put(0, key);
            pw.put(1, key);
            passwordsArray.put(pw);
        }
        JSONArray bannedUsersArray = new JSONArray();
        for (String user: bannedUsers) {
            bannedUsersArray.put(0, user);
        }
        JSONArray registeredNumbersArray = new JSONArray();
        for (String num: registeredNumbers) {
            registeredNumbersArray.put(0, num);
        }
        obj.put("passwords", passwordsArray);
        obj.put("bannedUsers", bannedUsersArray);
        obj.put("registeredNumbers", registeredNumbersArray);
    }

    public void update(WatchEvent<?> event) {
        updated = true;
    }

    public User newUser(UserType type, String nick, String name, String password) {
        if (updated) {
            loadFromDisk();
            updated = false;
        }
        UserBuilder builder = new UserBuilder(type);
        builder.buildNick(nick);
        builder.buildName(name);
        if (type == UserType.PLAYER) {
            String num = generateRegisterNumber();
            builder.buildRegisterNumber(num);
            registeredNumbers.add(num);
        }
        passwords.put(nick, password);
        User u = builder.build();
        JSONObject json = new JSONObject(u);
        saveToDisk();
        return u;
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
        } while (!registeredNumbers.contains(result));
        return result;
    }
}
