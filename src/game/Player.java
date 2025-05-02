package game;

import java.nio.file.WatchEvent;
import java.util.Scanner;

import characters.Character;
import characters.Hunter;
import characters.Vampire;
import characters.Werewolf;
import org.json.JSONObject;

public class Player extends User implements Jsonable{
    private final String registerNumber;
    private int goldWon;
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
        challenge.put("challenger", getNick());
        challenge.put("challenged", player);
        challenge.put("gold", gold);
        new FileManager().save("data/notifications/admin", challenge);
    }

    public void pay(Player player, int amount){
        if (amount <= 0) {
            System.out.println("La cantidad a pagar debe ser positiva.");
            return;
        }
        if (this.goldLost < amount) {
            System.out.println("No tienes suficiente oro para realizar el pago.");
            return;
        }
        player.goldWon += amount;
        this.goldLost -= amount;
        System.out.println("Pago realizado correctamente.");
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
        if (character != null) {
            character = null;
            System.out.println("Personaje dado de baja correctamente.");
        } else {
            System.out.println("No tienes un personaje para dar de baja.");
        }
    }

    public Character getCharacter() {
        return character;
    }

    @Override
    public void update(WatchEvent<?> event) {
        // TODO
        //System.out.println("Evento de actualización recibido: " + event.kind());
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
                    character = new Vampire();
                    break;
                case "Werewolf":
                    character = new Werewolf();
                    break;
                case "Hunter":
                    character = new Hunter();
                    break;
            }
            character.fromJSONObject(characterJson);
        }
    }
}
