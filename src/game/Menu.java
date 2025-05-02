package game;

import java.util.Scanner;
import java.util.function.Predicate;

public class Menu {
    private String title = "";
    private String[] options;

    public static boolean showConfirmationMenu() {
        int choice;
        Scanner input = new Scanner(System.in);
        do {
            System.out.println("¿Estas seguro?");
            System.out.println("[1] Si");
            System.out.println("[2] No");
            try {
                choice = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                choice = -1;
            }
        } while (choice != 1 && choice != 2);
        return choice == 1;
    }

    public Menu setOptions(String[] options) {
        this.options = options;
        return this;
    }

    public Menu setTitle(String title) {
        this.title = title;
        return this;
    }

    public int showMenu() {
        System.out.println(title);
        Scanner input = new Scanner(System.in);
        int choice;
        int n = options.length + 1;
        do {
            for (int i = 1; i < n; i++) {
                System.out.println("[" + i + "] " + options[i-1]);
            }
            System.out.println("[" + n + "] Salir");
            try {
                choice = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                choice = -1;
            }
        } while (choice <= 0);
        return choice;
    }

    public static String waitForInput(String inputText) {
        return waitForInput(inputText, _ -> true, null);
    }

    public static String waitForInput(String inputText, Predicate<String> condition, String errorText) {
        Scanner input = new Scanner(System.in);
        boolean validInput = false;
        String str;
        do {
            System.out.println(inputText + " No introduzcas nada para salir.");
            str = input.nextLine();
            if (str.isEmpty() || condition.test(str)) {
                validInput = true;
            }
            else {
                System.out.println(errorText);
            }
        } while (!validInput);
        return str;
    }
}
