package game;
import characters.*;
import characters.Character;

import java.nio.file.WatchEvent;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Operator extends User {
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
        Set <String> users = gameData.getPlayerSet();
        for (String user : users){
            System.out.println(user);
        }
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

    //TODO cambiar coso con todos los atributos
    public void modifyCharacter(){
        int option;
        do{
            CharacterFactory characterFactory = new CharacterFactory();
            Menu menu = new Menu();
            menu.setTitle("¿Qué personaje quieres modificar?");
            String[] menuOptions = {
                    "Vampiro",
                    "Hombre Lobo",
                    "Cazador"
            };
            menu.setOptions(menuOptions);
            option = menu.showMenu();
            // Mostrar todos los atributos de character
            switch (option) {
                case 1:
                    Vampire vampire = (Vampire) characterFactory.registerCharacter(CharacterType.Vampire);
                    System.out.println("Edad: " + vampire.getAge());
                    System.out.println("Puntos de sangre: " + vampire.getBloodPoints());
                    System.out.println("¿Que atributo quieres cambiar?");
                    Scanner input2= new Scanner(System.in);
                    String atribute = input2.nextLine();
                    switch (atribute){
                        case "Edad":
                            System.out.println("Introduzca la nueva edad");
                            Scanner input3= new Scanner(System.in);
                            int age = Integer.parseInt(input3.nextLine());
                            vampire.setAge(age);
                        case "Puntos de sangre":
                            System.out.println("Introduzca la nueva edad");
                            Scanner input4= new Scanner(System.in);
                            int bloodPoints = Integer.parseInt(input4.nextLine());
                            vampire.setBloodPoints(bloodPoints);
                    }
                    // Sustituir en lista
                    System.out.println("Personaje modificado con éxito");
                    break;
                case 2:
                    Werewolf werewolf = (Werewolf) characterFactory.registerCharacter(CharacterType.Werewolf);
                    // Mostrar atributos de vampire
                    // Preguntar cual quiere cambiar y permitir entrada de valores
                    // Cambiar valor y guardar en lista de characters disponibles
                    System.out.println("Personaje modificado con éxito");
                    break;
                case 3:
                    Hunter hunter = (Hunter) characterFactory.registerCharacter(CharacterType.Hunter);
                    // Mostrar atributos de vampire
                    // Preguntar cual quiere cambiar y permitir entrada de valores
                    // Cambiar valor y guardar en lista de characters disponibles
                    System.out.println("Personaje modificado con éxito");
                    break;
                case 4:
                    break;
                default:
                    System.out.println("Pulsa una opcion valida");
            }
        } while ((option < 1) || (option > 4));
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
