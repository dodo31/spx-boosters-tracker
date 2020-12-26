package com.drawrunner.constants;

public enum Features {
	FALCON_HEAVY("FALCON HEAVY", "data/images/Falcon Heavy symbol.png"),
	CREW_DRAGON("DRAGON CREW", "data/images/Crew Dragon symbol.png");
	
	public String name;
	public String symbolPath;
	
	Features(String name, String symbolPath) {
		this.name = name;
		this.symbolPath = symbolPath;
	}
	
	public static Features stringToFeature(String rawFeature) {
		if(rawFeature != null) {
			return Features.valueOf(rawFeature);
		} else {
			return null;
		}
	}
}
