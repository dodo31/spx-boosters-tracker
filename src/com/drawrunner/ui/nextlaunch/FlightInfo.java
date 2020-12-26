package com.drawrunner.ui.nextlaunch;

import java.awt.Color;

import com.drawrunner.ui.Drawable;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

public class FlightInfo extends Drawable {
	public final static float SYMBOL_SIZE = 164;
	public final static float TEXT_HEIGHT = SYMBOL_SIZE * 0.25f;
	
	private PImage symbol;
	
	private String text;
	private String descriptionText;

	private Color baseColor;
	private PFont flightInfoFont;
	
	public FlightInfo(PApplet applet, PImage symbol, String text, String descriptionText, Color baseColor, PFont flightInfoFont) {
		this.symbol = symbol;
		
		this.text = text;
		this.descriptionText = descriptionText;
		
		this.baseColor = baseColor;
		this.flightInfoFont = flightInfoFont;

		applet.textFont(flightInfoFont);

		float width = Math.max(SYMBOL_SIZE, applet.textWidth(text));
		float height = SYMBOL_SIZE + TEXT_HEIGHT;
		this.size = new PVector(width, height);
	}
	
	@Override
	public void draw(PGraphics g) {
		float scaleFacor = SYMBOL_SIZE / (symbol.width * 1f);
		
		float symbolWidth = symbol.width * scaleFacor;
		float symbolHeight = symbol.height * scaleFacor;
		
		float symbolPosX = position.x + size.x * 0.5f - symbolWidth * 0.5f;
		float symbolPosY = position.y;
		
		float baseColorHue = g.hue(baseColor.getRGB()) / 255f;
		float baseColorSaturation = g.saturation(baseColor.getRGB()) / 255f;
		float baseColorBrightness = g.brightness(baseColor.getRGB()) / 255f;
		
		int symbolTint = Color.HSBtoRGB(baseColorHue, baseColorSaturation - 0.215f, Math.min(1f, baseColorBrightness - 0.031f));
		int descriptionTextTint = Color.HSBtoRGB(baseColorHue, baseColorSaturation * 0.5f, Math.min(1f, baseColorBrightness));

		g.tint(symbolTint);
		g.image(symbol, symbolPosX, symbolPosY, symbolWidth, symbolHeight);
		
		g.textFont(flightInfoFont);
		g.textAlign(PApplet.CENTER, PApplet.TOP);
		g.fill(baseColor.getRGB());
		g.text(text, position.x + size.x * 0.5f, position.y + SYMBOL_SIZE);
		
//		if(descriptionText.toLowerCase().contains("orbit")) {
//			g.fill(descriptionTextTint);
//			g.textFont(flightInfoFont, flightInfoFont.getSize() * 0.6f);
//			g.text("(probably)", position.x + size.x * 0.5f + 92, position.y + SYMBOL_SIZE + 12);
//		}
		
		g.fill(descriptionTextTint);
		g.textFont(flightInfoFont, flightInfoFont.getSize() * 0.6f);
		g.text(descriptionText, position.x + size.x * 0.5f, position.y + SYMBOL_SIZE - flightInfoFont.getSize() + 80);
	}
}
