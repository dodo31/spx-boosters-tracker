package com.drawrunner.ui.nextlaunch;

import com.drawrunner.ui.Drawable;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PVector;

public class BoosterSection extends Drawable {
	private BoosterIdSection boosterIdSection;
	private FlightInfosSection flightInfosSection;
	
	public BoosterSection(PApplet p, String boosterId, float posY, PFont boosterIdsFont) {
		boosterIdSection = new BoosterIdSection(p, boosterId, new PVector(0, posY), boosterIdsFont);
		flightInfosSection = new FlightInfosSection();
		
		position = new PVector(0, posY);
		
		float width = p.width;
		float height = boosterIdSection.getSize().y + flightInfosSection.getSize().y;
		size = new PVector(width, height);
		
		float infosPosX = 0;
		float infosPosY = position.y + boosterIdSection.getSize().y + FlightInfosSection.TOP_MARGIN;
		
		float infosWidth = width;
		float infosHeight = flightInfosSection.getSize().y;
		
		flightInfosSection.setPosition(new PVector(infosPosX, infosPosY));
		flightInfosSection.setSize(new PVector(infosWidth, infosHeight));
	}
	
	public void addFlightInfo(FlightInfo newFlightInfo) {
		flightInfosSection.addFlightInfo(newFlightInfo);
	}
	
	@Override
	public void draw(PGraphics g) {
		boosterIdSection.draw(g);
		flightInfosSection.draw(g);
	}
	
	public BoosterIdSection getBoosterIdSection() {
		return this.boosterIdSection;
	}

	public FlightInfosSection getFlightInfosSection() {
		return this.flightInfosSection;
	}
}
