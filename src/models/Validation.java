/**
  * Java Course 4, Module 3
 * 
 * CAPSTONE PROJECT
 * Validation Class
 * 
 * @author Lmarl Saria
 * 
 * Date Created: October 10, 2022
 * Date Modified: December 19, 2022
 * 
 * -- update comments
 * -- refactoring (adjustment)
 */
package models;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;


public class Validation {
    static Scanner scan = new Scanner(System.in);
    private String input;

    // check string with valid pattern
    public String validateString(String message, String pattern) {
        boolean isValid = false;
        do {
            System.out.printf("%-35s", message);
            String enter = scan.nextLine();

            if (enter.isBlank()) {
                System.out.println("EMPTY INPUT. TRY AGAIN\n");
            } else if (!enter.matches(pattern)) {
                System.out.println("INVALID INPUT. TRY AGAIN\n");
            } else {
                this.input = enter;
                isValid = true;
            }
        } while (!isValid);

        return input;
    }

    // check valid date
    public String validateDate(String message, String pattern) {
        boolean isValid = false;

        do {
            System.out.printf("%-35s", message);
            String enter = scan.nextLine();

            if (enter.isBlank()) {
                System.out.println("EMPTY INPUT. TRY AGAIN\n");
            } else if (!enter.matches(pattern)) {
                System.out.println("INVALID INPUT. ALWAYS WRITE LEADING ZEROS. TRY AGAIN\n");
            } else {
                // validate Date (to be done)
                LocalDate dateEntered = LocalDate.parse(enter, DateTimeFormatter.ISO_DATE);
                this.input = String.valueOf(dateEntered);
                isValid = true;
            }
        } while (!isValid);

        return input;
    }

}
