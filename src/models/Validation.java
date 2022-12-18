/**
 * 
 */
package models;

import java.time.LocalDate;
import java.util.Scanner;

/**
 * @author LmarlSaria
 *
 */
public class Validation {
    static Scanner scan = new Scanner(System.in);
    private String input;

    public String validateString(String message, String pattern) {
        boolean isValid = false;
        do {
            System.out.printf("%-35s", message);
            String enter = scan.nextLine();

            if (enter.isBlank()) {
                System.out.println("Empty input. Try again\n");
            } else if (!enter.matches(pattern)) {
                System.out.println("Invalid input. Try again\n");
            } else {
                this.input = enter;
                isValid = true;
            }
        } while (!isValid);
        return input;
    }

    // check if effective date is not later than today
    public String validateDate(String message, String pattern) {
        boolean isValid = false;

        do {
            System.out.printf("%-35s", message);
            String enter = scan.nextLine();

            if (enter.isBlank()) {
                System.out.println("Empty input. Try again\n");
            } else if (!enter.matches(pattern)) {
                System.out.println("Invalid input. Try again\n");
            } else {
                // validate Date (to be done)
                LocalDate dateEntered = LocalDate.parse(String.valueOf(enter));
                if (LocalDate.now().compareTo(dateEntered) <= 0) {
                    this.input = enter;
                    isValid = true;
                } else {
                    System.out.println("Invalid date. It should be today onwards.\n");
                }
            }
        } while (!isValid);
        return input;
    }

}
