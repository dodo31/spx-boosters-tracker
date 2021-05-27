package com.drawrunner.ui.status;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import com.drawrunner.ui.ColorTemplate;
import com.drawrunner.ui.Window;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

public class BoostersStatusWindow extends Window {
	public static float TITLE_HEIGHT = 150;
	public static float ORDINATE_INDICATOR_WIDTH = 80;
	public static float ABSCISSA_INDICATOR_HEIGHT = 60;
	public static float CAPTION_MARGIN = 50;

	private PFont axisIndicatorFont;
	
	private PImage caption;
	
	private boolean block5Only;
	private boolean sortByDate;
	
	private BoostersStatus boostersStatus;
	
	public BoostersStatusWindow(PApplet p, PFont titleFont,
			PFont axisIndicatorFont, PImage caption, boolean block5Only) {
		super(p, titleFont);
		
		this.axisIndicatorFont = axisIndicatorFont;
		
		this.caption = caption;
		
		this.block5Only = block5Only;
		sortByDate = false;
		
		boostersStatus = new BoostersStatus(p);
	}
	
	@Override
	protected String generateTitle() {
		DateFormat dateFormat = new SimpleDateFormat("d MMMM yyyy", Locale.ENGLISH);
		dateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
		Date currentDate = new Date();
		
		return "BOOSTERS STATUS - " + dateFormat.format(currentDate).toUpperCase();
	}
	
	public void build(PApplet p) {
		boostersStatus.setPosition(new PVector(ORDINATE_INDICATOR_WIDTH, TITLE_HEIGHT));
		
		boostersStatus.buildStatus();
		
		if(sortByDate) {
			boostersStatus.sortHistories();
		}
		
		if(!block5Only) {
			boostersStatus.addLabel("B1046", "BLOCK 5");
		}
	}
	
	@Override
	public void draw(PGraphics g) {
		this.drawTitle(g);
		this.drawAxisIndicators(g);
		this.drawLegend(g);
		
		boostersStatus.draw(g);
	}
	
	private void drawTitle(PGraphics g) {
		g.textFont(titleFont, Mission.DEFAULT_SIZE * 0.54f);
		
		PVector textMargins = new PVector(32, 8);
		float titleWidth = g.textWidth(title) + textMargins.x * 2;
		float titleHeight = titleFont.getSize() + textMargins.y * 2;
		
		g.textAlign(PApplet.CENTER, PApplet.TOP);
		
		float titlePosX = g.width / 2f;
		float titlePosY = TITLE_HEIGHT / 2f;
		
		g.fill(255, 220);
		g.rect(titlePosX - titleWidth / 2f, titlePosY - titleHeight / 2f, titleWidth, titleHeight);
		
		g.fill(ColorTemplate.BACKGROUND.getRGB(), 220);
		g.text(title, titlePosX, titlePosY - titleHeight / 2f + textMargins.y - 3);
	}
	
	private void drawAxisIndicators(PGraphics g) {
		g.textFont(axisIndicatorFont);
		float arrowsMargins = axisIndicatorFont.getSize();
		
		this.drawAbscissaIndicator(g, arrowsMargins);
		this.drawOrdinateIndicator(g, arrowsMargins);
	}
	
	private void drawAbscissaIndicator(PGraphics g, float arrowsMargins) {
		float absIndicatorPosX = boostersStatus.getPosition().x + boostersStatus.getSize().x / 2f;
		float absIndicatorPosY = boostersStatus.getPosition().y + boostersStatus.getSize().y + axisIndicatorFont.getSize() / 2f + 20;
		this.drawIndicator(g, new PVector(absIndicatorPosX, absIndicatorPosY), "Boosters", arrowsMargins);
	}
	
	private void drawOrdinateIndicator(PGraphics g, float arrowsMargins) {
		float ordIndicatorPosX = boostersStatus.getPosition().x - 45;
		float ordIndicatorPosY = boostersStatus.getPosition().y + boostersStatus.getSize().y / 2f;
		
		g.pushMatrix();
		g.translate(ordIndicatorPosX, ordIndicatorPosY);
		g.rotate(-PApplet.HALF_PI);
		
		this.drawIndicator(g, new PVector(0, 0), "Missions", arrowsMargins);
		
		g.popMatrix();
	}
	
	private void drawIndicator(PGraphics g, PVector position, String text, float arrowsMargins) {
		float indicatorWidth = g.textWidth(text);
		float indicatorHeight = axisIndicatorFont.getSize();
		
		float leftArrowPosX = position.x - indicatorWidth / 2f - arrowsMargins;
		float rightArrowPosX = position.x + indicatorWidth / 2f + arrowsMargins;
		float arrowShiftY = indicatorHeight * 0.14f;
		
		g.fill(255, 196);
		
		g.triangle(leftArrowPosX, position.y - indicatorHeight * 0.5f + arrowShiftY,
			       leftArrowPosX - indicatorHeight * (2 / 3f), position.y + arrowShiftY,
			       leftArrowPosX, position.y + indicatorHeight * 0.5f + arrowShiftY);
		
		g.triangle(rightArrowPosX, position.y - indicatorHeight * 0.5f + arrowShiftY,
				   rightArrowPosX + indicatorHeight * (2 / 3f), position.y + arrowShiftY,
				   rightArrowPosX, position.y + indicatorHeight * 0.5f + arrowShiftY);
		
		g.textAlign(PApplet.CENTER, PApplet.CENTER);
		g.fill(255, 226);
		g.text(text, position.x, position.y);
	}

	private void drawLegend(PGraphics g) {
		float captionPosX = boostersStatus.getPosition().x + boostersStatus.getSize().x / 2f;
		float captionPosY = g.height - caption.height * 0.8f * 0.5f - CAPTION_MARGIN;
		
		float captionRatio = caption.height / (caption.width * 1f);
		float captionHeight = this.captionHeight();
		float captionWidth = captionHeight / captionRatio;
		
		g.noTint();
		g.imageMode(PApplet.CENTER);
		g.image(caption, captionPosX, captionPosY, captionWidth, captionHeight);
	}
	
	public float captionHeight() {
		return caption.height * 0.8f;
	}

	public String getTitle() {
		return this.title;
	}

	public BoostersStatus getBoostersStatus() {
		return this.boostersStatus;
	}

	public boolean isSortByDate() {
		return this.sortByDate;
	}

	public void setSortByDate(boolean sortByDate) {
		this.sortByDate = sortByDate;
	}
}
