package game;

import java.nio.file.Path;
import java.nio.file.WatchEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import characters.*;
import characters.Character;
import org.json.JSONArray;
import org.json.JSONObject;

public class Player extends User {
    private final String registerNumber;
    private int goldWon; // TODO: quitar oro cuando desafias y tal
    private int goldLost;
    private Character character;

    public Player(String nick, String name, String registerNumber) {
        super(nick, name);
        this.registerNumber = registerNumber;
    }

    @Override
    public void operate(){
        Menu menu = new Menu();
        menu.setTitle("Elige una opción");
        String[] menuOptions = {};
        if (character == null) {
            menuOptions = new String[] {
                    "Gestionar personaje",
                    "Desafiar usuario",
                    "Consultar oro",
                    "Consultar ranking",
                    "Darse de baja"
            };
        }
        menu.setOptions(menuOptions);
        int choice;
        do {
            if (!getNotifications().isEmpty()) {
                handleNotifications();
            }
            choice = menu.showMenu();
            switch (choice) {
                case 1:
                    manageCharacter();
                    break;
                case 2:
                    challengeUser();
                    break;
                case 3:
                    checkGold();
                    break;
                case 4:
                    GameData.getInstance().printRanking();
                    break;
                case 5:
                    if (Menu.showConfirmationMenu()) {
                        dropout();
                    }
                    break;
                case 6:
                    logout();
                    break;
            }
        } while (choice != 6 && choice != 5);
    }

    private void manageCharacter() {
        Menu menu = new Menu();
        menu.setTitle("Gestión de personaje");
        String[] menuOptions = {
                "Ver datos de personaje",
                "Gestionar equipamiento",
                "Dar de baja personaje"
        };
        menu.showMenu();
        int choice;
        do {
            choice = menu.showMenu();
            switch (choice) {
                case 1:
                    character.showStats();
                    break;
                case 2:
                    character.selectEquipment();
                    break;
                case 3:
                    if (Menu.showConfirmationMenu()) {
                        dropoutCharacter();
                    }
                    break;
            }
        } while (choice != 4);
    }

    private void checkGold() {
        System.out.println("Consulta de oro. Pulsa intro para volver.");
        System.out.println("- Oro ganado total: " + goldWon);
        System.out.println("- Oro perdido total: " + goldLost);
        System.out.println("- Oro del personaje actual: " + character.getGold());
    }

    public void challengeUser() {
        GameData data = GameData.getInstance();
        System.out.println("Jugadores: ");
        data.printPlayers();
        boolean correctInput = false;
        String player = "";
        while (!correctInput) {
            player = Menu.waitForInput("Escribe el nick del jugador al que quieras desafiar.",user -> (data.userExists(user) && !data.isBanned(user)), "Usuario incorrecto");
            if (player.isEmpty()) {
                return;
            }
            if (player.equals(getNick())) {
                System.out.println("No puedes desafiarte a ti mismo");
            } else {
                correctInput = true;
            }
        }
        correctInput = false;
        int gold = -1;
        Scanner input = new Scanner(System.in);
        while (!correctInput) {
            try {
                System.out.println("Introduce cantidad de oro para apostar. No introduzcas nada para salir.");
                String str = input.nextLine();
                if (str.isEmpty()) {
                    return;
                }
                gold = Integer.parseInt(str);
                if (character.getGold() < gold) {
                    System.out.println("No tienes tanto oro");
                } else if (gold <= 0) {
                    System.out.println("No puedes introducir una cantidad de oro negativa o 0");
                } else {
                    correctInput = true;
                }
            }
            catch (NumberFormatException _) {}
        }

        JSONObject challenge = new JSONObject();
        challenge.put("type", NotificationType.CHALLENGE_SENT);
        challenge.put("challenger", getNick());
        challenge.put("challenged", player);
        challenge.put("gold", gold);
        FileManager.save("data/notifications/admin/" + System.currentTimeMillis() + ".json", challenge);
    }

    public void registerCharacter(Character character){
        if (character == null) {
            System.out.println("No se puede registrar un personaje que no existe.");
            return;
        }
        this.character = character;
        System.out.println("Personaje registrado correctamente.");
    }

    public void dropoutCharacter(){
        character = null;
    }

    public Character getCharacter() {
        return character;
    }

    @Override
    public void update(WatchEvent<?> event) {
        super.update(event);
        // TODO: interrupt or some shi
    }

    private void handleNotifications() {
        for (Path path: getNotifications()) {
            JSONObject notification = FileManager.load(path);
            NotificationType type = NotificationType.valueOf(notification.getString("type"));
            switch (type) {
                case CHALLENGE_SENT:
                    challengeReceived(notification);
                    break;
                case CHALLENGE_ACCEPTED:
                    combatReceived(notification);
                    break;
                case CHALLENGE_REJECTED:
                    challengeRejected(notification);
                    break;
                case BANNED:
                    break;
            }
        }
    }

    private void challengeRejected(JSONObject notification) {
        int gold = notification.getInt("gold");
        goldWon += gold;
        character.setGold(character.getGold() + gold);
        System.out.println(notification.getString("by") + " ha rechazado tu desafío");
    }

    private void combatReceived(JSONObject notification) {

    }

    private void challengeReceived(JSONObject notification) {
        String challenger = notification.getString("challenger");
        int gold = Math.min(notification.getInt("gold"), character.getGold());
        JSONArray modifiersArray = notification.getJSONArray("activeModifiers");
        List<String> activeModifiers = new ArrayList<>();
        for (int i = 0; i < modifiersArray.length(); i++) {
            activeModifiers.add(modifiersArray.getString(i));
        }
        System.out.println("Has sido desafiado por " + challenger);
        System.out.println("Oro apostado: " + gold);
        JSONObject combatNotification = new JSONObject();
        if (Menu.showConfirmationMenu("¿Aceptas el desafio?")) {
            GameData data = GameData.getInstance();
            Combat combat = new Combat((Player) data.getUser(challenger), this, activeModifiers);
            combat.fight();
            String winner = combat.getWinner();
            if (!winner.equals("Empate")) {
                if (winner.equals(getNick())) {
                    goldWon += gold;
                    character.setGold(character.getGold() + gold);
                    data.increaseVictory(getNick());
                }
                else {
                    goldLost += gold;
                    character.setGold(character.getGold() - gold);
                    data.increaseVictory(challenger);
                }
                FileManager.save("data/users/" + getNick() + ".json", this);
            }
            FileManager.save("data/combat/" + combat.getId() + ".json", combat);

            combatNotification.put("type", NotificationType.CHALLENGE_ACCEPTED);
            combatNotification.put("combat_id", combat.getId());
        }
        else {
            gold /= 10;
            goldLost += gold;
            character.setGold(character.getGold() - gold);
            combatNotification.put("type", NotificationType.CHALLENGE_REJECTED);
            combatNotification.put("by", getNick());
            combatNotification.put("gold", gold);
        }
        FileManager.save("data/notifications/" + challenger + "/" + System.currentTimeMillis() + ".json", combatNotification);
    }

    @Override
    public void dropout() {
        GameData.getInstance().removeRegisterNumber(registerNumber);
        super.dropout();
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.put("nick", getNick());
        json.put("name", getName());
        json.put("type", "PLAYER");
        json.put("registerNumber", registerNumber);
        json.put("goldWon", goldWon);
        json.put("goldLost", goldLost);

        if (character != null) {
            json.put("character", character.toJSONObject());  // Serializamos el character
        }

        return json;
    }

    @Override
    public void fromJSONObject(JSONObject json) {
        setNick(json.getString("nick"));
        setName(json.getString("name"));
        goldWon = json.getInt("goldWon");
        goldLost = json.getInt("goldLost");

        // Cargar character desde el JSON
        if (json.has("character")) {
            JSONObject characterJson = json.getJSONObject("character");
            String type = characterJson.getString("type");
            switch (type) {
                case "Vampire":
                    character = new Vampire(150, CharacterType.Vampire);
                    break;
                case "Werewolf":
                    character = new Werewolf(1.90, 100, CharacterType.Werewolf);
                    break;
                case "Hunter":
                    character = new Hunter(CharacterType.Hunter);
                    break;
            }
            character.fromJSONObject(characterJson);
        }
    }
}
