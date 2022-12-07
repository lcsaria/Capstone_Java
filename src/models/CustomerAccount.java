/**
  * Java Course 4, Module 3
 * 
 * CAPSTONE PROJECT
 * Customer Account Class
 * 
 * @author Lmarl Saria
 * 
 * Date Created: June 28, 2022
 * Date Modified: December 5, 2022
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
    static Policy p = new Policy();
    static Validation valid = new Validation();
    static Connection connection = null;
    static Scanner scan = new Scanner(System.in);
    private String accountNumber;
    private String lastName;
    private String firstName;
    private String address;

    // --> CONNECTION
    public void setConnection(Connection conn) {
        this.connection = conn;
    }

    public Connection getConnection() {
        return connection;
    }

    public void initializeDatabase() {
        try {
            Statement stmt = connection.createStatement();

            String initDatabase = "CREATE DATABASE IF NOT EXISTS `capstone`; \r\n" +
                    "";

            String useDatabase = "USE `capstone`; \r\n" +
                    "";

            String customerAccountTable = "CREATE TABLE IF NOT EXISTS `capstone`.`customer_account` ( \r\n" +
                    " `accountNumber` INT NOT NULL, \r\n" +
                    "`firstName` VARCHAR(45) NOT NULL, \r\n" +
                    "`lastName` VARCHAR(255) NOT NULL, \r\n" +
                    "`address` VARCHAR(45) NOT NULL,  \r\n" +
                    "PRIMARY KEY (`accountNumber`));";

            String policyTable = "CREATE TABLE IF NOT EXISTS `capstone`.`policy` ( \r\n" +
                    "`policyNumber` INT NOT NULL, \r\n" +
                    "`effectiveDate` DATE NOT NULL, \r\n" +
                    "`expirationDate` DATE NOT NULL, \r\n" +
                    "`policyHolder` VARCHAR(255) NOT NULL, \r\n" +
                    "`vehicles` INT NOT NULL, \r\n" +
                    "`premium` DECIMAL(10,5) NOT NULL, \r\n" +
                    "`status` BOOLEAN NOT NULL, \r\n" +
                    "`acctNo` INT NOT NULL, \r\n" +
                    "PRIMARY KEY (`policyNumber`));";

            String policyHolderTable = "CREATE TABLE IF NOT EXISTS `capstone`.`policy_holder` ( \r\n" +
                    "`policyNumber` INT NOT NULL, \r\n" +
                    "`firstName` VARCHAR(255) NOT NULL, \r\n" +
                    "`lastName` VARCHAR(255) NOT NULL, \r\n" +
                    "`address` VARCHAR(255) NOT NULL, \r\n" +
                    "`driverLicenseNumber` VARCHAR(255) NOT NULL, \r\n" +
                    "`dateIssued` VARCHAR(255) NOT NULL, \r\n" +
                    "`acctNo` INT NOT NULL, \r\n" +
                    "PRIMARY KEY (`policyNumber`));";

            // FIXED: when creating 2 or more vehicles, it will cause error due to duplicate
            // key
            String vehicleTable = "CREATE TABLE IF NOT EXISTS `capstone`.`vehicle` ( \r\n" +
                    "`policyNumber` INT NOT NULL, \r\n" +
                    "`make` VARCHAR(255) NOT NULL, \r\n" +
                    "`model` VARCHAR(255) NOT NULL, \r\n" +
                    "`year` INT NOT NULL, \r\n" +
                    "`type` VARCHAR(255) NOT NULL, \r\n" +
                    "`fuelType` VARCHAR(255) NOT NULL, \r\n" +
                    "`purchasePrice` DOUBLE NOT NULL, \r\n" +
                    "`color` VARCHAR(45) NOT NULL, \r\n" +
                    "`premium` DOUBLE NOT NULL ) \r\n" +
                    "";

            String claimTable = "CREATE TABLE IF NOT EXISTS `capstone`.`claim` ( \r\n" +
                    "`claimNumber` VARCHAR(255) NOT NULL,  \r\n" +
                    "`dateOfAccident` VARCHAR(255) NOT NULL, \r\n" +
                    " `accidentAddress` VARCHAR(255) NOT NULL, \r\n" +
                    "`description` VARCHAR(255) NOT NULL, \r\n" +
                    "`damageDescription` VARCHAR(255) NOT NULL, \r\n" +
                    "`estimatedCost` DOUBLE NOT NULL, \r\n" +
                    " `policyNumber` INT NOT NULL, \r\n" +
                    " PRIMARY KEY (`claimNumber`));";

            stmt.executeUpdate(initDatabase);
            stmt.executeUpdate(useDatabase);
            System.out.println("Clearing cache....");
            stmt.executeUpdate(customerAccountTable);
            System.out.println("Creating customer_account table....");
            stmt.executeUpdate(policyTable);
            System.out.println("Creating policy table....");
            stmt.executeUpdate(policyHolderTable);
            System.out.println("Creating policy_holder table....");
            stmt.executeUpdate(vehicleTable);
            System.out.println("Creating vehicle table....");
            stmt.executeUpdate(claimTable);
            System.out.println("Creating claim table....");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // EXIT THE PROGRAM AND CLOSE THE CONNECTION
    public void close() throws SQLException {
        System.out.println("Thank you!!");
        connection.close();
    }

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

    // DELAY IN
    public void delay(int second) {
        try {
            Thread.sleep(second);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    // ---> ACCOUNT NUMBER
    public void accountNumber() {
        String pattern = "[0-9]{4}";
        String input;
        boolean flag = false;
        do {
            System.out.printf("%-30s", "Account Number:");
            input = scan.nextLine();

            if (input.isBlank()) {
                System.out.println("Account Number is empty!\n");
            } else {
                delay(1000);
                if (input.matches(pattern)) {
                    if (validateAccount(input) == 0) { // ==> GO TO 1.1
                        this.accountNumber = input;
                        flag = true;
                    } else {
                        System.out.printf("Account #%s exists!\n\n", input);
                    }
                } else {
                    System.out.println("Invalid input!\n");
                }

            }
        } while (flag == false);

        input = "";
        flag = false;
        pattern = null;
    }

    // 1.1 VALIDATE ACCOUNT
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

    // USE FOR TABLE IN 5.0 SEARCH ACCOUNT
    public void bar() {
        for (int count = 0; count <= 104; count++) {
            if (count == 0 || count == 104) {
                System.out.print("+");
            } else {
                System.out.print("-");
            }
        }
        System.out.println();
    }

    public void bar2() {
        for (int count = 0; count <= 84; count++) {
            if (count == 0 || count == 84) {
                System.out.print("+");
            } else {
                System.out.print("-");
            }
        }
        System.out.println();
    }

    public void bar3() {
        for (int count = 0; count <= 121; count++) {
            if (count == 0 || count == 121) {
                System.out.print("+");
            } else {
                System.out.print("-");
            }
        }
        System.out.println();
    }

    // ---> FUNCTIONS
    // 1.0 ENTER CUSTOMER ACCOUNT
    public void create() {
        String pattern = "[a-zA-Z\\s]+";
        System.out.println("\nCREATE A NEW CUSTOMER ACCOUNT\n");
        accountNumber(); // input (type, pattern)
        this.firstName = valid.validateString("First Name:", pattern);
        this.lastName = valid.validateString("Last Name:", pattern);
        this.address = valid.validateString("Address:", pattern);
        confirm(); // --> GO TO 1.2
    }

    // 1.2 CONFIRM USER INPUT
    // IF first and last name appear on the customer account, it will display an
    // error message
    // ELSE proceed to confirm
    public void confirm() {
        boolean flag = false;
        String sql = "SELECT * from customer_account where firstName=? and lastName=?";
        try {
            PreparedStatement ps = getConnection().prepareStatement(sql);
            ps.setString(1, firstName);
            ps.setString(2, lastName);

            ResultSet result = ps.executeQuery();

            if (result.next()) {
                delay(1000);
                System.out.println("\nA PERSON IS ALREADY USING YOUR DETAILS. PLEASE TRY AGAIN.");
            } else {
                do {
                    System.out.print("\nDo you want to create account using this data? [Y/N]:\t\t");
                    String input = scan.nextLine();
                    if (input.equalsIgnoreCase("Y")) {
                        delay(1000);
                        submit(); // GO TO 1.3
                        flag = true;
                    } else if (input.equalsIgnoreCase("N")) {
                        delay(1000);
                        System.out.println("\nData Failed to save!!");
                        flag = true;
                    } else {
                        System.out.println("Invalid choice");
                    }

                } while (flag == false);
            }

        } catch (SQLException e) {
            System.out.println("\nA PERSON IS ALREADY USING YOUR DETAILS. PLEASE TRY AGAIN.");
        }

    }

    // 1.3 SUBMIT CUSTOMER ACCOUNT
    public void submit() {
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
        this.firstName = valid.validateName("First Name:");
        this.lastName = valid.validateName("Last Name:");

        delay(1000);
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
                    delay(1000);
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
        bar();
        System.out.printf(format, " Account Number", " First Name", " Last Name", " Address");
        bar();
        System.out.printf(format, acctNumber, first, last, address);
        bar();
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
                bar2();
                System.out.printf(formatHead, " Policy #", " Effective Date", " Expiration Date", "Status");
                bar2();
                do {
                    String policyNo = String.format("%06d", result.getInt("policyNumber"));
                    String effectiveDate = result.getString("effectiveDate");
                    String expireDate = result.getString("expirationDate");
                    String status = (result.getInt("status") == 0) ? "NOT ACTIVE" : "ACTIVE";
                    System.out.printf(formatBody, policyNo, effectiveDate, expireDate, status);
                    bar2();
                } while (result.next());
                delay(1000);
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
        bar3();
        System.out.printf(format, " Policy #", " First Name", " Last Name", " Address", " Driver License #",
                " Date Issued"); // header
        bar3();

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
                    String address = result.getString("address");
                    String licenseNo = result.getString("driverLicenseNumber");
                    String date = result.getString("dateIssued");

                    System.out.printf(format, policyNo, first, last, address, licenseNo, date);
                    bar3();
                } while (result.next());
            } else {
                System.out.println("\nNO POLICY HOLDER ASSOCIATED TO SAID ACCOUNT.");
            }

        } catch (SQLException e) {
            System.out.println("\nNO POLICY HOLDER ASSOCIATED TO SAID ACCOUNT.");
        }

    }
}
