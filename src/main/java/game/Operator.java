package game;
import characters.*;
import characters.Character;
import characters.Equipment;
import characters.EquipmentType;
import fileEvents.AddFileEventNotifier;
import fileEvents.FileSystemEventNotifier;
import org.json.JSONArray;
import org.json.JSONObject;

import java.nio.file.Path;
import java.util.*;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Operator extends User {

    public Operator(String nick, String name) {
        super(nick, name);
        loadNotifications("data/notifications/admin");
    }

    public Operator() {
        super();
        loadNotifications("data/notifications/admin");
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
        String[] menuOptions = {
                "Crear un personaje",
                "Crear una armadura",
                "Crear un arma de una mano",
                "Crear un arma de dos manos",
                "Crear un minion",
                "Editar un personaje",
                "Validar y gestionar desafios",
                "Bloquear usuario",
                "Desbloquear usuario",
                "Darse de baja"
        };
        menu.setOptions(menuOptions);
        do {
            choice = menu.showMenu();
            switch (choice) {
                case 1:
                    createCharacter();
                    break;
                case 2:
                    createEquipment(EquipmentType.ARMOR);
                    break;
                case 3:
                    createEquipment(EquipmentType.ONEHANDEDWEAPON);
                    break;
                case 4:
                    createEquipment(EquipmentType.TWOHANDEDWEAPON);
                    break;
                case 5:
                    createMinion();
                    break;
                case 6:
                    modifyCharacter();
                    break;
                case 7:
                    manageCombat();
                    break;
                case 8:
                    banUsers();
                    break;
                case 9:
                    unbanUsers();
                    break;
                case 10:
                    if (Menu.showConfirmationMenu()) {
                        dropout();
                        choice = 11;
                    }
                    break;
                case 11:
                    logout();
                    break;
            }
        } while(choice != 11);
    }

    public void createCharacter(){
        GameData data = GameData.getInstance();
        Scanner input2 = new Scanner(System.in);
        String type;
        do {
            System.out.println("¿Que tipo de personaje quieres crear?");
            type = input2.nextLine();
            switch (type) {
                case "Vampiro" -> {
                    Vampire vampire = new Vampire();
                    vampire.setType(CharacterType.Vampire);
                    System.out.println("Introduzca el nombre del personaje");
                    vampire.setName(input2.nextLine());
                    System.out.println("Introduzca la edad del personaje");
                    vampire.setAge(Integer.parseInt(input2.nextLine()));
                    System.out.println("Introduzca los puntos de sangre");
                    vampire.setBloodPoints(Integer.parseInt(input2.nextLine()));
                    System.out.println("Introduce el nombre de la disciplina");
                    String name = input2.nextLine();
                    System.out.println("Introduce el ataque de la disciplina");
                    int attack = Integer.parseInt(input2.nextLine());
                    System.out.println("Introduce defensa de la disciplina");
                    int defense = Integer.parseInt(input2.nextLine());
                    Discipline discipline = new Discipline(name, attack, defense);
                    vampire.setSpecialAbility(discipline);
                    Equipment weapon = chooseWeapon();
                    vampire.setWeapons(weapon, null);
                    Equipment armor = chooseArmor();
                    vampire.setActiveArmor(armor);
                    System.out.println("¿Cuántos esbirros quieres añadir?");
                    int cantidadMinions = Integer.parseInt(input2.nextLine());
                    for (int i = 0; i < cantidadMinions; i++) {
                        createMinion();
                    }
                    System.out.println("Introduce la salud del personaje");
                    vampire.setHealth(Integer.parseInt(input2.nextLine()));
                    System.out.println("Introduce el poder del personaje");
                    vampire.setPower(Integer.parseInt(input2.nextLine()));
                    Modifier modifier = chooseModifier();
                    vampire.setModifier(modifier);
                    System.out.println("Introduce el nombre de la fortaleza");
                    String PowerUpName = input2.nextLine();
                    System.out.println("Introduce el valor de la fortaleza");
                    int PowerUpValue = Integer.parseInt(input2.nextLine());
                    PowerUp powerUp = new PowerUp(PowerUpName, PowerUpValue);
                    vampire.addPowerUp(powerUp);
                    System.out.println("Introduce el nombre de la debilidad");
                    String WeaknessName = input2.nextLine();
                    System.out.println("Introduce el valor de la debilidad");
                    int WeaknessValue = Integer.parseInt(input2.nextLine());
                    Weakness weakness = new Weakness(WeaknessName, WeaknessValue);
                    vampire.addWeakness(weakness);
                    System.out.println("Introduce el oro del personaje");
                    vampire.setGold(Integer.parseInt(input2.nextLine()));
                    data.addCharacter(vampire.getName());
                    JSONObject jsonVampire = vampire.toJSONObject();
                    FileManager.save("data/characters/" + vampire.getName() + ".json", jsonVampire);
                }
                case "Hombre Lobo" -> {
                    Werewolf werewolf = new Werewolf();
                    werewolf.setType(CharacterType.Werewolf);
                    System.out.println("Introduzca el nombre del personaje");
                    werewolf.setName(input2.nextLine());
                    System.out.println("Introduzca la rabia del personaje");
                    werewolf.setRage(Integer.parseInt(input2.nextLine()));
                    System.out.println("Introduce el nombre del don");
                    String name = input2.nextLine();
                    System.out.println("Introduce el ataque del don");
                    int attack = Integer.parseInt(input2.nextLine());
                    System.out.println("Introduce defensa del don");
                    int defense = Integer.parseInt(input2.nextLine());
                    Don don = new Don(name, attack, defense);
                    werewolf.setSpecialAbility(don);
                    System.out.println("Introduzca la altura del personaje");
                    werewolf.setHeight(Integer.parseInt(input2.nextLine()));
                    System.out.println("Introduzca el peso del personaje");
                    werewolf.setWeight(Integer.parseInt(input2.nextLine()));
                    Equipment weapon = chooseWeapon();
                    werewolf.setWeapons(weapon, null);
                    Equipment armor = chooseArmor();
                    werewolf.setActiveArmor(armor);
                    System.out.println("¿Cuántos esbirros quieres añadir?");
                    int cantidadMinions = Integer.parseInt(input2.nextLine());
                    for (int i = 0; i < cantidadMinions; i++) {
                        createMinion();
                    }
                    System.out.println("Introduce la salud del personaje");
                    werewolf.setHealth(Integer.parseInt(input2.nextLine()));
                    System.out.println("Introduce el poder del personaje");
                    werewolf.setPower(Integer.parseInt(input2.nextLine()));
                    Modifier modifier = chooseModifier();
                    werewolf.setModifier(modifier);
                    System.out.println("Introduce el nombre de la fortaleza");
                    String PowerUpName = input2.nextLine();
                    System.out.println("Introduce el valor de la fortaleza");
                    int PowerUpValue = Integer.parseInt(input2.nextLine());
                    PowerUp powerUp = new PowerUp(PowerUpName, PowerUpValue);
                    werewolf.addPowerUp(powerUp);
                    System.out.println("Introduce el nombre de la debilidad");
                    String WeaknessName = input2.nextLine();
                    System.out.println("Introduce el valor de la debilidad");
                    int WeaknessValue = Integer.parseInt(input2.nextLine());
                    Weakness weakness = new Weakness(WeaknessName, WeaknessValue);
                    werewolf.addWeakness(weakness);
                    System.out.println("Introduce el oro del personaje");
                    werewolf.setGold(Integer.parseInt(input2.nextLine()));
                    data.addCharacter(werewolf.getName());
                    JSONObject jsonWerewolf = werewolf.toJSONObject();
                    FileManager.save("data/characters/" + werewolf.getName() + ".json", jsonWerewolf);
                }
                case "Cazador" -> {
                    Hunter hunter = new Hunter();
                    hunter.setType(CharacterType.Hunter);
                    System.out.println("Introduzca el nombre del personaje");
                    hunter.setName(input2.nextLine());
                    System.out.println("Introduzca la voluntad del personaje");
                    hunter.setWillpower(Integer.parseInt(input2.nextLine()));
                    System.out.println("Introduce el nombre del talento");
                    String name = input2.nextLine();
                    System.out.println("Introduce el ataque del talento");
                    int attack = Integer.parseInt(input2.nextLine());
                    System.out.println("Introduce defensa del talento");
                    int defense = Integer.parseInt(input2.nextLine());
                    Talent talent = new Talent(name, attack, defense);
                    hunter.setSpecialAbility(talent);
                    Equipment weapon = chooseWeapon();
                    hunter.setWeapons(weapon, null);
                    Equipment armor = chooseArmor();
                    hunter.setActiveArmor(armor);
                    System.out.println("¿Cuántos esbirros quieres añadir?");
                    int cantidadMinions = Integer.parseInt(input2.nextLine());
                    for (int i = 0; i < cantidadMinions; i++) {
                        createMinion();
                    }
                    System.out.println("Introduce la salud del personaje");
                    hunter.setHealth(Integer.parseInt(input2.nextLine()));
                    System.out.println("Introduce el poder del personaje");
                    hunter.setPower(Integer.parseInt(input2.nextLine()));
                    Modifier modifier = chooseModifier();
                    hunter.setModifier(modifier);
                    System.out.println("Introduce el nombre de la fortaleza");
                    String PowerUpName = input2.nextLine();
                    System.out.println("Introduce el valor de la fortaleza");
                    int PowerUpValue = Integer.parseInt(input2.nextLine());
                    PowerUp powerUp = new PowerUp(PowerUpName, PowerUpValue);
                    hunter.addPowerUp(powerUp);
                    System.out.println("Introduce el nombre de la debilidad");
                    String WeaknessName = input2.nextLine();
                    System.out.println("Introduce el valor de la debilidad");
                    int WeaknessValue = Integer.parseInt(input2.nextLine());
                    Weakness weakness = new Weakness(WeaknessName, WeaknessValue);
                    hunter.addWeakness(weakness);
                    System.out.println("Introduce el oro del personaje");
                    hunter.setGold(Integer.parseInt(input2.nextLine()));
                    data.addCharacter(hunter.getName());
                    JSONObject jsonHunter = hunter.toJSONObject();
                    FileManager.save("data/characters/" + hunter.getName() + ".json", jsonHunter);
                }
                default -> System.out.println("El personaje debe ser: 'Vampiro', 'Hombre Lobo' o 'Cazador'");
            }
        } while(!type.equals("Vampiro") && !type.equals("Hombre Lobo") && !type.equals("Cazador"));
    }

    public void createMinion(){
        GameData data = GameData.getInstance();
        Set<String> minions = data.getMinions();
        Scanner input2 = new Scanner(System.in);
        String type;
        do {
            System.out.println("¿Que tipo de minion quieres crear?");
            type = input2.nextLine();
            if (type.equals("Humano")) {
                Human human = new Human();
                human.setType(MinionType.Human);
                System.out.println("Introduzca el nombre del esbirro");
                human.setName(input2.nextLine());
                System.out.println("Introduce la salud del esbirro");
                human.setHealth(Integer.parseInt(input2.nextLine()));
                System.out.println("Introduce la lealtad del esbirro");
                human.setLoyalty(input2.nextLine());
                data.addMinion(human.getName());
                JSONObject jsonHuman = human.toJSONObject();
                FileManager.save("data/minions/" + human.getName() + ".json", jsonHuman);
            } else if (type.equals("Ghoul")) {
                Ghoul ghoul = new Ghoul();
                ghoul.setType(MinionType.Ghoul);
                System.out.println("Introduzca el nombre del esbirro");
                ghoul.setName(input2.nextLine());
                System.out.println("Introduce la salud del esbirro");
                ghoul.setHealth(Integer.parseInt(input2.nextLine()));
                System.out.println("Introduce la dependencia del esbirro");
                ghoul.setDependency(Integer.parseInt(input2.nextLine()));
                data.addMinion(ghoul.getName());
                JSONObject jsonGhoul = ghoul.toJSONObject();
                FileManager.save("data/minions/" + ghoul.getName() + ".json", jsonGhoul);
            } else if (type.equals("Demonio")) {
                Demon demon = new Demon();
                demon.setType(MinionType.Demon);
                System.out.println("Introduzca el nombre del esbirro");
                demon.setName(input2.nextLine());
                System.out.println("Introduce la salud del esbirro");
                demon.setHealth(Integer.parseInt(input2.nextLine()));
                System.out.println("Introduce el pacto del esbirro");
                demon.setPact(input2.nextLine());
                data.addMinion(demon.getName());
                JSONObject jsonDemon = demon.toJSONObject();
                FileManager.save("data/minions/" + demon.getName() + ".json", jsonDemon);
            } else {
                System.out.println("El esbirro debe ser: 'Humano', 'Ghoul' o 'Demonio'");
            }
        }while(!type.equals("Demonio") && !type.equals("Ghoul") && !type.equals("Humano"));
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

    public Modifier chooseModifier(){
        GameData data = GameData.getInstance();
        List<String> modifiers = data.getModifiers();
        Modifier modifier = new Modifier(null, 0, "");
        if(!modifiers.isEmpty()){
            Menu menu = new Menu();
            String[] modifiersArray = modifiers.toArray(new String[0]);
            menu.setTitle("Elija el numero de modificador");
            menu.setOptions(modifiersArray);
            int choice;
            do {
                choice = menu.showMenu(false);
                if (choice <= modifiersArray.length) {
                    JSONObject JSONWeapon = FileManager.load("data/modifiers/" + modifiersArray[choice-1] + ".json");
                    modifier.fromJSONObject(JSONWeapon);
                }
            } while (choice > modifiersArray.length);
        } else {
            System.out.println("No existen modificadores. Tienes que crear uno");
            modifier = createModifier();
        }
        return modifier;
    }

    public Equipment chooseWeapon(){
        GameData data = GameData.getInstance();
        Equipment weapon = new Equipment("", 0, 0, null);
        List<String> weapons = data.getWeapons();
        if(!weapons.isEmpty()){
            Menu menu = new Menu();
            String[] weaponsArray = weapons.toArray(new String[0]);
            menu.setTitle("Elija el numero de arma");
            menu.setOptions(weaponsArray);
            int choice;
            do {
                choice = menu.showMenu(false);
                if (choice <= weaponsArray.length) {
                    JSONObject JSONWeapon = FileManager.load("data/weapons/" + weaponsArray[choice-1] + ".json");
                    weapon.fromJSONObject(JSONWeapon);
                }
            } while (choice > weaponsArray.length);
        } else {
            System.out.println("No existen armas. Tienes que crear una");
            weapon = createEquipment(EquipmentType.ONEHANDEDWEAPON);
        }
        return weapon;
    }

    public Equipment chooseArmor(){
        GameData data = GameData.getInstance();
        Equipment armor = new Equipment("", 0, 0, EquipmentType.ARMOR);
        List<String> armors = data.getArmors();
        if(!armors.isEmpty()){
            Menu menu = new Menu();
            String[] armorsArray = armors.toArray(new String[0]);
            menu.setTitle("Elija el numero de armadura");
            menu.setOptions(armorsArray);
            int choice;
            do {
                choice = menu.showMenu(false);
                if (choice <= armorsArray.length) {
                    JSONObject JSONWeapon = FileManager.load("data/armors/" + armorsArray[choice-1] + ".json");
                    armor.fromJSONObject(JSONWeapon);
                }
            } while (choice > armorsArray.length);
        } else {
            System.out.println("No existen armaduras. Tienes que crear una");
            armor = createEquipment(EquipmentType.ARMOR);
            JSONObject JSONArmor = FileManager.load("data/armors/" + armor.getName() + ".json");
            armor.fromJSONObject(JSONArmor);
        }
        return armor;
    }

    public void modifyCharacter(){
        Scanner input2 = new Scanner(System.in);
        GameData gameData = GameData.getInstance();
        Set<String> characters = gameData.getCharacters();
        String atribute;
        String opt;
        int num;
        if(!characters.isEmpty()){
            for (String character : characters){
                System.out.println(character);
            }
            System.out.println("¿Que personaje quieres modificar?");
            String characterName = input2.nextLine();
            JSONObject JSONCharacter = FileManager.load("data/characters/" + characterName + ".json");
            Character character;
            character = switch (CharacterType.valueOf(JSONCharacter.getString("type"))) {
                case Vampire -> new Vampire();
                case Werewolf -> new Werewolf();
                case Hunter -> new Hunter();
            };
            character.fromJSONObject(JSONCharacter);
            do {
                System.out.println("Elija uno de los siguientes atributos para modificarlo o escriba 'SAVE' para guardar los cambios:");
                System.out.println("Nombre, Salud, Poder, Fortalezas, Debilidades, Oro");
                atribute = input2.nextLine();
                switch(atribute){
                    case "Nombre":
                        System.out.println("Introduzca el nuevo nombre");
                        character.setName(input2.nextLine());
                        break;
                    case "Salud":
                        System.out.println("Introduzca la nueva salud");
                        character.setHealth(Integer.parseInt(input2.nextLine()));
                        break;
                    case "Poder":
                        System.out.println("Introduzca el nuevo poder");
                        character.setPower(Integer.parseInt(input2.nextLine()));
                        break;
                    case "Fortalezas":
                        System.out.println("¿'Añadir' o 'Eliminar'?");
                        opt = input2.nextLine();
                        switch(opt){
                            case "Añadir":
                                System.out.println("Introduzca el nombre de la nueva fortaleza");
                                String name = input2.nextLine();
                                System.out.println("Introduzca el valor de la nueva fortaleza");
                                int value = Integer.parseInt(input2.nextLine());
                                PowerUp powerUp = new PowerUp(name, value);
                                character.addPowerUp(powerUp);
                                break;
                            case "Eliminar":
                                int j = 0;
                                for (PowerUp powerup : character.getPowerUps()){
                                    System.out.println("[" + j + "] Nombre: " + powerup.getName() + " Valor:" + powerup.getValue());
                                    j += 1;
                                }
                                System.out.println("¿Cual quieres eliminar?");
                                num = Integer.parseInt(input2.nextLine());
                                character.getPowerUps().remove(num);
                                break;
                        }
                        break;
                    case "Debilidades":
                        System.out.println("¿'Añadir' o 'Eliminar'?");
                        opt = input2.nextLine();
                        switch(opt){
                            case "Añadir":
                                System.out.println("Introduzca el nombre de la nueva debilidad");
                                String name = input2.nextLine();
                                System.out.println("Introduzca el valor de la nueva debilidad");
                                int value = Integer.parseInt(input2.nextLine());
                                Weakness weakness = new Weakness(name, value);
                                character.addWeakness(weakness);
                                break;
                            case "Eliminar":
                                int j = 0;
                                for (Weakness weaknessObj : character.getWeaknesses()){
                                    System.out.println("[" + j + "] Nombre: " + weaknessObj.getName() + " Valor:" + weaknessObj.getValue());
                                    j += 1;
                                }
                                System.out.println("¿Cual quieres eliminar?");
                                num = Integer.parseInt(input2.nextLine());
                                character.getWeaknesses().remove(num);
                                break;
                        }
                        break;
                    case "Oro":
                        System.out.println("Indique la nueva cantidad de oro");
                        character.setGold(Integer.parseInt(input2.nextLine()));
                        break;
                }
            } while(!atribute.equals("SAVE"));
            characters.remove(characterName);
            FileManager.delete("data/characters/" + characterName + ".json");
            characters.add(character.getName());
            JSONObject json = character.toJSONObject();
            FileManager.save("data/characters/" + character.getName() + ".json", json);
            System.out.println("Personaje modificado con exito");
        } else {
            System.out.println("No existen personajes. Tienes que crear uno");
            createCharacter();
        }
    }

    public void manageCombat() {
        GameData gd = GameData.getInstance();
        Path[] notifications = getNotifications().toArray(new Path[0]);
        for (Path path: notifications) {
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
                System.out.println("Modificadores actuales: " + activeModifiers);
                choice = menu.showMenu();
                int modifierIndex;
                switch (choice) {
                    case 1:
                        Menu menuAddModifier = new Menu();
                        menuAddModifier.setTitle("Seleccione un modificador");
                        menuAddModifier.setOptions(existingModifiers);
                        do {
                            modifierIndex = menuAddModifier.showMenu();
                            if (modifierIndex <= existingModifiers.length) {
                                activeModifiers.add(existingModifiers[modifierIndex-1]);
                            }
                        } while (modifierIndex > existingModifiers.length + 1);
                        break;
                    case 2:
                        String[] activeModifiersArray = activeModifiers.toArray(new String[0]);
                        Menu menuDeleteModifier = new Menu();
                        menuDeleteModifier.setTitle("Seleccione un modificador");
                        menuDeleteModifier.setOptions(activeModifiersArray);
                        do {
                            modifierIndex = menuDeleteModifier.showMenu();
                            if (modifierIndex <= activeModifiersArray.length) {
                                activeModifiers.remove(activeModifiersArray[modifierIndex-1]);
                            }
                        } while (modifierIndex > activeModifiersArray.length + 1);
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
                        FileManager.save("data/notifications/" + challenger + "/" + System.currentTimeMillis() + ".json", challengeNotification);
                        break;
                    case 5:
                        return;
                }
            } while ((choice < 3) || (choice > 5));
            removeNotification(path);
        }
    }

    public Modifier createModifier(){
        Scanner input = new Scanner(System.in);
        System.out.println("Introduce el nombre del modificador");
        String modifierName = input.nextLine();
        System.out.println("Introduce el valor del modificador");
        int modifierValue = Integer.parseInt(input.nextLine());
        System.out.println("Introduce el tipo de modificador");
        String modifierType = input.nextLine();
        Modifier modifier = new Modifier(modifierName, modifierValue, modifierType);
        JSONObject  json = modifier.toJSONObject();
        FileManager.save("data/modifiers/" + modifierName + ".json", json);
        GameData.getInstance().addModifier(modifier.getName());
        return modifier;
    }

    public Equipment createEquipment(EquipmentType type){
        Scanner input = new Scanner(System.in);
        System.out.println("Introduce el nombre del equipamiento");
        String equipmentName = input.nextLine();
        System.out.println("Introduce el valor de ataque del equipamiento");
        int attack = Integer.parseInt(input.nextLine());
        System.out.println("Introduce el valor de defensa del equipamiento");
        int defense = Integer.parseInt(input.nextLine());
        Equipment equipment = new Equipment(equipmentName, attack, defense, type);
        JSONObject  json = equipment.toJSONObject();
        if (type == EquipmentType.ONEHANDEDWEAPON || type == EquipmentType.TWOHANDEDWEAPON){
            FileManager.save("data/weapons/" + equipmentName + ".json", json);
            GameData.getInstance().addWeapon(equipment);
        } else {
            FileManager.save("data/armors/" + equipmentName + ".json", json);
            GameData.getInstance().addArmor(equipment);
        }

        return equipment;
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
