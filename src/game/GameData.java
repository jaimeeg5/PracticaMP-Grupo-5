package game;

import java.nio.file.Path;
import java.util.*;

public class GameData implements FileSystemEventListener {
    private static volatile GameData instance = null;
    private final String filename = "/gamedata";
    private final Map<String, String> passwords;
    private final Set<String> bannedUsers;
    private final Set<String> registerNumbers;

    private GameData() {
        passwords = new HashMap<>();
        bannedUsers = new TreeSet<>();
        registerNumbers = new HashSet<>();
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
        return passwords.containsKey(user);
    }

    public boolean checkPassword(String user, String password) {
        return passwords.get(user).equals(password);
    }

    public User getUser(String user) {
        UserBuilder builder = new UserBuilder(UserType.PLAYER);
        return builder.build();
    }

    public void banUser(String user) {
        bannedUsers.add(user);
        saveToDisk();
    }

    public void unbanUser(String user) {
        bannedUsers.remove(user);
        saveToDisk();
    }

    public void loadFromDisk() {
        // TODO: cargar GameData del fichero correspondiente
    }

    public void saveToDisk() {
        // TODO: guardar GameData al fichero correspondiente
    }

    public void update(Path file) {
        loadFromDisk();
    }

    public User newUser(UserType type, String nick, String name, String password) {
        UserBuilder builder = new UserBuilder(type);
        builder.buildNick(nick);
        builder.buildName(name);
        if (type == UserType.PLAYER) {
            String num = generateRegisterNumber();
            builder.buildRegisterNumber(num);
            registerNumbers.add(num);
        }
        passwords.put(nick, password);
        // TODO: save user to disk
        saveToDisk();
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
        } while (!registerNumbers.contains(result));
        return result;
    }
}
