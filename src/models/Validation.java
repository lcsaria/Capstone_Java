/**
 * 
 */
package models;

import java.util.Scanner;

/**
 * @author LmarlSaria
 *
 */
public class Validation {
    static Scanner scan = new Scanner(System.in);
    private String input;

    public String validateName(String type) {
        boolean isValid = false;
        do {
            System.out.printf("%-24s", type);
            String enter = scan.nextLine();

            if (enter.isBlank()) {
                System.out.println("Empty input. Try again");
            } else if (!enter.matches("[a-zA-Z\\s]+")) {
                System.out.println("Invalid input. Try again");
            } else {
                this.input = enter;
                isValid = true;
            }
        } while (isValid != true);
        return input;
    }

    public String validateAddress() {
        boolean isValid = false;
        do {
            System.out.printf("%-24s", "Address");
            String enter = scan.nextLine();

            if (enter.isBlank()) {
                System.out.println("Empty input. Try again");
            } else if (!enter.matches("[a-zA-Z\\s]+")) {
                System.out.println("Invalid input. Try again");
            } else {
                this.input = enter;
                isValid = true;
            }
        } while (isValid != true);
        return input;
    }

    public String validateString(String type, String pattern) {
        boolean isValid = false;
        do {
            System.out.printf("%-30s", type);
            String enter = scan.nextLine();

            if (enter.isBlank()) {
                System.out.println("Empty input. Try again");
            } else if (!enter.matches(pattern)) {
                System.out.println("Invalid input. Try again");
            } else {
                this.input = enter;
                isValid = true;
            }
        } while (!isValid);
        return input;
    }

}
