package game;

public class GameManager {

    public void start() {
        Menu menu = new Menu();
        menu.setTitle("Bienvenido a Fantasy Fight");
        String[] menuOptions = {
                "Iniciar sesión",
                "Registrarse"
        };
        menu.setOptions(menuOptions);
        int choice;
        User user;
        do {
            choice = menu.showMenu();
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
        } while (choice != 3);
        GameData.getInstance().stopUpdates();
    }

    private User login() {
        GameData data = GameData.getInstance();
        String user = Menu.waitForInput("Intoduce apodo.", data::userExists, "Este usuario no existe");
        if (data.isBanned(user)) {
            System.out.println("Usuario baneado");
            return null;
        }
        if (user.isEmpty()) {
            return null;
        }
        String password = Menu.waitForInput("Introduce contraseña.", p -> data.checkPassword(user, p), "Contraseña incorrecta");
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
        String nick = Menu.waitForInput("Intoduce apodo.", u -> !data.userExists(u), "Este apodo ya está en uso");
        if (nick.isEmpty()) {
            return null;
        }
        builder.buildNick(nick);
        String name = Menu.waitForInput("Introduce nombre.");
        if (name.isEmpty()) {
            return null;
        }
        builder.buildName(name);
        String password = Menu.waitForInput("Introduce contraseña.");
        if (password.isEmpty()) {
            return null;
        }
        return data.newUser(type, nick, name, password);
    }


}