/**
 * Java Course 4, Module 3
 * 
 * CAPSTONE PROJECT
 * class Policy Holder
 * 
 * @author Lmarl Saria
 * 
 * Date Created: June 28, 2022
 * Date Modified: December 19, 2022
 * 
 * -- refacotring (user validation)
 * -- add new function
 * -- enhanced switch (JAVA 17)
 * -- using conditional formatting
 */

package models;

import java.sql.*;

public class PolicyHolder extends Policy {
	static RatingEngine re = new RatingEngine();
	private String policyNumber;
	private String firstName;
	private String lastName;
	private String address;
	private String driverLicenseNumber;
	static String dateIssued;

	// 2.3.4.1.1a IF THE POLICY HOLDER is ACCOUNT OWNER
	public void setPolicyHolder(String policyNumber, String accountNumber) {
		this.policyNumber = policyNumber;
		System.out.println("\nPOLICY HOLDER");
		fetchOwnerInformation(accountNumber); // GO TO 2.3.4.1a.1
		this.driverLicenseNumber = valid.validateString("Driver License Number: ", "^.{0,255}$");
		this.dateIssued = valid.validateDate("Date Issued: ", "^\\d{4}-\\d{2}-\\d{2}$");
	}

	// 2.3.4.1.1b IF THE POLICY HOLDER is DEPENDENT
	public void setPolicyHolder(String policyNumber) {
		this.policyNumber = policyNumber;
		System.out.println("\nPOLICY HOLDER");
		this.firstName = valid.validateString("First Name: ", "[a-zA-Z\s]+");
		this.lastName = valid.validateString("Last Name: ", "[a-zA-Z\s]+");
		this.address = valid.validateString("Address: ", "[a-zA-Z\s]+");
		this.driverLicenseNumber = valid.validateString("Driver License Number: ", "^.{0,255}$");
		this.dateIssued = valid.validateDate("Date Issued: ", "^\\d{4}-\\d{2}-\\d{2}$");
	}

	// 2.3.4.1a.1 FETCH OWNER INFORMATION
	public void fetchOwnerInformation(String acct) {
		String sql = "SELECT * FROM customer_account WHERE accountNumber=?";
		try {
			PreparedStatement ps = getConnection().prepareStatement(sql);
			ps.setString(1, acct);
			ResultSet result = ps.executeQuery();
			while (result.next()) {
				this.firstName = result.getString("firstName");
				this.lastName = result.getString("lastName");
				this.address = result.getString("address");
				System.out.printf("%-35s%s\n", "First Name:", firstName);
				System.out.printf("%-35s%s\n", "Last Name:", lastName);
				System.out.printf("%-35s%s\n", "Address:", address);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 2.6.1 SUBMIT POLICY HOLDER
	public void submitPolicy(int acctNo) {
		String sql = "INSERT into policy_holder VALUES (?,?,?,?,?,?,?)";
		try {
			PreparedStatement ps = getConnection().prepareStatement(sql);
			ps.setString(1, policyNumber);
			ps.setString(2, firstName);
			ps.setString(3, lastName);
			ps.setString(4, address);
			ps.setString(5, driverLicenseNumber);
			ps.setString(6, dateIssued);
			ps.setInt(7, acctNo); // additional column
			int result = ps.executeUpdate();
			System.out.println((result == 1) ? "\nPolicy Holder Saved!" : "Something wrong!\n"); // using conditional
																									// formatting
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
