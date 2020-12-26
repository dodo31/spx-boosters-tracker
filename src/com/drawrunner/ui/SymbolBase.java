package com.drawrunner.ui;

import java.util.HashMap;
import java.util.Map;

import com.drawrunner.constants.Features;
import com.drawrunner.constants.Landings;
import com.drawrunner.constants.Orbits;
import com.drawrunner.constants.Pads;

import processing.core.PApplet;
import processing.core.PImage;

public class SymbolBase {
	private PApplet applet;

	private Map<String, PImage> symbols;
	
	private PImage soSymbol;
	private PImage leoSymbol;
	private PImage meoSymbol;
	private PImage gtoSymbol;
	
	private PImage expandSymbol;
	private PImage rtlsSymbol;
	private PImage asdsSymbol;
	
	private PImage falconHeavySymbol; 
	private PImage crewDragonSymbol;
	
	private PImage payloadSymbol;
	
	private PImage lc39aPadSymbol;
	private PImage slc40PadSymbol;
	private PImage slc4EPadSymbol;
	
	public SymbolBase(PApplet applet) {
		this.applet = applet;
		
		symbols = new HashMap<String, PImage>();
		this.loadSymbols();
	}
	
	private void loadSymbols() {
		soSymbol = this.loadSymbol(Orbits.SO.symbolPath);
		leoSymbol = this.loadSymbol(Orbits.LEO.symbolPath);
		meoSymbol = this.loadSymbol(Orbits.MEO.symbolPath);
		gtoSymbol = this.loadSymbol(Orbits.GTO.symbolPath);

		expandSymbol = this.loadSymbol(Landings.EXPENDED.symbolPath);
		rtlsSymbol = this.loadSymbol(Landings.RTLS.symbolPath);
		asdsSymbol = this.loadSymbol(Landings.ASDS.symbolPath);
		
		falconHeavySymbol = this.loadSymbol(Features.FALCON_HEAVY.symbolPath);
		crewDragonSymbol = this.loadSymbol(Features.CREW_DRAGON.symbolPath);
		
		payloadSymbol = this.loadSymbol("data/images/Payload symbol.png");
		
		lc39aPadSymbol = this.loadSymbol(Pads.LC39A.symbolPath);
		slc40PadSymbol = this.loadSymbol(Pads.SLC40.symbolPath);
		slc4EPadSymbol = this.loadSymbol(Pads.SLC4E.symbolPath);
	}
	
	private PImage loadSymbol(String symbolPath) {
		PImage newSymbol = applet.loadImage(symbolPath);
		symbols.put(symbolPath, newSymbol);
		return newSymbol;
	}
	
	public PImage infoToSymbol(String infoSymbolPath) {
		if(infoSymbolPath != null) {
			return symbols.get(infoSymbolPath);
		} else {
			return null;
		}
	}
	
	public PImage getSoSymbol() {
		return this.soSymbol;
	}

	public void setSoSymbol(PImage soSymbol) {
		this.soSymbol = soSymbol;
	}
	
	public PImage getLeoSymbol() {
		return this.leoSymbol;
	}

	public void setLeoSymbol(PImage leoSymbol) {
		this.leoSymbol = leoSymbol;
	}

	public PImage getMeoSymbol() {
		return this.meoSymbol;
	}

	public void setMeoSymbol(PImage meoSymbol) {
		this.meoSymbol = meoSymbol;
	}

	public PImage getGtoSymbol() {
		return this.gtoSymbol;
	}

	public void setGtoSymbol(PImage gtoSymbol) {
		this.gtoSymbol = gtoSymbol;
	}

	public PImage getExpandSymbol() {
		return this.expandSymbol;
	}

	public void setExpandSymbol(PImage expandSymbol) {
		this.expandSymbol = expandSymbol;
	}

	public PImage getRtlsSymbol() {
		return this.rtlsSymbol;
	}

	public void setRtlsSymbol(PImage rtlsSymbol) {
		this.rtlsSymbol = rtlsSymbol;
	}

	public PImage getAsdsSymbol() {
		return this.asdsSymbol;
	}

	public void setAsdsSymbol(PImage asdsSymbol) {
		this.asdsSymbol = asdsSymbol;
	}

	public PImage getFalconHeavySymbol() {
		return this.falconHeavySymbol;
	}

	public void setFalconHeavySymbol(PImage falconHeavySymbol) {
		this.falconHeavySymbol = falconHeavySymbol;
	}

	public PImage getCrewDragonSymbol() {
		return this.crewDragonSymbol;
	}

	public void setCrewDragonSymbol(PImage crewDragonSymbol) {
		this.crewDragonSymbol = crewDragonSymbol;
	}

	public PImage getLc39aPadSymbol() {
		return this.lc39aPadSymbol;
	}
	
	public PImage getSlc40PadSymbol() {
		return this.slc40PadSymbol;
	}
	
	public PImage getSlc4EPadSymbol() {
		return this.slc4EPadSymbol;
	}
	
	public void setSlc40PadSymbol(PImage slc40PadSymbol) {
		this.slc40PadSymbol = slc40PadSymbol;
	}
	
	public PImage getPayloadSymbol() {
		return this.payloadSymbol;
	}

	public void setPayloadSymbol(PImage payloadSymbol) {
		this.payloadSymbol = payloadSymbol;
	}
}
