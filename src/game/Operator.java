package game;
import characters.*;
import characters.Character;
import fileEvents.AddFileEventNotifier;
import fileEvents.FileSystemEventNotifier;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Path;
import java.util.*;
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
    public void setupNotifier() {
        FileSystemEventNotifier notifier = new AddFileEventNotifier("data/notifications/admin");
        setNotifier(notifier);
        notifier.subscribe(this);
        notifier.start();
    }

    @Override
    public void operate() {
        Menu menu = new Menu();
        int choice;
        menu.setTitle("Elija una opcion:");
        do {
            String[] menuOptions = {
                    "Darse de baja",
                    "Salir del sistema",
                    "Editar un personaje",
                    "Validar y gestionar desafios",
                    "Bloquear usuario",
                    "Desbloquear usuario"
            };
            menu.setOptions(menuOptions);
            choice = menu.showMenu();
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
        } while(choice != 7);
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
        if(!characters.isEmpty()){
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
        } else {
            System.out.println("No existen personajes. Tienes que crear uno");
            System.out.println("¿Que tipo de personaje quieres crear?");
            String type = input2.nextLine();
            if (type.equals("Vampiro")){
                Vampire vampire = new Vampire();
                System.out.println("Introduzca el nombre del personaje");
                vampire.setName(input2.nextLine());
                System.out.println("Introduzca la edad del personaje");
                vampire.setAge(Integer.parseInt(input2.nextLine()));
                characters.add(vampire);
            } else if (type.equals("Werewolf")) {

            } else {

            }
        }

    }

    public void manageCombat() {
        GameData gd = GameData.getInstance();
        for (Path path: getNotifications()) {
            JSONObject notification = FileManager.load(path);
            String challenger = notification.getString("challenger");
            String challenged = notification.getString("challenged");
            int gold = notification.getInt("gold");

            System.out.println("Usuario desafiador: " + challenger);
            System.out.println("Usuario desafiado: " + challenged);
            System.out.println("Oro apostado: " + gold);
            int choice;
            List<String> activeModifiers = new LinkedList<>();
            String[] existingModifiers = gd.getModifiers().toArray(new String[0]);
            System.out.println("Modificadores actuales: " + activeModifiers);
            Menu menu = new Menu();
            menu.setTitle("¿Que accion quieres realizar?");
            String[] menuOptions = {
                    "Añadir modificador",
                    "Eliminar modificador",
                    "Aceptar desafío",
                    "Rechazar desafío"
            };
            menu.setOptions(menuOptions);
            do {
                choice = menu.showMenu();
                int modifierIndex;
                switch (choice) {
                    case 1:
                        Menu menuAddModifier = new Menu();
                        menuAddModifier.setTitle("Seleccione un modificador");
                        menuAddModifier.setOptions(existingModifiers);
                        do {
                            modifierIndex = menuAddModifier.showMenu();
                        } while (modifierIndex != existingModifiers.length + 1);
                        activeModifiers.add(existingModifiers[modifierIndex]);
                        break;
                    case 2:
                        String[] activeModifiersArray = activeModifiers.toArray(new String[0]);
                        Menu menuDeleteModifier = new Menu();
                        menuDeleteModifier.setTitle("Seleccione un modificador");
                        menuDeleteModifier.setOptions(activeModifiersArray);
                        do {
                            modifierIndex = menuDeleteModifier.showMenu();
                        } while (modifierIndex != activeModifiersArray.length + 1);
                        activeModifiers.remove(modifierIndex);
                        break;
                    case 3:
                        JSONArray arr = new JSONArray(activeModifiers);
                        notification.put("activeModifiers", arr);
                        FileManager.save("data/notifications/" + challenged + "/" + System.currentTimeMillis() + ".json", notification);
                        break;
                    case 4:
                        JSONObject challengeNotification = new JSONObject();
                        challengeNotification.put("type", NotificationType.CHALLENGE_REJECTED);
                        challengeNotification.put("by", "Un administrador");
                        challengeNotification.put("gold", 0);
                        FileManager.save("data/notifications/" + challenged + "/" + System.currentTimeMillis() + ".json", notification);
                        break;
                    case 5:
                        return;
                }
            } while ((choice < 3) || (choice > 5));
            removeNotification(path);
        }
    }

    @Override
    public JSONObject toJSONObject() {
        JSONObject json = new JSONObject();
        json.put("type", UserType.OPERATOR.toString());
        json.put("nick", getNick());
        json.put("name", getName());
        return json;
    }

    @Override
    public void fromJSONObject(JSONObject json) {
        setNick(json.getString("nick"));
        setName(json.getString("name"));
    }
}
