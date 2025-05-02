package game;

import java.nio.file.WatchEvent;
import java.util.Scanner;

import characters.Character;
import org.json.JSONObject;

public class Player extends User {
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
        player.goldWon += amount;
        this.goldLost -= amount;
    }

    public void registerCharacter(Character character){
        // TODO
    }

    public void dropoutCharacter(){
        // TODO
    }

    public Character getCharacter() {
        return character;
    }

    @Override
    public void update(WatchEvent<?> event) {
        // TODO
    }

    @Override
    public void dropout() {
        GameData.getInstance().removeRegisterNumber(registerNumber);
        super.dropout();
    }
}
