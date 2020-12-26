package com.drawrunner.ui.nextlaunch;

import java.awt.Color;

import com.drawrunner.data.entities.LaunchBase;
import com.drawrunner.ui.ColorTemplate;
import com.drawrunner.ui.RoundedRectangleFactory;
import com.drawrunner.ui.SymbolBase;
import com.drawrunner.ui.Window;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;

public class NextLaunchWindow extends Window {
	private String title;
	
	private PFont nextLaunchIndicatorFont;
	private PGraphics topRoundedRect;
	
	private NextLaunch nextLaunch;
	
	public NextLaunchWindow(PApplet p, PFont nextLaunchIndicatorFont, PFont nextLaunchFont, PFont flightInfoFont, PFont boosterIdFont, SymbolBase symbolBase) {
		super(p, nextLaunchFont);
		
		title = this.generateTitle();
		
		this.nextLaunchIndicatorFont = nextLaunchIndicatorFont;
		
		topRoundedRect = RoundedRectangleFactory.createTopRoundedRect(p, p.width, (int) NextLaunch.FRAME_CORNER_RADIUS, new Color(230, 230, 230));
		
		nextLaunch = new NextLaunch(p, flightInfoFont, boosterIdFont, symbolBase);
	}
	
	public void build(PApplet p) {
		nextLaunch.build(p);
		size.set(size.x, nextLaunch.getSize().y);
	}
	
	@Override
	protected String generateTitle() {
		LaunchBase launchBase = LaunchBase.getInstance();
		return launchBase.getNextLaunch().getName();
	}
	
	@Override
	public void draw(PGraphics g) {
		nextLaunch.draw(g);
		this.drawFrame(g);
		this.drawTitle(g);
	}

	private void drawTitle(PGraphics g) {
		g.noTint();
		g.image(topRoundedRect, 0, 0);
		
		g.noStroke();
		g.fill(230, 230, 230);
		g.rect(0, topRoundedRect.height, g.width, NextLaunch.HEADER_HEIGHT - topRoundedRect.height);
		
		g.textAlign(PApplet.CENTER, PApplet.TOP);
		g.textFont(nextLaunchIndicatorFont);
		g.fill(ColorTemplate.BACKGROUND.getRGB());
		g.text("NEXT LAUNCH", g.width * 0.5f, 8);
		
		g.textAlign(PApplet.CENTER, PApplet.BOTTOM);
		g.textFont(titleFont, 46);
		g.text(title, g.width * 0.5f, NextLaunch.HEADER_HEIGHT - 8);
	}
	
	private void drawFrame(PGraphics g) {
		final float FRAME_THINKNESS = 4;
		
		float framePosX = FRAME_THINKNESS * 0.5f - 1;
		float framePosY = FRAME_THINKNESS * 0.5f - 1;
		
		float frameWidth = g.width - FRAME_THINKNESS + 1;
		float frameHeight = g.height - FRAME_THINKNESS + 1;
		
		g.stroke(230, 230, 230);
		g.strokeWeight(FRAME_THINKNESS);
		g.noFill();
		g.rect(framePosX, framePosY, frameWidth, frameHeight, NextLaunch.FRAME_CORNER_RADIUS);
	}
}
