package game;
import characters.*;
import characters.Character;

import java.nio.file.WatchEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Operator extends User {

    private List<Character> characters = new ArrayList<>();

    public Operator(String nick, String name) {
        super(nick, name);
    }

    @Override
    public void operate(){
        Menu menu = new Menu();
        menu.setTitle("Elija una opcion:");
        String[] menuOptions = {
                "Darse de baja",
                "Salir del sistema",
                "Editar un personaje",
                "Validar y gestionar desafios",
                "Bloquear usuario",
                "Desbloquear usuario"
        };
        menu.setOptions(menuOptions);
        int choice = menu.showMenu();
        switch (choice) {
            case 1:
                if (Menu.showConfirmationMenu()) {
                    dropout();
                }
                break;
            case 2:
                logout();
                break;
            case 3:
                modifyCharacter();
                break;
            case 4:
                manageCombat();
                break;
            case 5:
                banUsers();
                break;
            case 6:
                unbanUsers();
                break;
        }
    }

    public void banUsers(){
        GameData gameData = GameData.getInstance();
        gameData.printPlayers();
        System.out.println("Elige el nick del usuario a bloquear");
        Scanner input2 = new Scanner(System.in);
        String nick = input2.nextLine();
        gameData.banUser(nick);
    }

    public void unbanUsers(){
        GameData gameData = GameData.getInstance();
        Set<String> users = gameData.getBannedUsers();
        for (String user : users){
            System.out.println(user);
        }
        System.out.println("Elige el nick del usuario a desbloquear");
        Scanner input2 = new Scanner(System.in);
        String nick = input2.nextLine();
        gameData.unbanUser(nick);
    }

    public void modifyCharacter(){
        Scanner input2 = new Scanner(System.in);
        String atribute;
        String opt;
        int num;
        int i = 0;
        for (Character character : characters){
            String characterData = "[" + i + "]" + "Tipo: " + character.getType() + " Nombre: " + character.getName() + " Salud: " + character.getHealth();
            characterData += " Poder: " + character.getPower() + " Fortalezas: " + character.getPowerUps() + " Debilidades: " + character.getWeaknesses();
            characterData += " Oro: " + character.getGold();
            System.out.println(characterData);
            i += 1;
        }
        System.out.println("¿Que personaje quieres modificar?");
        int index = Integer.parseInt(input2.nextLine());
        Character characterToModify = characters.remove(index);
        do {
            System.out.println("Elija uno de los siguientes atributos para modificarlo o escriba 'SAVE' para guardar los cambios:");
            System.out.println("Nombre, Salud, Poder, Fortalezas, Debilidades, Oro");
            atribute = input2.nextLine();
            switch(atribute){
                case "Nombre":
                    System.out.println("Introduzca el nuevo nombre");
                    characterToModify.setName(input2.nextLine());
                    break;
                case "Salud":
                    System.out.println("Introduzca la nueva salud");
                    characterToModify.setHealth(Integer.parseInt(input2.nextLine()));
                    break;
                case "Poder":
                    System.out.println("Introduzca el nuevo poder");
                    characterToModify.setPower(Integer.parseInt(input2.nextLine()));
                    break;
                case "Fotalezas":
                    System.out.println("¿'Añadir' o 'Eliminar'?");
                    opt = input2.nextLine();
                    switch(opt){
                        case "Añadir":
                            PowerUp powerUp = new PowerUp();
                            System.out.println("Introduzca el nombre de la nueva fortaleza");
                            powerUp.setName(input2.nextLine());
                            System.out.println("Introduzca el valor de la nueva fortaleza");
                            powerUp.setValue(Integer.parseInt(input2.nextLine()));
                            characterToModify.addPowerUp(powerUp);
                            break;
                        case "Eliminar":
                            int j = 0;
                            for (PowerUp powerup : characterToModify.getPowerUps()){
                                System.out.println("[" + j + "] Nombre: " + powerup.getName() + " Valor:" + powerup.getValue());
                                j += 1;
                            }
                            System.out.println("¿Cual quieres eliminar?");
                            num = Integer.parseInt(input2.nextLine());
                            characterToModify.getPowerUps().remove(num);
                            break;
                    }
                    break;
                case "Debilidades":
                    System.out.println("¿'Añadir' o 'Eliminar'?");
                    opt = input2.nextLine();
                    switch(opt){
                        case "Añadir":
                            Weakness weakness = new Weakness();
                            System.out.println("Introduzca el nombre de la nueva debilidad");
                            weakness.setName(input2.nextLine());
                            System.out.println("Introduzca el valor de la nueva debilidad");
                            weakness.setValue(Integer.parseInt(input2.nextLine()));
                            characterToModify.addWeakness(weakness);
                            break;
                        case "Eliminar":
                            int j = 0;
                            for (Weakness weaknessObj : characterToModify.getWeaknesses()){
                                System.out.println("[" + j + "] Nombre: " + weaknessObj.getName() + " Valor:" + weaknessObj.getValue());
                                j += 1;
                            }
                            System.out.println("¿Cual quieres eliminar?");
                            num = Integer.parseInt(input2.nextLine());
                            characterToModify.getWeaknesses().remove(num);
                            break;
                    }
                    break;
                case "Oro":
                    System.out.println("Inidique la nueva cantidad de oro");
                    characterToModify.setGold(Integer.parseInt(input2.nextLine()));
                    break;
            }
        } while(!atribute.equals("SAVE"));
        characters.add(characterToModify);
    }

    public void manageCombat() {
        Notification notification = null;
        Combat combat = new Combat(notification);
        int option;
        boolean choice;
        do {
            Menu menu = new Menu();
            menu.setTitle("¿Que accion quieres realizar?");
            String[] menuOptions = {
                    "Añadir modificador",
                    "Eliminar modificador"
            };
            menu.setOptions(menuOptions);
            option = menu.showMenu();
            switch (option) {
                case 1:
                    Scanner input2 = new Scanner(System.in);
                    System.out.println("Introduzca el nombre del modificador");
                    String name = input2.nextLine();
                    System.out.println("Introduzca el valor del modificador");
                    int value = Integer.parseInt(input2.nextLine());
                    System.out.println("Introduzca el tipo del modificador ('Fortaleza' o 'Debilidad')");
                    String type = input2.nextLine();
                    choice = Menu.showConfirmationMenu();
                    if (choice) {
                        Modifier modifier = new Modifier(name, value, type);
                        combat.addModifier(modifier);
                    }
                    break;
                case 2:
                    List<Modifier> activeModifiers = combat.getActiveModifiers();
                    int i = 0;
                    for (Modifier modifier : activeModifiers) {
                        System.out.println("[" + i + "] " + modifier.getName());
                        i += 1;

                    }
                    System.out.println("Escribe el número del modificador a eliminar del combate");
                    Scanner input3= new Scanner(System.in);
                    int modifierIndex = Integer.parseInt(input3.nextLine());
                    choice = Menu.showConfirmationMenu();
                    if (choice) {
                        combat.removeModifier(activeModifiers.get(modifierIndex));
                    }
                    break;
                case 3:
                    break;
                default:
                    System.out.println("Pulsa una opcion valida");
            }
        } while ((option < 1) || (option > 3));
    }

    @Override
    public void update(WatchEvent<?> event) {

    }
}
