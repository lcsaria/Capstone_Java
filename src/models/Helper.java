/**
we * Java Course 4, Module 3
 * 
 * CAPSTONE PROJECT
 * Claim class 
 * 
 * @author Lmarl Saria
 * 
 * Date Created: December 20, 2022
 * 
 * 
 *  
 */

package models;

public class Helper {

    public void menu() {
        System.out.println("\nAUTOMOBILE INSURANCE POLICY AND CLAIMS ADMINISTRATION SYSTEM");
        System.out.println("[1] Create a new Customer Account");
        System.out.println("[2] Get a policy quote and buy the policy");
        System.out.println("[3] Cancel a specific policy");
        System.out.println("[4] File an accident claim against a policy");
        System.out.println("[5] Search for a Customer account ");
        System.out.println("[6] Search for and display a specific policy");
        System.out.println("[7] Search for and display a specific claim");
        System.out.println("[-1] Exit the PAS System");
        System.out.print("Enter your choice:\t");
    }

    public void delay(int second) {
        try {
            Thread.sleep(second);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void createBar(String type) {
        final int LENGTH_POLICY = 126;
        final int LENGTH_POLICY_HOLDER = 125;
        final int LENGTH_VEHICLES = 143;

        switch (type) {
            // 6.A.1 POLICY
            case "policy" -> {
                for (int count = 0; count <= LENGTH_POLICY; count++) {
                    if (count == 0 || count == LENGTH_POLICY) {
                        System.out.print("+");
                    } else {
                        System.out.print("-");
                    }
                }
                System.out.println();
            }
            // 6.A.2 POLICY HOLDER
            case "policyHolder" -> {
                for (int count = 0; count <= LENGTH_POLICY_HOLDER; count++) {
                    if (count == 0 || count == LENGTH_POLICY_HOLDER) {
                        System.out.print("+");
                    } else {
                        System.out.print("-");
                    }
                }
                System.out.println();
            }
            // 6.A.3 VEHICLES
            case "vehicle" -> {
                for (int count = 0; count <= LENGTH_VEHICLES; count++) {
                    if (count == 0 || count == LENGTH_VEHICLES) {
                        System.out.print("+");
                    } else {
                        System.out.print("-");
                    }
                }
                System.out.println();
            }
            case "customerAccount" -> {
                for (int count = 0; count <= 104; count++) {
                    if (count == 0 || count == 104) {
                        System.out.print("+");
                    } else {
                        System.out.print("-");
                    }
                }
                System.out.println();
            }
            case "customerAccount.policy" -> {
                for (int count = 0; count <= 84; count++) {
                    if (count == 0 || count == 84) {
                        System.out.print("+");
                    } else {
                        System.out.print("-");
                    }
                }
                System.out.println();
            }
            case "customerAccount.policyHolder" -> {
                for (int count = 0; count <= 121; count++) {
                    if (count == 0 || count == 121) {
                        System.out.print("+");
                    } else {
                        System.out.print("-");
                    }
                }
                System.out.println();
            }
        }
    }
}
