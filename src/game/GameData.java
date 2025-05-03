package game;

import fileEvents.FileModifyEventNotifier;
import fileEvents.FileSystemEventListener;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.nio.file.*;
import java.util.*;

public class GameData implements FileSystemEventListener, Jsonable {
    public final Path gameDataPath = Paths.get("data/gameData.json");
    private static volatile GameData instance = null;
    private final Map<String, String> passwords;
    private final Set<String> bannedUsers;
    private final Map<String, String> registeredNumbers;
    private boolean updated;
    private final FileModifyEventNotifier notifier;
    private final Map<String, Integer> ranking; // TODO: añadir gente al ranking cuando tal
    private final List<String> modifiers;
    private int lastCombatId = -1;

    private GameData() {
        passwords = new HashMap<>();
        bannedUsers = new TreeSet<>();
        registeredNumbers = new HashMap<>();
        ranking = new TreeMap<>();
        notifier = new FileModifyEventNotifier(gameDataPath);
        modifiers = new ArrayList<String>();
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

    public List<String> getModifiers() {
        return modifiers;
    }

    public void addModifier(String modifier) {
        modifiers.add(modifier);
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
        JSONObject json = FileManager.load(gameDataPath);
        if (json != null) {
            fromJSONObject(json);
        }
        updated = false;
    }

    private void saveToDisk() {
        JSONObject json = toJSONObject();
        if (!Files.exists(gameDataPath)) {
            FileManager.save(gameDataPath, json);
        }
        else {
            try (FileChannel fc = FileChannel.open(gameDataPath, StandardOpenOption.WRITE)) {
                try (FileLock lock = fc.lock()) {
                    FileManager.save(gameDataPath, json);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        updated = false;
    }

    public void update(WatchEvent<?> event) {
        updated = true;
    }

    public void printRanking() {
        List<Map.Entry<String, Integer>> rankingList = new ArrayList<>(ranking.entrySet());
        rankingList.sort((u1, u2) -> Integer.compare(u2.getValue(), u1.getValue()));
        System.out.println("Ranking de usuarios:");
        for (Map.Entry<String, Integer> entry : rankingList) {
            System.out.println(entry.getKey() + " - Victorias: " + entry.getValue());
        }
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
            FileManager.createDirectory("data/notifications/" + nick);
            String num = generateRegisterNumber();
            builder.buildRegisterNumber(num);
            json.put("registerNumber", num);
            registeredNumbers.put(num, nick);
        }

        passwords.put(nick, password);
        User u = builder.build();
        ranking.put(nick, 0);

        FileManager.save("data/users/" + nick + ".json", json);

        saveToDisk();
        return u;
    }

    public void increaseVictory(String user) {
        ranking.put(user, ranking.get(user) + 1);
    }

    public User getUser(String nick) {// TODO: cargar notificaciones
        JSONObject json = FileManager.load("data/users/" + nick + ".json");
        UserType type = UserType.valueOf(json.getString("type"));
        UserBuilder builder = new UserBuilder(type);
        builder.buildNick(json.getString("nick"));
        builder.buildName(json.getString("name"));
        if (type == UserType.PLAYER) {
            builder.buildRegisterNumber(json.getString("registerNumber"));
        }
        return builder.build();
        /*sustituir return builder.build(); por User user = builder.build();

        // Cargar notificaciones del usuario
        Path userNotifPath = Paths.get("data/notifications/" + nick);

        // Limpiar las notificaciones previas
        user.getNotifications().clear();

        try {
            // Cargar todos los archivos de notificación de la carpeta
            if (Files.exists(userNotifPath) && Files.isDirectory(userNotifPath)) {
                for (Path p : Files.newDirectoryStream(userNotifPath)) {
                    if (Files.isRegularFile(p)) {
                        user.getNotifications().add(p);
                    }
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return user;
    }
         */
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

    public void printPlayers() {
        for (String player: ranking.keySet()) {
            System.out.println(player);
        }
    }

    public void deleteUser(User user) {
        if (updated) {
            loadFromDisk();
        }
        String nick = user.getNick();
        passwords.remove(nick);
        bannedUsers.remove(nick);

        FileManager.delete("data/users/" + nick + ".json");
        for (Path path: user.getNotifications()) {
            FileManager.delete(path);
        }
        FileManager.delete("data/notifications/" + nick);
        saveToDisk();
    }

    public void removeRegisterNumber(String registerNumber) {
        registeredNumbers.remove(registerNumber);
    }

    public void stopUpdates() {
        notifier.unsubscribe(this);
        if (notifier.isEmpty()) {
            notifier.interrupt();
        }
    }

    @Override
    public JSONObject toJSONObject() {
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
        JSONArray rankingArray = new JSONArray();
        for (String key: ranking.keySet()) {
            JSONArray pl =  new JSONArray();
            pl.put(0, key);
            pl.put(1, ranking.get(key));
            rankingArray.put(pl);
        }
        JSONArray modifiersArray = new JSONArray();
        for (String user: bannedUsers) {
            bannedUsersArray.put(0, user);
        }
        json.put("passwords", passwordsArray);
        json.put("bannedUsers", bannedUsersArray);
        json.put("registeredNumbers", registeredNumbersArray);
        json.put("ranking", rankingArray);
        json.put("modifiers", modifiersArray);
        json.put("lastCombatId", lastCombatId);
        return json;
    }

    @Override
    public void fromJSONObject(JSONObject json) {
        JSONArray arr = json.getJSONArray("passwords");
        for (int i = 0; i < arr.length(); i++) {
            JSONArray pw = arr.getJSONArray(i);
            passwords.put(pw.getString(0), pw.getString(1));
        }

        arr = json.getJSONArray("bannedUsers");
        bannedUsers.clear();
        for (int i = 0; i < arr.length(); i++) {
            bannedUsers.add(arr.getString(i));
        }

        arr = json.getJSONArray("registeredNumbers");
        registeredNumbers.clear();
        for (int i = 0; i < arr.length(); i++) {
            JSONArray rn = arr.getJSONArray(i);
            registeredNumbers.put(rn.getString(0), rn.getString(1));
        }

        arr = json.getJSONArray("ranking");
        ranking.clear();
        for (int i = 0; i < arr.length(); i++) {
            JSONArray pl = arr.getJSONArray(i);
            ranking.put(pl.getString(0), pl.getInt(1));
        }

        arr = json.getJSONArray("modifiers");
        modifiers.clear();
        for (int i = 0; i < arr.length(); i++) {
            modifiers.add(arr.getString(i));
        }

        lastCombatId = json.getInt("lastCombatId");
    }

    public int increaseLastCombatId() {
        if (updated) {
            loadFromDisk();
        }
        lastCombatId += 1;
        saveToDisk();
        return lastCombatId;
    }
}
