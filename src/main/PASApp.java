/**
 * Java Course 4, Module 3
 * 
 * CAPSTONE PROJECT
 * PAS Driver Class
 * 
 * @author Lmarl Saria
 * 
 * Date Created: June 28, 2022
 * Date Modified: November 23, 2022
 * 
 * -- add database implementation
 * -- refactoring
 * -- enhanced switch (JAVA 17)
 */

package main;

import java.sql.*;
import java.util.*;
import models.*;

public class PASApp {

	static Scanner scan = new Scanner(System.in);
	static Connection conn = null;
	static String URL = "jdbc:mysql://127.0.0.1:3306/"; // URL
	static String USERNAME = "root"; // USERNAME
	static String PASSWORD = "root"; // PASSWORD

	public static void main(String[] args) {
		String choice = "";
		CustomerAccount customer = new CustomerAccount();
		Policy policy = new Policy();
		Claim claim = new Claim();

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");// Register driver
			customer.delay(500);
			System.out.println("Establishing connection...");// Establish a connection with in the database
			customer.delay(500);
			conn = DriverManager.getConnection(URL, USERNAME, PASSWORD);
			customer.setConnection(conn);
			System.out.println("Connection done.");
			customer.initializeDatabase();
			customer.delay(500);
			do {
				customer.menu(); // set to menu
				choice = scan.nextLine();
				if (choice.isBlank()) {
					System.out.println("\nNo choice. Try again!");
				} else {
					// enhanced switch case (JAVA 17)
					switch (choice) {
						case "1" -> customer.create();
						case "2" -> policy.create();
						case "3" -> policy.cancelPolicy();
						case "4" -> claim.file();
						case "5" -> customer.search();
						case "6" -> policy.search();
						case "7" -> claim.search();
						case "-1" -> customer.close();
						default -> System.out.println("Invalid choice.");
					}
				}
			} while (!choice.equalsIgnoreCase("-1"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
