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
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class Validation {
    static Scanner scan = new Scanner(System.in);
    private String input;

    // check string with valid pattern
    public String validateString(String message, String pattern) {
        boolean isValid = false;

        while (!isValid) {
            System.out.printf("%-35s", message);
            String enter = scan.nextLine();

            if (enter.isBlank()) {
                System.out.println("EMPTY INPUT. TRY AGAIN\n");
            } else if (!enter.matches(pattern)) {
                System.out.println("INVALID INPUT. TRY AGAIN\n");
            } else {
                input = enter;
                isValid = true;
            }
        }

        return input;
    }

    // check valid date
    public String validateDate(String message, String pattern) {
        boolean isValid = false;

        while (!isValid) {
            System.out.printf("%-35s", message);
            String enter = scan.nextLine();

            if (enter.isBlank()) {
                System.out.println("EMPTY INPUT. TRY AGAIN\n");
            } else if (!enter.matches(pattern)) {
                System.out.println("INVALID INPUT. ALWAYS WRITE LEADING ZEROS. TRY AGAIN\n");
            } else {
                LocalDate dateEntered = parseDate(enter);
                if (dateEntered != null) {
                    input = String.valueOf(dateEntered);
                    isValid = true;
                } else {
                    System.out.println("INVALID DATE. TRY AGAIN\n");
                }
            }
        }

        return input;
    }

    private LocalDate parseDate(String input) {
        try {
            return LocalDate.parse(input, DateTimeFormatter.ISO_DATE);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

}
