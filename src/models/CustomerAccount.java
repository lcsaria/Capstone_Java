/**
  * Java Course 4, Module 3
 * 
 * CAPSTONE PROJECT
 * Customer Account Class
 * 
 * @author Lmarl Saria
 * 
 * Date Created: June 28, 2022
 * Date Modified: December 19, 2022
 * 
 * -- add database implementation
 * -- update comments
 * -- refactoring (additional user validation, fixed errors in SQL)
 * -- enhanced switch (JAVA 17)
 * -- additional display for search account (5.5 and 5.6)
 * -- using conditional formatting
 */

package models;

import java.sql.*;
import java.util.*;

public class CustomerAccount {
    static Validation valid = new Validation();
    static Connection connection = null;
    static Scanner scan = new Scanner(System.in);
    static Helper put = new Helper();
    private String accountNumber;
    private String lastName;
    private String firstName;
    private String address;

    // --> CONNECTION
    public void setConnection(Connection conn) {
        CustomerAccount.connection = conn;
    }

    public Connection getConnection() {
        return connection;
    }

    // EXIT THE PROGRAM AND CLOSE THE CONNECTION
    public void close() throws SQLException {
        System.out.println("Thank you!!");
        connection.close();
    }

    // ---> FUNCTIONS
    // 1.0 ENTER CUSTOMER ACCOUNT
    public void create() {
        String pattern = "[a-zA-Z\\s]+";
        System.out.println("\nCREATE A NEW CUSTOMER ACCOUNT\n");
        inputAccountNumber(); // go to 1.1.0
        this.firstName = valid.validateString("First Name:", pattern);
        this.lastName = valid.validateString("Last Name:", pattern);
        this.address = valid.validateString("Address:", pattern);
        confirmCustomerAccount(); // --> GO TO 1.2
    }

    // 1.1.0 INPUT ACCOUNT NUMBER
    public void inputAccountNumber() {
        String input;
        do {
            System.out.printf("%-35s", "Account Number:");
            input = scan.nextLine();

            if (input.isBlank()) {
                System.out.println("Account Number is empty!\n");
            } else {
                put.delay(1000);
                if (input.matches("[0-9]{4}")) {
                    if (validateAccount(input) == 0) { // ==> GO TO 1.1.1
                        this.accountNumber = input;
                    } else {
                        System.out.printf("Account #%s exists!\n\n", input);
                    }
                } else {
                    System.out.println("Invalid input!\n");
                }

            }
        } while (this.accountNumber == null);
    }

    // 1.1.1 VALIDATE ACCOUNT
    public int validateAccount(String accountNumber) {
        int count = 0;
        String sql = "SELECT COUNT(*) as count from customer_account WHERE accountNumber = ?";
        try {
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ps.setString(1, accountNumber);
            ResultSet result = ps.executeQuery();

            if (result.next()) {
                do {
                    count = result.getInt("count");
                } while (result.next());
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return count;
    }

    // 1.2 CONFIRM USER INPUT
    // IF first and last name appear on the customer account, it will display an
    // error message
    // ELSE proceed to confirm
    public void confirmCustomerAccount() {
        boolean flag = false;
        String sql = "SELECT * from customer_account where firstName=? and lastName=?";
        try {
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ps.setString(1, firstName);
            ps.setString(2, lastName);

            ResultSet result = ps.executeQuery();

            if (result.next()) {
                put.delay(1000);
                System.out.println("\nA PERSON IS ALREADY USING YOUR DETAILS. PLEASE TRY AGAIN.");
            } else {
                do {
                    System.out.print("\nDO YOU WANT TO CREATE ACCOUNT USING THIS DATA? [Y/N]:\t\t");
                    String input = scan.nextLine();
                    if (input.equalsIgnoreCase("Y")) {
                        put.delay(1000);
                        submitCustomerAccount(); // GO TO 1.3
                        flag = true;
                    } else if (input.equalsIgnoreCase("N")) {
                        put.delay(1000);
                        System.out.println("\nDATA NOT SAVED!!");
                        flag = true;
                    } else {
                        System.out.println("INVALID CHOICE!");
                    }

                } while (flag == false);
            }

        } catch (SQLException e) {
            System.out.println("\nA PERSON IS ALREADY USING YOUR DETAILS. PLEASE TRY AGAIN.");
        }

    }

    // 1.3 SUBMIT CUSTOMER ACCOUNT
    public void submitCustomerAccount() {
        String sql = "INSERT into customer_account VALUES (?,?,?,?)";
        try {
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ps.setString(1, accountNumber);
            ps.setString(2, firstName);
            ps.setString(3, lastName);
            ps.setString(4, address);
            int result = ps.executeUpdate();
            // using conditional formatting
            String message = (result == 1) ? String.format("ACCOUNT #%s SUCCESSFULLY CREATED!\n", accountNumber)
                    : String.format("ACCOUNT #%s EXIST!\n", accountNumber);
            System.out.print(message);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 5.1 SEARCH CUSTOMER ACCOUNT
    public void search() {
        System.out.println("\nSEARCH CUSTOMER ACCOUNT");
        this.firstName = valid.validateString("First Name:", "[a-zA-Z\\s]+");
        this.lastName = valid.validateString("Last Name:", "[a-zA-Z\\s]+");

        put.delay(1000);
        String sql = "SELECT * from customer_account where firstName=? and lastName=?";
        try {
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ps.setString(1, firstName);
            ps.setString(2, lastName);

            ResultSet result = ps.executeQuery();

            if (result.next()) {
                do {
                    String acct = result.getString("accountNumber");
                    String first = result.getString("firstName");
                    String last = result.getString("lastName");
                    String add = result.getString("address");

                    System.out.printf("\nAccount #%s exist.\n", acct);
                    getAccountInformation(acct, first, last, add); // GO TO 5.3
                    put.delay(1000);
                    getPolicyOwned(acct);
                } while (result.next());
            } else {
                System.out.println("\nACCOUNT DOESN'T EXIST. PLEASE CREATE AN ACCOUNT!!");
            }

        } catch (SQLException e) {
            System.out.println("\nACCOUNT DOESN'T EXIST. PLEASE CREATE AN ACCOUNT!!");
        }
    }

    // 5.3 GET ACCOUNT INFORMATION
    // ---- edit
    public void getAccountInformation(String accountNumber, String first, String last, String address) {
        String format = "|%1$-20s|%2$-20s|%3$-20s|%4$-40s|\n";
        String acctNumber = String.format("%04d", Integer.parseInt(accountNumber));
        put.createBar("customerAccount");
        System.out.printf(format, " Account Number", " First Name", " Last Name", " Address");
        put.createBar("customerAccount");
        System.out.printf(format, acctNumber, first, last, address);
        put.createBar("customerAccount");
    }

    // 5.4 GET POLICY OWNED
    public void getPolicyOwned(String acct) {
        String formatHead = "|%1$-20s|%2$-20s|%3$-20s|%4$-20s|\n";
        String formatBody = "|%1$-20s|%2$-20s|%3$-20s|%4$-20s|\n";
        String sql = "SELECT * from policy where acctNo = ?";
        try {
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ps.setString(1, acct);

            ResultSet result = ps.executeQuery();

            if (result.next()) {
                System.out.printf("\nPOLICY OWNED BY ACCOUNT #%s\n", acct);
                put.createBar("customerAccount.policy");
                System.out.printf(formatHead, " Policy #", " Effective Date", " Expiration Date", "Status");
                put.createBar("customerAccount.policy");
                do {
                    String policyNo = String.format("%06d", result.getInt("policyNumber"));
                    String effectiveDate = result.getString("effectiveDate");
                    String expireDate = result.getString("expirationDate");
                    String status = (result.getInt("status") == 0) ? "NOT ACTIVE" : "ACTIVE";
                    System.out.printf(formatBody, policyNo, effectiveDate, expireDate, status);
                    put.createBar("customerAccount.policy");
                } while (result.next());
                put.delay(1000);
                getPolicyHolder(acct);
            } else {
                System.out.println("NO POLICY OWNED.\n");
                System.out.println("NO POLICY HOLDER ASSOCIATED TO SAID ACCOUNT.\n");
            }

        } catch (SQLException e) {
            System.out.println("NO POLICY OWNED.\n");
        }
    }

    // 5.6 GET POLICY HOLDER
    // select all policy holder associated to searched account
    public void getPolicyHolder(String acct) {
        String format = "|%1$-15s|%2$-15s|%3$-15s|%4$-30s|%5$-20s|%6$-20s|\n";
        System.out.println("\nPOLICY HOLDER ASSOCIATED TO ACCOUNT #" + acct);
        put.createBar("customerAccount.policyHolder");
        System.out.printf(format, " Policy #", " First Name", " Last Name", " Address", " Driver License #",
                " Date Issued"); // header
        put.createBar("customerAccount.policyHolder");

        String sql = "SELECT * from policy_holder where acctNo = ?";
        try {
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ps.setString(1, acct);
            ResultSet result = ps.executeQuery();

            if (result.next()) {
                do {
                    String policyNo = String.format("%06d", result.getInt("policyNumber"));
                    String first = result.getString("firstName");
                    String last = result.getString("lastName");
                    String addr = result.getString("address");
                    String licenseNo = result.getString("driverLicenseNumber");
                    String date = result.getString("dateIssued");

                    System.out.printf(format, policyNo, first, last, addr, licenseNo, date);
                    put.createBar("customerAccount.policyHolder");
                } while (result.next());
            } else {
                System.out.println("\nNO POLICY HOLDER ASSOCIATED TO SAID ACCOUNT.");
            }

        } catch (SQLException e) {
            System.out.println("\nNO POLICY HOLDER ASSOCIATED TO SAID ACCOUNT.");
        }

    }
}
