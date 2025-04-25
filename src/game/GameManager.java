package game;

import java.util.Scanner;
import java.util.function.Predicate;

public class GameManager {

    public void start() {
        Menu menu = new Menu();
        menu.setTitle("Bienvenido a Fantasy Fight");
        String[] menuOptions = {
                "Iniciar sesión",
                "Registrarse"
        };
        menu.setOptions(menuOptions);
        int choice = menu.showMenu();
        User user;
        switch (choice) {
            case 1:
                user = login();
                if (user != null) {
                    user.operate();
                }
                break;
            case 2:
                user = register();
                if (user != null) {
                    user.operate();
                }
                break;
        }
    }

    private User login() {
        GameData data = GameData.getInstance();
        String user = waitForInput("Intoduce apodo.", data::userExists, "Este usuario no existe");
        if (data.isBanned(user)) {
            System.out.println("Usuario baneado");
            return null;
        }
        if (user.isEmpty()) {
            return null;
        }
        String password = waitForInput("Introduce contraseña.", p -> data.checkPassword(user, p), "Contraseña incorrecta");
        if (password.isEmpty()) {
            return null;
        }
        return data.getUser(user);
    }

    private User register() {
        Menu menu = new Menu();
        menu.setTitle("Elige tipo de usuario");
        String[] menuOptions = {
                "Operador",
                "Usuario"
        };
        menu.setOptions(menuOptions);
        int choice = menu.showMenu();
        UserType type;
        switch (choice) {
            case 1:
                type = UserType.OPERATOR;
                break;
            case 2:
                type = UserType.PLAYER;
                break;
            default:
                return null;
        }
        UserBuilder builder = new UserBuilder(type);
        GameData data = GameData.getInstance();
        String nick = waitForInput("Intoduce apodo.", u -> !data.userExists(u), "Este apodo ya está en uso");
        if (nick.isEmpty()) {
            return null;
        }
        builder.buildNick(nick);
        String name = waitForInput("Introduce nombre.");
        if (name.isEmpty()) {
            return null;
        }
        builder.buildName(name);
        String password = waitForInput("Introduce contraseña.");
        if (password.isEmpty()) {
            return null;
        }
        return data.newUser(type, nick, name, password);
    }

    private String waitForInput(String inputText) {
        return waitForInput(inputText, _ -> true, null);
    }

    private String waitForInput(String inputText, Predicate<String> condition, String errorText) {
        Scanner input = new Scanner(System.in);
        boolean validInput = false;
        String str;
        do {
            System.out.println(inputText + " No introduzcas nada para salir.");
            str = input.nextLine();
            if (str.isEmpty() || condition.test(str)) {
                validInput = true;
            }
            System.out.println(errorText);
        } while (!validInput);
        return str;
    }
}