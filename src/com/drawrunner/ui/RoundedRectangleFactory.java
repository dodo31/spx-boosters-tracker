package com.drawrunner.ui;

import java.awt.Color;

import processing.core.PApplet;
import processing.core.PGraphics;

public class RoundedRectangleFactory {
	public static PGraphics createTopRoundedRect(PApplet p, int width, int radius, Color color) {
		PGraphics topRoundedRect = p.createGraphics(width, radius);
		topRoundedRect.beginDraw();
		topRoundedRect.noStroke();
		topRoundedRect.fill(color.getRGB());
		topRoundedRect.rect(0, 0, topRoundedRect.width, topRoundedRect.height * 2, radius, radius, 0, 0);
		topRoundedRect.endDraw();
		return topRoundedRect;
	}
}
