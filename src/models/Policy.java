/**
 * Java Course 4, Module 3
 * 
 * CAPSTONE PROJECT
 * Policy class
 * 
 * @author Lmarl Saria
 * 
 * Date Created: June 28, 2022
 * Date Modified: September 14, 2022
 * 
 * -- edit comments
 * -- refactoring (add user validation)
 * -- enhanced switch (JAVA 17)
 * -- using conditional formatting
 */

package models;

import java.sql.*;
import java.util.*;

public class Policy extends CustomerAccount {
	static PolicyHolder ph = new PolicyHolder();
	static RatingEngine re = new RatingEngine();
	private String accountNumber;
	private String policyNumber;
	private String effectiveDate;
	private String expirationDate;
	private String policyHolder;
	private String option;
	private int vehicle;
	private double premium;
	static ArrayList<Vehicle> vehicles = new ArrayList<Vehicle>();

	// --> INPUT
	/*
	 * in this method, every input has name and pattern, it will find their
	 * respective
	 * case via switch, after the user prompt the enter input, it will validate
	 */
	public void input(String type, String pattern) {
		String choice = type;
		String input = null;
		boolean flag = false;

		switch (choice) {
			// 2.1 ENTER ACCOUNT NUMBER
			// 2.2.1 ENTER POLICY NUMBER
			case "Policy Number" -> {
				do {
					System.out.printf("%-30s", "Enter policy number (XXXXXX):");
					input = scan.nextLine();

					delay(1000);
					if (input.isBlank()) {
						System.out.println(type + " is empty!\n");
					} else if (!input.matches(pattern)) {
						System.out.println("Invalid input!\n");
					} else if (validatePolicyNumber(input) != 0) {
						System.out.println("Policy #" + input + " exists!!\n");
					} else {
						this.policyNumber = input;
						flag = true;
					}
				} while (flag == false);
			}

			// 2.2.1 ENTER POLICY NUMBER
			// 6.1 ENTER POLICY NUMBER
			case "Search Policy" -> {
				do {
					System.out.print("Enter policy number (XXXXXX):\t");
					input = scan.nextLine();

					if (input.isBlank()) {
						System.out.println(type + " is empty!\n");
					} else if (!input.matches(pattern)) {
						System.out.println("Invalid input!\n");
					} else {
						this.policyNumber = input;
						flag = true;
					}
				} while (flag == false);
			}
			// 3.
			case "Cancel Policy" -> {
				do {
					System.out.print("Enter policy number (XXXXXX):\t");
					input = scan.nextLine();

					if (input.isBlank()) {
						System.out.println("Policy number is empty!\n");
					} else if (!input.matches(pattern)) {
						System.out.println("Invalid input!\n");
					} else {
						this.policyNumber = input;
						flag = true;
					}
				} while (flag == false);
			}
		}

		flag = false;
		type = "";
		pattern = "";
		choice = "";
		input = "";
	}

	// VALIDATE POLICY NUMBER
	public int validatePolicyNumber(String policyNumber) {
		int isExist = 0;
		String sql = "SELECT COUNT(*) as count from policy WHERE policyNumber=?";
		try {
			PreparedStatement ps = getConnection().prepareStatement(sql);
			ps.setString(1, policyNumber);
			ResultSet result = ps.executeQuery();
			if (result.next()) {
				do {
					int count = result.getInt("count");
					if (count > 0) {
						isExist = 1;
					}
				} while (result.next());
			}
		} catch (SQLException e) {
			isExist = 0;
		}

		return isExist;
	}

	// --> FUNCTIONS
	// 2.0 GET THE POLICY QUOTE AND BUY THE POLICY
	@Override
	public void create() {
		System.out.println("\nGET A POLICY QUOTE AND BUY THE POLICY");
		this.accountNumber = valid.validateString("Account Number: ", "[0-9]{1,4}");
		int search = searchCustomerAccount(this.accountNumber); // GO TO 2.1
		if (search != 0) {
			createPolicy(this.accountNumber); // GO TO 2.2
		}
	}

	// 2.1 SEARCH ACCOUNT
	public int searchCustomerAccount(String accountNo) {
		int count = 0;
		String sql = "SELECT COUNT(*) as count FROM customer_account WHERE accountNumber=?";

		try {
			PreparedStatement ps = getConnection().prepareStatement(sql);
			ps.setString(1, accountNo);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				String acct = result.getString("count");
				count = Integer.parseInt(acct);
				delay(1000);
				if (count == 0) {
					System.out.println("\nAccount doesn't exist. Please create an account!!");
					delay(1000);
					return 0;
				} else {
					System.out.printf("\nAccount #%s exist.\n", accountNo);
					delay(1000);
					return count;
				}
			}
			return count;
		} catch (SQLException e) {
			System.out.println("\nAccount doesn't exist. Please create an account!!");
			delay(1000);
			return 0;
		}
	}

	// 2.2 CREATE POLICY
	public void createPolicy(String accountNumber) {
		System.out.println("\nCREATING POLICY");
		input("Policy Number", "[0-9]{6}");
		// 2.2.2 ENTER EFFECTIVE DATE
		this.effectiveDate = valid.validateString("Effective Date (YYYY-MM-DD): ", "[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}");
		getExpirationDate(); // GO TO 2.2.3
		assignPolicyHolder();
		if (option.equals("1")) {
			this.policyHolder = "Owner";
			ph.setPolicyHolder(policyNumber, accountNumber); // GO TO 2.3.4.1.1a
			addVehicle();// GO TO 2.4
		} else {
			this.policyHolder = "Dependent";
			ph.setPolicyHolder(policyNumber); // GO TO 2.3.4.1.1b
			addVehicle(); // GO TO 2.4
		}
		confirm(); // GO TO 2.5
	}

	// 2.2.3 GET THE EXPIRATION DATE
	public void getExpirationDate() {
		delay(1000);
		String[] date = effectiveDate.split("-"); // split effective date into 3
		int year = Integer.parseInt(date[0]);
		int month = Integer.parseInt(date[1]);
		int day = Integer.parseInt(date[2]);
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month - 1); // NOTE: 0-Jan, 1-Feb ... 11-Dec
		c.set(Calendar.DAY_OF_MONTH, day);
		c.add(Calendar.MONTH, 6); // add date by 6 months

		String expire = String.format("%d-%d-%d", c.get(Calendar.YEAR), (c.get(Calendar.MONTH) + 1),
				c.get(Calendar.DATE));
		this.expirationDate = expire;
		System.out.printf("Expiration Date (YYYY-MM-DD):\t%s\n", expirationDate);
	}

	// 2.2.4 ASSIGNING POLICY HOLDER
	public void assignPolicyHolder() {
		System.out.println("\nASSIGNING POLICY HOLDER");
		System.out.println("[1] Owner or [2] Dependent");
		this.option = valid.validateString("Option: ", "[1-2]{1}");
	}

	// 2.4 ADD VEHICLE/S
	public void addVehicle() {
		System.out.println("\nVEHICLE");
		System.out.print("Number of vehicle you want to insured:\t");
		int no = Integer.parseInt(scan.nextLine());
		this.vehicle = no;
		for (int count = 0; count < this.vehicle; count++) {
			Vehicle vehicle = new Vehicle();
			vehicles.add(vehicle);
			vehicles.get(count).setVehicle(policyNumber, count + 1); // GO TO 2.4.0
		}
	}

	// 2.5 CONFIRM POLICY
	public void confirm() {
		boolean flag = false;
		do {
			System.out.print("\nDo you want to save using this data? [Y/N]:\t\t");
			String input = scan.nextLine();
			if (input.equalsIgnoreCase("Y")) {
				submit(); // GO TO 1.2
				flag = true;
			} else if (input.equalsIgnoreCase("N")) {
				delay(1000);
				System.out.println("\nData Failed to save!!");
				break;
			} else {
				System.out.println("Invalid choice");
			}
		} while (flag == false);
	}

	// 2.6 SUBMIT POLICY
	public void submit() {
		int acctNo = Integer.parseInt(accountNumber);
		try {
			delay(1000);
			ph.submitPolicy(acctNo); // ==> GO TO 2.6.1

			for (int count = 0; count < this.vehicle; count++) {
				delay(1000);
				vehicles.get(count).submit(); // ==> GO TO 2.6.2
				System.out.printf("%d of %d VEHICLE SAVED.\n", count + 1, this.vehicle);
			}
			this.premium = totalPremium(); // ==> GO TO 2.6.3
			delay(1000);
			String sql = "INSERT into policy VALUES (?,?,?,?,?,?,?,?)";
			PreparedStatement ps = getConnection().prepareStatement(sql);

			ps.setString(1, policyNumber);
			ps.setString(2, effectiveDate);
			ps.setString(3, expirationDate);
			ps.setString(4, policyHolder);
			ps.setInt(5, vehicle);
			ps.setDouble(6, premium);
			ps.setBoolean(7, true);
			ps.setInt(8, acctNo); // additional column

			int result = ps.executeUpdate();
			// ternary
			String message = (result == 1) ? String.format("Policy #%s Saved!\n", policyNumber) : "Something wrong!\n";
			System.out.print(message);
		} catch (SQLException e) {
			System.out.println("Something wrong!\n");
		}
	}

	// 2.6.1 GET TOTAL PREMIUM
	public double totalPremium() {
		String sql = "SELECT SUM(premium) as total from vehicle WHERE policyNumber=?";
		double total = 0;
		try {
			PreparedStatement ps = getConnection().prepareStatement(sql);
			ps.setString(1, policyNumber);

			ResultSet result = ps.executeQuery();

			if (result.next()) {
				do {
					total = result.getDouble("total");
				} while (result.next());
			} else {
				total = 0;
			}

		} catch (SQLException e) {
			total = 0;
		}
		return total;
	}

	// 3.0 SEARCH POLICY TO BE CANCEL
	public void cancelPolicy() {
		System.out.println("\nCANCEL POLICY");
		input("Cancel Policy", "[0-9]{6}");
		getPolicyInformation(); // GO TO 3.1
	}

	// 3.1 GET SPECIFIC POLICY INFORMATION
	public void getPolicyInformation() {
		boolean status;
		String sql = "SELECT * from policy WHERE policyNumber=?";

		try {
			PreparedStatement ps = getConnection().prepareStatement(sql);
			ps.setString(1, policyNumber);

			ResultSet result = ps.executeQuery();

			if (result.next()) {
				do {
					int pNo = result.getInt("policyNumber");
					String effectiveDate = result.getString("effectiveDate");
					String expireDate = result.getString("expirationDate");
					status = result.getBoolean("status");

					// if expiration date exceeds today's date, no need to change.
					if (!status) {
						System.out.println("\nThe said policy number is NOT ACTIVE");
						System.out.println("Expiration date: " + expireDate);
						break;
					}
					System.out.printf("\nPolicy #%06d\t\t\t", pNo);
					System.out.println("\nEffective Date:\t\t\t" + effectiveDate);
					System.out.println("Expiration Date:\t\t" + expireDate);

					confirmCancel(); // GO TO 3.3

				} while (result.next());
			} else {
				System.out.println("\nNo policy number exist.");
			}

		} catch (SQLException e) {
			System.out.println("\nNo policy number exist.");
		}
	}

	// 3.3 CONFIRM IF THE SAID POLICY WILL BE CANCEL
	public void confirmCancel() {
		String choice;
		boolean flag = false;
		do {
			System.out.print("\nDO YOU WANT TO CANCEL THE POLICY [y/n]? ");
			choice = scan.nextLine();
			if (choice.equalsIgnoreCase("y")) {
				delay(1000);
				System.out.println("\nChanging expiration date...");
				delay(1000);
				System.out.println("Changing status...");
				delay(1000);
				flag = true;
				cancel(); // GO TO 3.4
			} else if (choice.equalsIgnoreCase("n")) {
				delay(1000);
				System.out.println("\nCancel failed.");
				flag = true;
			} else {
				System.out.println("Invalid Input.");
			}
		} while (flag == false);
	}

	// 3.4 CHANGE the EXPIRATION DATE to today, THEN set STATUS to 0
	public void cancel() {
		String today = String.valueOf(java.time.LocalDate.now());
		String sql = "UPDATE policy SET expirationDate=?, status=0 WHERE policyNumber=?";

		try {
			PreparedStatement ps = getConnection().prepareStatement(sql);
			ps.setString(1, today);
			ps.setString(2, policyNumber);
			int result = ps.executeUpdate();
			System.out.println((result == 1) ? "DONE" : "Something wrong!\n"); // conditional formatting
		} catch (SQLException e) {
			System.out.println("Something wrong!\n");
		}
	}

	@Override
	// 6.1 SEARCH POLICY
	public void search() {
		System.out.println("\nSEARCH POLICY");
		input("Search Policy", "[0-9]{1,6}");
		displayPolicy();
	}

	// 6.2 DISPLAY POLICY
	public void displayPolicy() {
		String sql = "SELECT * from policy WHERE policyNumber=?";
		delay(1000);
		try {
			PreparedStatement ps = getConnection().prepareStatement(sql);
			ps.setString(1, policyNumber);
			ResultSet result = ps.executeQuery();

			if (result.next()) {
				do {
					String policyNo = result.getString("policyNumber");
					String effectiveDate = result.getString("effectiveDate");
					String expireDate = result.getString("expirationDate");
					String policyHolder = result.getString("policyHolder");
					int vehicles = result.getInt("vehicles");
					Double prem = result.getDouble("premium");
					Boolean status = result.getBoolean("status");

					// if expiration date is today date, the said policy is NOT ACTIVE.
					if (!status) {
						System.out.println("\nThe said policy number is NOT ACTIVE");
						System.out.println("Expiration date: " + expireDate);
						System.out.println("Please contact administrator if there is a problem.");
						break;
					}

					// TABLE FOR POLICY DETAILS
					String formatHead = "|%1$-20s|%2$-20s|%3$-20s|%4$-20s|%5$-20s|%6$-20s|\n";
					String formatBody = "|%1$-20s|%2$-20s|%3$-20s|%4$-20s|$%5$-19.2f|%6$-20s|\n";
					delay(1000);
					System.out.println("\nPolicy #" + policyNumber);
					createBar("policy"); // GO TO 6.A.1
					System.out.printf(formatHead, "Effective Date", "Expiration Date", "Policy Holder", "Vehicles",
							"Premium Charge", "Status");
					createBar("policy"); // GO TO 6.A.1
					System.out.printf(formatBody, effectiveDate, expireDate, policyHolder, vehicles, prem,
							(status ? "ACTIVE" : "NOT ACTIVE"));
					createBar("policy");
					delay(1000);
					System.out.println("\nPOLICY HOLDER");
					showPolicyHolder(policyNo); // GO TO 6.3
					delay(1000);
					System.out.println((vehicles == 1) ? "\nVEHICLE" : "\nVEHICLES"); // conditional formatting
					showVehicle(policyNo); // GO TO 6.4
					createBar("vehicle");
				} while (result.next());
			} else {
				System.out.println("\nPOLICY DOESN'T EXIST. PLEASE GET A POLICY QUOTE AND BUY THE POLICY!!");
			}
		} catch (SQLException e) {
			System.out.println("\nPOLICY DOESN'T EXIST. PLEASE GET A POLICY QUOTE AND BUY THE POLICY!!");
		}
	}

	// 6.3 TABLE FOR POLICY HOLDER DETAILS
	public void showPolicyHolder(String policyNumber) {

		String format = "|%1$-20s|%2$-20s|%3$-40s|%4$-20s|%5$-20s|\n";
		createBar("policy_holder");// 6.A.2
		System.out.printf(format, "First Name", "Last Name", "Address", "Driver License#", "Date Issued"); // header
		createBar("policy_holder");// 6.A.2

		String sql = "SELECT * from policy_holder WHERE policyNumber=?";
		try {
			PreparedStatement ps = getConnection().prepareStatement(sql);
			ps.setString(1, policyNumber);
			ResultSet result = ps.executeQuery();

			if (result.next()) {
				do {
					String first = result.getString("firstName");
					String last = result.getString("lastName");
					String address = result.getString("address");
					String licenseNo = result.getString("driverLicenseNumber");
					String date = result.getString("dateIssued");

					System.out.printf(format, first, last, address, licenseNo, date); // body
					createBar("policy_holder"); // 6.A.2
				} while (result.next());
			} else {
				System.out.println("\nNO POLICY HOLDER");
			}
		} catch (SQLException e) {
			System.out.println("\nNO POLICY HOLDER");
		}

	}

	// 6.4 TABLE FOR VEHICLE DETAILS
	public void showVehicle(String policyNumber) {
		String formatHead = "|%1$-15s|%2$-15s|%3$-15s|%4$-20s|%5$-15s|%6$-20s|%7$-15s|%8$-20s|\n"; // set header format
		String formatBody = "|%1$-15s|%2$-15s|%3$-15s|%4$-20s|%5$-15s|$%6$-19.2f|%7$-15s|$%8$-19.2f|\n"; // set body
																											// format

		String sql = "SELECT * from vehicle WHERE policyNumber=?";
		createBar("vehicle"); // 6.A.3
		System.out.printf(formatHead, "Make/Brand", "Model", "Year", "Type", "Fuel Type", "Purchase Price", "Color",
				"Premium Charge");

		try {
			PreparedStatement ps = getConnection().prepareStatement(sql);
			ps.setString(1, policyNumber);
			ResultSet result = ps.executeQuery();

			if (result.next()) {
				do {
					String make = result.getString("make");
					String model = result.getString("model");
					int year = result.getInt("year");
					String type = result.getString("type");
					String fuel = result.getString("fuelType");
					Double price = result.getDouble("purchasePrice");
					String color = result.getString("color");
					Double premiumCharge = result.getDouble("premium");
					createBar("vehicle"); // 6.A.3
					System.out.printf(formatBody, make, model, year, type, fuel, price, color, premiumCharge);
				} while (result.next());
			} else {
				System.out.println("\nNO VEHICLES");
			}
		} catch (SQLException e) {
			System.out.println("\nNO VEHICLES");
		}
	}

	// 6.A CREATE HORIZONTAL BAR
	public void createBar(String type) {
		final int LENGTH_POLICY = 126;
		final int LENGTH_POLICY_HOLDER = 125;
		final int LENGTH_VEHICLES = 143;

		switch (type) {
			// 6.A.1 POLICY
			case "policy": {
				for (int count = 0; count <= LENGTH_POLICY; count++) {
					if (count == 0 || count == LENGTH_POLICY) {
						System.out.print("+");
					} else {
						System.out.print("-");
					}
				}
				System.out.println();
				break;
			}
			// 6.A.2 POLICY HOLDER
			case "policy_holder": {
				for (int count = 0; count <= LENGTH_POLICY_HOLDER; count++) {
					if (count == 0 || count == LENGTH_POLICY_HOLDER) {
						System.out.print("+");
					} else {
						System.out.print("-");
					}
				}
				System.out.println();
				break;
			}
			// 6.A.3 VEHICLES
			case "vehicle": {
				for (int count = 0; count <= LENGTH_VEHICLES; count++) {
					if (count == 0 || count == LENGTH_VEHICLES) {
						System.out.print("+");
					} else {
						System.out.print("-");
					}
				}
				System.out.println();
				break;
			}
		}
	}
}