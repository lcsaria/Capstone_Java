/**
 * Java Course 4, Module 3
 * 
 * CAPSTONE PROJECT
 * Rating Engine Class
 *
 * @author Lmarl Saria
 * 
 * Date Created: June 28, 2022
 * Date Updated: August 16, 2022
 * 
 * -- refactoring (add user validation) 
 * -- fix bug (infinite values by adding cast (float) to total.
 */

package models;

import java.time.*;
import java.util.*;

public class RatingEngine {
	CustomerAccount ca = new CustomerAccount();
	PolicyHolder ph = new PolicyHolder();
	private float calculatedPremium;
	private double dlx;
	private double vp;
	private double vpf;

	final int TODAY = Calendar.getInstance().get(Calendar.YEAR);

	/*
	 * CALCULATE THE PREMIUM USING FORMULA BELOW:
	 * P (premium) = (VP x VPF) + ((VP/100)/DLX)
	 * P = calculated premium
	 * VP = vehicle purchase price
	 * VPF = vehicle price factor
	 * DLX = num of years since driver license was first issued
	 */
	public double computedPremium(double premium, int year) {
		this.dlx = setDLX();
		this.vp = premium;
		setVPF(year);
		compute();
		return calculatedPremium;
	}

	// The setDLX() method computes the number of years since driver license was
	// first issued.
	public double setDLX() {
		String dateIssued = ph.dateIssued;
		String[] date = dateIssued.split("-"); // split the date by "-" (YYYY-MM-DD)
		int year = Integer.parseInt(date[0]); // YYYY
		int month = Integer.parseInt(date[1]); // MM
		int day = Integer.parseInt(date[2]); // DD

		LocalDate issueDate = LocalDate.of(year, month, day);
		LocalDate dateToday = LocalDate.now(ZoneId.of("Asia/Shanghai")); // get the today's date
		double years = java.time.temporal.ChronoUnit.YEARS.between(issueDate, dateToday); // compute the number of years
		return years;
	}

	// The setVPF() method computes the vehicle price factor.
	public void setVPF(int year) {
		int age = TODAY - year; // get the age of the car
		if (age < 1) {
			this.vpf = 0.01;
		} else if (age < 3) {
			this.vpf = 0.008;
		} else if (age < 5) {
			this.vpf = 0.007;
		} else if (age < 10) {
			this.vpf = 0.006;
		} else if (age < 15) {
			this.vpf = 0.004;
		} else if (age < 20) {
			this.vpf = 0.002;
		} else if (age < 40) {
			this.vpf = 0.001;
		} else {
			this.vpf = 0;
		}
	}

	// The compute() method computes the total amount.
	public void compute() {
		// add casting
		double total = ((vp * vpf) + ((vp / 100.0) / dlx));
		this.calculatedPremium = (float) total;
	}

	// It gets the premium
	public float getPremium() {
		return calculatedPremium;
	}
}
