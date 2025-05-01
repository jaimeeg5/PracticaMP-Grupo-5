package game;

import java.nio.file.WatchEvent;

import characters.Character;

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
        String[] menuOptions = {
                "Gestionar personaje",
                "Desafiar usuario",
                "Consultar oro",
                "Consultar ranking",
                "Darse de baja"
        };
        if (character != null) {
            menuOptions[0] = "Registrar personaje";
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
                    checkRanking();
                    break;
                case 5:
                    dropout();
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
                    // TODO: copiar a Jaime
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

    private void checkRanking() {
        // TODO
    }

    public void challengeUser(){
        // TODO
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
