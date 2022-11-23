/**
 * Java Course 4, Module 3
 * 
 * CAPSTONE PROJECT
 * Vehicle Class
 *
 * @author Lmarl Saria
 * 
 * Date Created: June 28, 2022
 * Date Updated: Septmeber 16, 2022
 * 
 * -- refactoring (user validation)
 * --  enhanced switch (JAVA 17)	
 */

package models;

import java.sql.*;

public class Vehicle extends Policy {
	RatingEngine re = new RatingEngine();
	private String policyNumber;
	private String make;
	private String model;
	private int year;
	private String type;
	private String fuelType;
	private double price;
	private String color;
	private double premium;
	private int vehicleNo;

	
	/* 	in this method, every input has name and pattern, it will find their respective
	*	case via switch, after the user prompt the enter input, it will validate 
	*/
	public void input(String type) {
		String choice = type;
		String input = null;
		boolean flag = false;
		String pattern = null;

		switch (choice) {
		case "Make/Brand", "Model", "Color" -> {
			do {
				if (type == "Make/Brand") {
					System.out.print(type + ":\t\t\t");
				} else {
					System.out.print(type + ":\t\t\t\t");
				}
				input = scan.nextLine();

				if (input.isBlank()) {
					System.out.println(type + " is empty!\n");
				} else {
					switch (type) {
						case "Make/Brand" -> {
							this.make = input;
						}
						case "Model" -> {
							this.model = input;
						}
						case "Color" -> {
							this.color = input;
						}
					}
					flag = true;
				}
			} while (flag == false);
			break;
		}
		case "Year" -> {
			pattern = "[0-9]{4}";
			do {
				System.out.print(choice + ":\t\t\t\t");
				input = scan.nextLine();

				if (input.isBlank()) {
					System.out.println(type + " is empty!\n");
				} else if (!input.matches(pattern)) {
					System.out.println("Invalid input!\n");
				} else {
					this.year = Integer.parseInt(input);
					flag = true;
				}
			} while (flag == false);
		}
		case "Type" -> {
			pattern = "[1-4]{1}";
			do {
				System.out.println("\nType: [1] 4-door sedan, [2] 2-door sports car, [3] SUV, or [4] truck");
				System.out.print("Select:\t\t\t\t");
				input = scan.nextLine();

				if (input.isBlank()) {
					System.out.println(type + " is empty!\n");
				} else if (!input.matches(pattern)) {
					System.out.println("Invalid input!\n");
				} else {
					switch (input) {
						case "1" ->	setType("4-door sedan");
						case "2" ->	setType("2-door sports car");
						case "3" ->	setType("SUV");
						case "4" ->	setType("truck");
					}
					flag = true;
				}
			} while (flag == false);
			break;
		}
		case "Fuel Type" -> {
			pattern = "[1-3]{1}";
			do {
				System.out.println("\nFuel Type: [1] Diesel, [2] Electric, or [3] Petrol");
				System.out.print("Select:\t\t\t\t");
				input = scan.nextLine();

				if (input.isBlank()) {
					System.out.println(type + " is empty!\n");
				} else if (!input.matches(pattern)) {
					System.out.println("Invalid input!\n");
				} else {
					switch (input) {
					case "1" ->	setFuelType("Diesel");
					case "2" ->	setFuelType("Electric");
					case "3" ->	setFuelType("Petrol");
					}
					flag = true;
				}
			} while (flag == false);
			break;
		}
		case "Purchase Price" -> {
			pattern = "(0|([1-9][0-9]*))(\\.[0-9]{1,2}+)?$";
			do {
				System.out.print("\nPurchase Price:\t\t\t$");
				input = scan.nextLine();

				if (input.isBlank()) {
					System.out.println(type + "is empty!\n");
				} else if (!input.matches(pattern)) {
					System.out.println("Invalid input!\n");
				} else {
					this.price = Double.parseDouble(input);
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

	public void setVehicle(String policyNumber, int no) {
		this.policyNumber = policyNumber;
		this.vehicleNo = no;
		addVehicle();
	}

	// 2.4 ADD VEHICLES
	@Override
	public void addVehicle() {
		System.out.printf("\nVehicle no. #%d\n", vehicleNo);
		input("Make/Brand");
		input("Model");
		input("Year");
		input("Type");
		input("Fuel Type");
		input("Purchase Price");
		input("Color");

		computePremium();
	}

	public void setType(String input) {
		this.type = input;
	}

	public void setFuelType(String input) {
		this.fuelType = input;
	}

	public void computePremium() {
		re.computedPremium(price, year);
		this.premium = (float) re.getPremium();
	}

	// 2.6.2 SUBMIT
	@Override
	public void submit() {
		String sql = "INSERT into vehicle VALUES (?,?,?,?,?,?,?,?,?)";
		try {
			PreparedStatement ps = getConnection().prepareStatement(sql);
			ps.setString(1, policyNumber);
			ps.setString(2, make);
			ps.setString(3, model);
			ps.setInt(4, year);
			ps.setString(5, type);
			ps.setString(6, fuelType);
			ps.setDouble(7, price);
			ps.setString(8, color);
			ps.setDouble(9, premium);
			int result = ps.executeUpdate();
			if (result == 1) {
				System.out.print("->\t");
			} else {
				System.out.println("Something wrong!\n");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
