package game;

import java.util.Scanner;
import java.util.function.Predicate;

public class Login {
    String user;
    String password;

    private void waitForUser() {
        GameData data = GameData.getInstance();
        user = waitForInput("Intoduce apodo.", data::userExists, "Este usuario no existe");
    }

    private void waitForPassword() {
        GameData data = GameData.getInstance();
        boolean passwordOK;
        do {
            password = waitForInput("Introduce contraseña.");
            if (password.isEmpty()) {
                return;
            }
            passwordOK = data.checkPassword(user, password);
            if (!passwordOK) {
                System.out.println("Contraseña incorrecta.");
            }
        } while (!passwordOK);

    }

    protected String waitForInput(String inputText) {
        return waitForInput(inputText, s -> true, null);
    }

    protected String waitForInput(String inputText, Predicate<String> condition, String errorText) {
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

    public User operate() {
        waitForUser();
        if (user.isEmpty()) {
           return null;
        }
        waitForPassword();
        if (password.isEmpty()) {
            return null;
        }
        return GameData.getInstance().getUser(user);
    }
}
