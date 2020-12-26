package com.drawrunner.data.entities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Rocket extends SpaceEntity {
	private String name;
	private Date firstFlightDate;
	
	public Rocket(String id) {
		super(id);
		
		name = null;
		firstFlightDate = new Date();
	}

	public boolean isFalcon9() {
		return this.formattedName().contains("falcon9");
	}
	
	public boolean isFalconHeavy() {
		return this.formattedName().contains("falconheavy");
	}
	
	private String formattedName() {
		return name.toLowerCase().replace(" ", "");
	}
	
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getFirstFlightDate() {
		return this.firstFlightDate;
	}

	public void setFirstFlightDate(Date firstFlightDate) {
		this.firstFlightDate = firstFlightDate;
	}
	
	public void setFirstFlightDate(String firstFlightDate) {
		try {
			this.firstFlightDate = new SimpleDateFormat("yyyy-MM-dd").parse(firstFlightDate);
		} catch (ParseException e) {
			System.err.println("Error parsing first flight date: " + firstFlightDate);
			e.printStackTrace();
		}
	}
}
