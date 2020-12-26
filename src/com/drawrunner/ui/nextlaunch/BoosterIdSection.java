package com.drawrunner.ui.nextlaunch;

import com.drawrunner.ui.Drawable;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PVector;

public class BoosterIdSection extends Drawable {
	private final static float SEPARATOR_WIDTH_FACTOR = 0.08f;

	private String boosterId;
	
	private PFont boosterIdsFont;
	
	public BoosterIdSection(PApplet p, String boosterId, PVector position, PFont boosterIdsFont) {
		this.boosterId = boosterId;
		
		this.position = position;
		size = new PVector(p.width, boosterIdsFont.getSize() * 1.5f);
		
		this.boosterIdsFont = boosterIdsFont;
	}
	
	@Override
	public void draw(PGraphics g) {
		this.drawBoosterId(g);
		this.drawSeparator(g);
	}

	private void drawBoosterId(PGraphics g) {
		g.textFont(boosterIdsFont);
		
		float boosterIdWidth = this.boosterIdWidth(g.parent);
		float boosterIdCursor = -boosterIdWidth * 0.5f;
		
		float boosterIdCenterX = position.x + size.x * 0.5f;
		
		boolean isFlightNumberSection = false;
		
		g.textAlign(PApplet.LEFT, PApplet.CENTER);
		
		for(int j = 0; j < boosterId.length(); j++) {
			char boosterIdChar = boosterId.charAt(j);
			
			if(boosterIdChar == '.') {
				isFlightNumberSection = true;
			}
			
			if(!isFlightNumberSection) {
				g.fill(242);
			} else {
				g.fill(242, 166);
			}
			
			float charPosX = boosterIdCenterX + boosterIdCursor;
			float charPosY = position.y + size.y * 0.4f;
			g.text(boosterIdChar,charPosX, charPosY);

			if(j < boosterId.length() - 1) {
				boosterIdCursor += g.textWidth(boosterIdChar) + this.boosterCharsMargin();
			} else {
				boosterIdCursor += g.textWidth(boosterIdChar);
			}
		}
	}
	
	private void drawSeparator(PGraphics g) {
		g.stroke(255);
		g.strokeWeight(2.5f);
		g.line(position.x + size.x * SEPARATOR_WIDTH_FACTOR, position.y + size.y,
				position.x + size.x * (1 - SEPARATOR_WIDTH_FACTOR), position.y + size.y);
	}
	
	private float boosterIdWidth(PApplet p) {
		p.textFont(boosterIdsFont);
		
		float charsMargin = this.boosterCharsMargin();
		float boosterIdWidth = p.textWidth(boosterId);
		return boosterIdWidth + ((boosterId.length() - 1) * charsMargin);
	}
	
	private float boosterCharsMargin() {
		return boosterIdsFont.getSize() / 12f;
	}
}
