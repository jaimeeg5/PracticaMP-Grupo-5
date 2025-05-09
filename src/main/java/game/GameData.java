package game;

import characters.*;
import fileEvents.FileModifyEventNotifier;
import fileEvents.FileSystemEventListener;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
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
    private final Map<String, Integer> ranking;
    private final List<String> modifiers;
    private final List<String> armors;
    private final List<String> weapons;
    private int lastCombatId = -1;
    private final Set<String> characters;
    private final Set<String> minions;


    private GameData() {
        passwords = new HashMap<>();
        bannedUsers = new TreeSet<>();
        registeredNumbers = new HashMap<>();
        ranking = new TreeMap<>();
        notifier = new FileModifyEventNotifier(gameDataPath);
        modifiers = new ArrayList<>();
        armors = new ArrayList<>();
        weapons = new ArrayList<>();
        characters = new TreeSet<>();
        minions = new TreeSet<>();
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

    public List<String> getWeapons() {
        if (updated) {
            loadFromDisk();
        }
        return weapons;
    }

    public List<String> getArmors() {
        if (updated) {
            loadFromDisk();
        }
        return armors;
    }

    public List<String> getModifiers() {
        if (updated) {
            loadFromDisk();
        }
        return modifiers;
    }

    public void addModifier(String modifier) {
        if (updated) {
            loadFromDisk();
        }
        modifiers.add(modifier);
        saveToDisk();
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
        if (updated) {
            loadFromDisk();
        }
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
        } else {
            try (FileOutputStream file = new FileOutputStream(gameDataPath.toString())) {
                FileLock lock = file.getChannel().lock();
                try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(file))) {
                    writer.write(json.toString(4));
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            updated = false;
        }
    }

    public void update(WatchEvent<?> event) {
        updated = true;
    }

    public void printRanking() {
        if (updated) {
            loadFromDisk();
        }
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

        UserBuilder builder = new UserBuilder(type);

        builder.buildNick(nick);

        builder.buildName(name);

        if (type == UserType.PLAYER) {
            FileManager.createDirectory("data/notifications/" + nick);
            String num = generateRegisterNumber();
            builder.buildRegisterNumber(num);
            registeredNumbers.put(num, nick);
            ranking.put(nick, 0);
        }

        passwords.put(nick, password);
        User u = builder.build();

        FileManager.save("data/users/" + nick + ".json", u);

        saveToDisk();
        return u;
    }

    public void increaseVictory(String user) {
        if (updated) {
            loadFromDisk();
        }
        ranking.put(user, ranking.get(user) + 1);
        saveToDisk();
    }

    public User getUser(String nick) {
        if (updated) {
            loadFromDisk();
        }
        JSONObject json = FileManager.load("data/users/" + nick + ".json");
        UserType type = UserType.valueOf(json.getString("type"));
        User user = switch (type) {
            case PLAYER -> new Player();
            case OPERATOR -> new Operator();
        };
        user.fromJSONObject(json);
        return user;
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
        if (updated) {
            loadFromDisk();
        }
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
        ranking.remove(nick);

        FileManager.delete("data/users/" + nick + ".json");
        for (Path path: user.getNotifications()) {
            FileManager.delete(path);
        }
        FileManager.delete("data/notifications/" + nick);
        saveToDisk();
    }

    public void removeRegisterNumber(String registerNumber) {
        if (updated) {
            loadFromDisk();
        }
        registeredNumbers.remove(registerNumber);
        saveToDisk();
    }

    public void stopUpdates() {
        notifier.unsubscribe(this);
        if (notifier.isEmpty()) {
            notifier.interrupt();
        }
    }

    public Set<String> getCharacters() {
        if (updated) {
            loadFromDisk();
        }
        return characters;
    }

    public void addCharacter(String character) {
        if (updated) {
            loadFromDisk();
        }
        characters.add(character);
        saveToDisk();
    }

    public Set<String> getMinions() {
        if (updated) {
            loadFromDisk();
        }
        return minions;
    }

    public void addMinion(String m) {
        if (updated) {
            loadFromDisk();
        }
        minions.add(m);
        saveToDisk();
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
            registeredNumbersArray.put(rn);
        }
        JSONArray rankingArray = new JSONArray();
        for (String key: ranking.keySet()) {
            JSONArray pl =  new JSONArray();
            pl.put(0, key);
            pl.put(1, ranking.get(key));
            rankingArray.put(pl);
        }
        JSONArray modifiersArray = new JSONArray();
        for (String modifier: modifiers) {
            modifiersArray.put(modifier);
        }
        JSONArray weaponsArray = new JSONArray();
        for (String weapon: weapons) {
            weaponsArray.put(weapon);
        }
        JSONArray armorsArray = new JSONArray();
        for (String armor: armors) {
            armorsArray.put(armor);
        }
        JSONArray characterArray = new JSONArray();
        for (String c: characters) {
            characterArray.put(c);
        }
        JSONArray minionArray = new JSONArray();
        for (String c: minions) {
            minionArray.put(c);
        }
        json.put("minions", minionArray);
        json.put("characters", characterArray);
        json.put("passwords", passwordsArray);
        json.put("bannedUsers", bannedUsersArray);
        json.put("registeredNumbers", registeredNumbersArray);
        json.put("ranking", rankingArray);
        json.put("modifiers", modifiersArray);
        json.put("weapons", weaponsArray);
        json.put("armors", armorsArray);
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

        arr = json.getJSONArray("weapons");
        weapons.clear();
        for (int i = 0; i < arr.length(); i++) {
            weapons.add(arr.getString(i));
        }

        arr = json.getJSONArray("armors");
        armors.clear();
        for (int i = 0; i < arr.length(); i++) {
            armors.add(arr.getString(i));
        }
        arr = json.getJSONArray("characters");
        characters.clear();
        for (int i = 0; i < arr.length(); i++) {
            characters.add(arr.getString(i));
        }
        arr = json.getJSONArray("minions");
        minions.clear();
        for (int i = 0; i < arr.length(); i++) {
            minions.add(arr.getString(i));
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

    public void addWeapon(Equipment equipment) {
        if (updated) {
            loadFromDisk();
        }
        weapons.add(equipment.getName());
        saveToDisk();
    }

    public void addArmor(Equipment equipment) {
        if (updated) {
            loadFromDisk();
        }
        armors.add(equipment.getName());
        saveToDisk();
    }
}
