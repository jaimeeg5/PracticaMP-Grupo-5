package game;

import java.util.Scanner;

public class Menu {
    private String title;
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

    public void setOptions(String[] options) {
        this.options = options;
    }

    public void setTitle(String title) {
        this.title = title;
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
}
