/**
we * Java Course 4, Module 3
 * 
 * CAPSTONE PROJECT
 * Claim class 
 * 
 * @author Lmarl Saria
 * 
 * Date Created: June 28, 2022
 * Date Updated: August 15, 2022
 * 
 * -- edit header and comments
 * -- add user validation [claim number]
 * -- enhanced switch case (JAVA 17)
 * -- use ternary operator for single if-else statement
 */

package models;

import java.sql.*;

public class Claim extends Policy {

	private String claimNumber;
	private String date;
	private String address;
	private String description;
	private String damageDescription;
	private double cost;
	private String policyNumber;

	// INPUT CLAIM INFORMATION
	@Override
	public void input(String type, String pattern) {
		String choice = type;
		String input = null;
		boolean flag = false;

		switch (choice) {
			case "Claim Number" -> {
				do {
					System.out.print(type + "[Cxxxxx]:\t\t");
					input = scan.nextLine();

					if (input.isBlank()) {
						System.out.println(type + " is empty!\n");
					} else if (!input.matches(pattern)) {
						System.out.println("Invalid input\n");
					} else {
						if (validateClaimNumber(input) == false) { // GO TO 4.2.1
							System.out.println("Claim #" + input + " exists. Try another number.\n");
						} else {
							this.claimNumber = input;
							flag = true;
						}
					}
				} while (flag == false);
			}
			case "Claim Number - Search" -> {
				do {
					System.out.print("Claim Number [Cxxxxx]:\t\t");
					input = scan.nextLine();

					if (input.isBlank()) {
						System.out.println(type + " is empty!\n");
					} else if (!input.matches(pattern)) {
						System.out.println("Invalid input\n");
					} else {
						this.claimNumber = input;
						flag = true;
					}
				} while (flag == false);
			}
		}

		flag = false;
		type = "";
		pattern = "";
		choice = "";
	}

	// 4.0 SEARCH POLICY NUMBER
	public void file() {
		System.out.println("\nFILE A ACCIDENT CLAIM");

		try {
			ResultSet result = searchPolicyNumber(); // GOTO 4.1
			// IF policyNumber EXIST
			if (result.next()) {
				do {
					String expire = result.getString("expirationDate");
					int status = result.getInt("status");
					// IF status is INACTIVE, ELSE ready to file.
					if (status == 0) {
						System.out.printf("\nPolicy #%s is INACTIVE. Expration Date: %s\n", policyNumber, expire);
					} else {
						delay(1000);
						System.out.printf("Policy #%s exist. Ready for filing claim.\n\n", policyNumber);
						claimInfo(); // GO TO 4.2
					}
				} while (result.next());
			} else {
				System.out.println("\nPolicy doesn't exist. Please get a policy quote and buy the policy!!");
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 4.1 TO FILE A CLAIM, THE USER MUST SEARCH POLICY NUMBER
	public ResultSet searchPolicyNumber() {
		this.policyNumber = valid.validateString("Policy Number [XXXXXX]:", "[0-9]{6}");
		ResultSet result = null;
		String sql = "SELECT * from policy WHERE policyNumber=?";
		try {
			PreparedStatement ps = getConnection().prepareStatement(sql);
			ps.setString(1, policyNumber);
			result = ps.executeQuery();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return result;
	}

	// 4.2 GET CLAIM INFORMATION
	public void claimInfo() {
		System.out.println("CLAIM INFORMATION\n");
		input("Claim Number", "^[C][0-9]{5}");
		this.date = valid.validateString("Date of Accident (YYYY-MM-DD): ", "[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}");
		this.address = valid.validateString("Address: ", "[a-zA-Z\s]+");
		this.description = valid.validateString("Description: ", "[a-zA-Z\s]+");
		this.damageDescription = valid.validateString("Damage Description: ", "[a-zA-Z\s]+");
		this.cost = Double.parseDouble(valid.validateString("Estimated cost of repairs: $", "[0-9]+[\\.]?[0-9]*"));
		confirm(); // GO TO 4.3
	}

	// 4.2.1 VALIDATE IF CLAIM NUMBER EXISTS
	public boolean validateClaimNumber(String input) {
		delay(1000);
		boolean isValid = false;
		String sql = "SELECT * from claim where claimNumber=?";
		try {
			PreparedStatement ps = getConnection().prepareStatement(sql);
			ps.setString(1, input);
			ResultSet result = ps.executeQuery();
			isValid = (result.next()) ? false : true;
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return isValid;
	}

	// 4.3 CONFIRM CLAIM
	@Override
	public void confirm() {
		String input;
		boolean flag = false;
		do {
			System.out.print("\nDo you want to save claim using this data? [Y/N] ");
			input = scan.nextLine();
			if (input.equalsIgnoreCase("n")) {
				delay(1000); // 1 sec delay
				System.out.println("Saved Failed!");
				flag = true;
			} else if (input.equalsIgnoreCase("y")) {
				delay(1000);
				submit(); // GO TO 4.4
				flag = true;
			} else {
				System.out.println("Invalid choice!");
			}

		} while (flag == false);
	}

	// 4.4 SUBMIT CLAIM
	@Override
	public void submit() {
		String sql = "INSERT into claim VALUES (?,?,?,?,?,?,?)";
		try {
			PreparedStatement ps = getConnection().prepareStatement(sql);
			ps.setString(1, claimNumber);
			ps.setString(2, date);
			ps.setString(3, address);
			ps.setString(4, description);
			ps.setString(5, damageDescription);
			ps.setDouble(6, cost);
			ps.setString(7, policyNumber);
			int result = ps.executeUpdate();
			String message = (result == 1) ? String.format("\nClaim #%s Saved!", claimNumber) : "\nSomething wrong!"; // ternary
			System.out.println(message);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 7.1 SEARCH CLAIM
	@Override
	public void search() {
		System.out.println("\nSEARCH CLAIM");
		input("Claim Number - Search", "^[C][0-9]{5}");
		delay(1000);
		String sql = "SELECT * from claim where claimNumber=?";
		try {
			PreparedStatement ps = getConnection().prepareStatement(sql);
			ps.setString(1, claimNumber);
			ResultSet result = ps.executeQuery();

			if (result.next()) {
				do {
					String cNo = result.getString("claimNumber");
					String date = result.getString("dateOfAccident");
					String address = result.getString("accidentAddress");
					String desc = result.getString("description");
					String dmgDesc = result.getString("damageDescription");
					Double cost = result.getDouble("estimatedCost");
					int pNo = result.getInt("policyNumber");

					System.out.println("\nClaim #" + cNo);
					delay(1000);
					System.out.printf("Linked to Policy #%06d\n", pNo);
					System.out.println("Date of Accident:\t\t" + date);
					System.out.println("Address:\t\t\t" + address);
					System.out.println("Description:\t\t\t" + desc);
					System.out.println("Damage Description:\t\t" + dmgDesc);
					System.out.printf("Estimated Cost:\t\t\t$%.2f\n", cost);

				} while (result.next());
			} else {
				System.out.printf("\nClaim #%s doesn't exist. Please file a claim!!\n", claimNumber);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
