package com.drawrunner.constants;

public enum Pads {
	LC39A("LC 39A", "data/images/LC-39A pad symbol.png"),
	SLC40("SLC 40", "data/images/SLC-40 pad symbol.png"),
	SLC4E("SLC 4E", "data/images/SLC-4E pad symbol.png");
	
	public String name;
	public String symbolPath;
	
	Pads(String name, String symbolPath) {
		this.name = name;
		this.symbolPath = symbolPath;
	}
	
	public static Pads approxStringToPad(String rawPad) {
		Pads[] padsValues = Pads.values();
		
		
		int i = 0;
		for (; i < padsValues.length && !rawPad.toLowerCase().contains(padsValues[i].name.toLowerCase()); i++);
		
		
		if(i < padsValues.length) {
			return padsValues[i];
		} else {
			return null;
		}
	}
	
	public static Pads stringToPad(String rawPad) {
		if(rawPad != null) {
			return Pads.valueOf(rawPad);
		} else {
			return null;
		}
	}
}
