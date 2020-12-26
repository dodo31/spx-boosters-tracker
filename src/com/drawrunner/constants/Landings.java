package com.drawrunner.constants;

public enum Landings {
	RTLS("RTLS", "data/images/RTLS symbol.png"),
	ASDS("ASDS", "data/images/ASDS symbol.png"),
	EXPENDED("Expended", "data/images/Expended symbol.png"),
	OCEAN("Ocean", "data/images/Expand symbol.png");

	public String name;
	public String symbolPath;
	
	Landings(String name, String symbolPath) {
		this.name = name;
		this.symbolPath = symbolPath;
	}
	
	public static Landings stringToLanding(String rawLanding) {
		if(rawLanding != null) {
			try {
				return Landings.valueOf(rawLanding);
			} catch(IllegalArgumentException e) {
				return null;
			}
		} else {
			return null;
		}
	}
}
