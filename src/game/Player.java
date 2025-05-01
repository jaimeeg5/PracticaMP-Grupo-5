package game;

import java.nio.file.Path;
import characters.Character;

public class Player extends User {
    private String registerNumber;
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
    }

    private void checkGold() {
    }

    private void checkRanking() {
    }





    @Override
    public void update(Path file){

    }

    public void challengeUser(){

    }

    public void pay(Player player, int amount){
        player.goldWon += amount;
        this.goldLost -= amount;
    }

    public void registerCharacter(Character character){

    }

    public void dropoutCharacter(){

    }

    public Character getCharacter() {
        return character;
    }
}
