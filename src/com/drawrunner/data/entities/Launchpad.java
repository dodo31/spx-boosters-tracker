package com.drawrunner.data.entities;

public class Launchpad extends SpaceEntity {
	private String name;
	private String shortName;
	private String fullName;
	
	private String symbolPath;
	
	public Launchpad(String id) {
		super(id);
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getShortName() {
		return this.shortName;
	}

	public void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public String getFullName() {
		return this.fullName;
	}

	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	public String getSymbolPath() {
		return this.symbolPath;
	}

	public void setSymbolPath(String symbolPath) {
		this.symbolPath = symbolPath;
	}
}
