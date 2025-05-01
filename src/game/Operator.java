package game;

import characters.*;

import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

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
                deleteOperator();
                break;
            case 2:
                super.logout();
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

    @Override
    public void update(Path file){

    }

    public void banUsers(){
        GameData gameData = GameData.getInstance();
        // pillar y mostrar usuarios
        // permitir eleccion de usuario y guardarlo en nick
        gameData.banUser(nick);
    }

    public void unbanUsers(){
        GameData gameData = GameData.getInstance();
        // pillar y mostrar usuarios
        // permitir eleccion de usuario y guardarlo en nick
        gameData.banUser(nick);
    }

    public void deleteOperator(){
        Scanner input2 = new Scanner(System.in);
        int option;
        do{
            Menu menu = new Menu();
            menu.setTitle("¿Seguro que quieres darte de baja?");
            String[] menuOptions = {
                    "Si",
                    "No"
            };
            option = menu.showMenu();
            if (option == 1) {
                super.dropout();
                System.out.println("Te has dado de baja con éxito");
                break;
            } else if (option == 2) {
                break;
            } else {
                System.out.println("Pulsa una opcion valida");
            }
        } while ((option < 1) || (option > 2) );
    }

    //TODO cambiar coso con todos los atributos
    public void modifyCharacter(){
        Scanner input2 = new Scanner(System.in);
        int option;
        do{
            Menu menu = new Menu();
            menu.setTitle("¿Qué personaje quieres modificar?");
            String[] menuOptions = {
                    "Vampiro",
                    "Hombre Lobo",
                    "Cazador"
            };
            option = menu.showMenu();
            if (option == 1) {
                Vampire vampire = new Vampire(100, CharacterType.Vampire);
                System.out.println("Personaje modificado con éxito");
                break;
            } else if (option == 2) {
                Werewolf werewolf = new Werewolf(2.0, 100,CharacterType.Werewolf);
                System.out.println("Personaje modificado con éxito");
                break;
            } else if (option == 3){
                Hunter hunter = new Hunter(CharacterType.Hunter);
                System.out.println("Personaje modificado con éxito");
                break;
            } else {
                System.out.println("Pulsa una opcion valida");
            }
        } while ((option < 1) || (option > 3) );
    }

    public void manageCombat(){
        Notification notification = null;
        Combat combat = new Combat(notification);
        Scanner input2 = new Scanner(System.in);
        int option;
        do{
            Menu menu = new Menu();
            menu.setTitle("¿Quieres añadir modificadores?");
            String[] menuOptions = {
                    "Si",
                    "No"
            };
            option = menu.showMenu();
            if (option == 1){
                System.out.println("Introduzca el nombre del modificador");
                String name = input2.nextLine();
                System.out.println("Introduzca el valor del modificador");
                int value = Integer.parseInt(input2.nextLine());
                System.out.println("Introduzca el tipo del modificador (Fortaleza o debilidad)");
                String type = input2.nextLine();
                Modifier modifier = new Modifier(name, value, type);
                combat.addModifier(modifier);
                break;
            } else if (option == 2) {
                break;
            }
        } while((option > 2) || (option < 1));

        do{
            Menu menu = new Menu();
            menu.setTitle("¿Quieres eliminar modificadores?");
            String[] menuOptions = {
                    "Si",
                    "No"
            };
            option = menu.showMenu();
            if (option == 1){
                List<Modifier> activeModifiers = combat.getActiveModifiers();
                for (Modifier modifier : activeModifiers){
                    menu.setTitle("¿Quieres eliminar el modificador " + modifier.getName() + "?");
                    String[] menuOpt = {
                            "Si",
                            "No"
                    };
                    int opt = menu.showMenu();
                    if (opt == 1){
                        combat.removeModifier(modifier);
                    }
                }
                break;
            } else if (option == 2) {
                break;
            }
        } while((option > 2) || (option < 1));
    }
}
