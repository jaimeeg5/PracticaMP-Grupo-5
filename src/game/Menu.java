package game;

import java.util.Scanner;

public class Menu {
    private String title;
    private String[] options;

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
                System.out.print("[" + i + "] " + options[i]);
            }
            System.out.print("[" + n + "] Salir");
            try {
                choice = Integer.parseInt(input.nextLine());
            } catch (NumberFormatException e) {
                choice = -1;
            }
        } while (choice <= 0);
        return choice;
    }
}
