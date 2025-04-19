package system;

import helper.Color;
import interfaces.Menu;

import java.util.Scanner;

public class LoginMenu implements Menu {

    private final SessionManager sessionManager;
    private final Scanner scanner;

    public LoginMenu(SessionManager sessionManager, Scanner scanner) {
        this.sessionManager = sessionManager;
        this.scanner = scanner;
    }

    @Override
    public void display() {
        Color.println("Login with Singpass to continue...", Color.BLUE);
    }

    @Override
    public void handleInput() {
        Color.print("Enter your NRIC:", Color.BLUE);
        String nric = scanner.nextLine();
        Color.print("Enter your password:", Color.BLUE);
        String password = scanner.nextLine();

        if (sessionManager.login(nric, password)) {
            Color.println("Login successful!", Color.GREEN);
        } else {
            Color.println("Login failed. Please check your credentials.", Color.RED);
        }
    }
}
