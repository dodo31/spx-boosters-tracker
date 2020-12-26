package com.drawrunner.constants;

import java.util.Objects;

public enum Orbits {
	SO("Suborbital", "data/images/SO symbol.png"),
	
	VLEO("VLEO", "data/images/LEO symbol.png"),
	LEO("LEO", "data/images/LEO symbol.png"),
	SSO("SSO", "data/images/LEO symbol.png"),
	ISS("ISS", "data/images/LEO symbol.png"),

	MEO("MEO", "data/images/MEO symbol.png"),
	PO("Polar", "data/images/MEO symbol.png"),
	
	GTO("GTO", "data/images/GTO symbol.png"),
	GEO("GEO", "data/images/GTO symbol.png"),
	HEO("HEO", "data/images/GTO symbol.png"),
	TLI("TLI", "data/images/GTO symbol.png"),
	L1("L1", "data/images/GTO symbol.png"),
	ESL1("ES-L1", "data/images/GTO symbol.png"),
	TMI("TMI", "data/images/GTO symbol.png"),
	HCO("HCO", "data/images/GTO symbol.png");

	public String name;
	public String symbolPath;
	
	Orbits(String name, String symbolPath) {
		this.name = name;
		this.symbolPath = symbolPath;
	}
	
	public static Orbits stringToOrbit(String rawOrbit) {
		rawOrbit = rawOrbit.replace("-", "");
		
		if(!Objects.isNull(rawOrbit)) {
			return Orbits.valueOf(rawOrbit);
		} else {
			System.err.println("Unknown payload orbit: " + rawOrbit);
			return null;
		}
	}
}
