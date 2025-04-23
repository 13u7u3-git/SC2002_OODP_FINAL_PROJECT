package interfaces;

import helper.Color;
import java.util.List;
import java.util.Scanner;

public abstract class AbstractMenu implements Menu {
    protected final Scanner scanner;

    public AbstractMenu(Scanner scanner) {
        this.scanner = scanner;
    }

    public List<String> getInputsChangePassword() {
        Color.println("===========Changing Password==========", Color.CYAN);
        Color.print("Enter your old password: ", Color.GREEN);
        String oldPassword = scanner.nextLine();
        Color.print("Enter your new password: ", Color.GREEN);
        String newPassword = scanner.nextLine();
        Color.print("Confirm your new password: ", Color.GREEN);
        String confirmPassword = scanner.nextLine();
        Color.println("======================================", Color.CYAN);
        return List.of(oldPassword, newPassword, confirmPassword);
    }
}