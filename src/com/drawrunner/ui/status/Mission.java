package com.drawrunner.ui.status;
import java.awt.Color;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.drawrunner.constants.Features;
import com.drawrunner.constants.MilestoneResults;
import com.drawrunner.data.entities.Core;
import com.drawrunner.data.entities.Launch;
import com.drawrunner.ui.ColorTemplate;
import com.drawrunner.ui.Drawable;

import processing.core.PApplet;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

public class Mission extends Drawable {
	public static final float DEFAULT_SIZE = 100;
	public static final float LOGO_SIZE_FACTOR = 0.76f;
	public static final float BADGES_SIZE = DEFAULT_SIZE * 0.36f;
	public static final float BADGES_MARGIN = BADGES_SIZE * 0.35f;

	private Date date;
	private boolean isAccomplished;
	
	private PImage orbitSymbol;
	private PImage landingSymbol;
	
	private PGraphics partialFailureHalfPatch;
	private PGraphics partialFailureFullPatch;
	
	private MilestoneResults liftoffResult;
	private MilestoneResults landingResult;

	private Map<Features, PImage> featureSymbols;
	
	public Mission() {
		this.size = new PVector(DEFAULT_SIZE, DEFAULT_SIZE);
		
		this.date = null;
		
		this.orbitSymbol = null;
		this.landingSymbol = null;
		
		this.liftoffResult = null;
		this.landingResult = null;
		
		this.partialFailureHalfPatch = null;
		this.partialFailureFullPatch = null;
		
		this.featureSymbols = new HashMap<Features, PImage>();
		
		isAccomplished = false;
	}
	
	public Mission(Core core, Launch launch, PImage orbitSymbol, PImage landingSymbol,
			PGraphics partialFailureHalfPatch, PGraphics partialFailureFullPatch) {
		this.size = new PVector(DEFAULT_SIZE, DEFAULT_SIZE);
		
		this.date = launch.getLaunchDate();
		
		this.orbitSymbol = orbitSymbol;
		this.landingSymbol = landingSymbol;
		
		this.liftoffResult = launch.liftoffResult();
		this.landingResult = launch.landingResult(core.getId());
		
		this.partialFailureHalfPatch = partialFailureHalfPatch;
		this.partialFailureFullPatch = partialFailureFullPatch;
		
		this.featureSymbols = new HashMap<Features, PImage>();
		
		isAccomplished = (liftoffResult != null);
	}

	public void addFeature(Features feature, PImage featureSymbol) {
		featureSymbols.put(feature, featureSymbol);
	}
	
	@Override
	public void draw(PGraphics g) {
		g.noStroke();
		g.imageMode(PApplet.CENTER);
		
		if(orbitSymbol != null) {
			if(liftoffResult != null) {
				switch (liftoffResult) {
				case FAILURE:
					this.drawLiftOffPatch(g, ColorTemplate.LIFTOFF_TOTAL_FAILURE);
					break;
				case PARTIAL_FAILURE:
					this.drawLiftOffPatch(g, ColorTemplate.LIFTOFF_PARTIAL_FAILURE);				
					break;
				case SUCCESS:
					this.drawLiftOffPatch(g, ColorTemplate.LIFTOFF_SUCCESS);
					break;
				}
				
				if(landingSymbol != null) {
					switch (landingResult) {
					case FAILURE:
						this.drawLandingPatch(g, ColorTemplate.LANDING_TOTAL_FAILURE);
						break;
					case PARTIAL_FAILURE:
						this.drawLandingPatch(g, ColorTemplate.LANDING_OCEAN_LANDING);				
						break;
					case SUCCESS:
						this.drawLandingPatch(g, ColorTemplate.LANDING_SUCCESS);
						break;
					}
				}
			} else {
				this.drawFutureMissionPatch(g);
			}
		} else {
			this.drawFutureMissionPatch(g);
		}
		
		if(!featureSymbols.isEmpty()) {
			this.drawFeatureBadges(g);
		}
		
		if(date != null) {
			if(this.isRecentLaunch()) {
				g.ellipseMode(PApplet.CENTER);
				g.fill(255);
				g.stroke(ColorTemplate.BACKGROUND.getRGB());
				g.strokeWeight(BADGES_SIZE * 0.16f);
				g.ellipse(position.x + size.x, position.y, DEFAULT_SIZE * 0.3f, DEFAULT_SIZE * 0.3f);
			}
		}
	}

	private boolean isRecentLaunch() {
		if(date != null) {
			Date todayDate = new Date();
			LocalDate todayLocalDate = todayDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			LocalDate launchLocalDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
			
			int dayDiff = (int) Math.abs(todayLocalDate.toEpochDay() - launchLocalDate.toEpochDay());
			
			return dayDiff < 3;
		} else {
			return false;
		}
	}
	
	private void drawLiftOffPatch(PGraphics g, Color backgroundColor) {
		float orbitLogoRatio = orbitSymbol.height / (orbitSymbol.width * 1f);
		
		g.fill(backgroundColor.getRGB());
		
		if(landingSymbol != null) {
			g.tint(255, 150);
			g.rect(position.x, position.y, size.x / 2f, size.y, DEFAULT_SIZE / 5f, 0, 0, DEFAULT_SIZE / 5f);
			g.tint(ColorTemplate.PATCH_SYMBOL_TINT.getRGB());
			g.image(orbitSymbol, position.x + size.x / 4f, position.y + size.y / 2f, DEFAULT_SIZE * LOGO_SIZE_FACTOR, (DEFAULT_SIZE * orbitLogoRatio) * LOGO_SIZE_FACTOR);
		} else {
			if(backgroundColor == ColorTemplate.LIFTOFF_PARTIAL_FAILURE) {
				g.tint(ColorTemplate.LANDING_OCEAN_LANDING.getRGB());
				g.image(partialFailureFullPatch, position.x + size.x / 2f, position.y + size.y / 2f);
			} else if(backgroundColor == ColorTemplate.LIFTOFF_TOTAL_FAILURE) {
				g.tint(ColorTemplate.LIFTOFF_TOTAL_FAILURE.getRGB());
				g.image(partialFailureFullPatch, position.x + size.x / 2f, position.y + size.y / 2f);
			} else {
				g.rect(position.x, position.y, size.x, size.y, DEFAULT_SIZE / 5f);
			}
			
			g.tint(255, 150);
			g.tint(ColorTemplate.PATCH_SYMBOL_TINT.getRGB());
			g.image(orbitSymbol, position.x + size.x / 2f, position.y + size.y / 2f, DEFAULT_SIZE * LOGO_SIZE_FACTOR, (DEFAULT_SIZE * orbitLogoRatio) * LOGO_SIZE_FACTOR);
		}
	}
	
	private void drawLandingPatch(PGraphics g, Color backgroundColor) {
		g.fill(backgroundColor.getRGB());
		
		if(landingSymbol != null) {
			float landingLogoRatio = orbitSymbol.height / (orbitSymbol.width * 1f);
			
			if(backgroundColor == ColorTemplate.LANDING_OCEAN_LANDING) {
				g.tint(ColorTemplate.LANDING_OCEAN_LANDING.getRGB());
				g.image(partialFailureHalfPatch, position.x + size.x * (3 / 4f), position.y + size.y / 2f);
			} else if(backgroundColor == ColorTemplate.LANDING_TOTAL_FAILURE) {
				g.tint(ColorTemplate.LANDING_TOTAL_FAILURE.getRGB());
				g.image(partialFailureHalfPatch, position.x + size.x * (3 / 4f), position.y + size.y / 2f);
			} else {
				g.rect(position.x + size.x / 2f, position.y, size.x / 2f, size.y, 0, DEFAULT_SIZE / 5f, DEFAULT_SIZE / 5f, 0);
			}
			
			g.tint(255, 150);
			g.tint(ColorTemplate.PATCH_SYMBOL_TINT.getRGB());
			g.image(landingSymbol, position.x + (3 * size.x / 4f), position.y + size.y / 2f, DEFAULT_SIZE * LOGO_SIZE_FACTOR, (DEFAULT_SIZE * landingLogoRatio) * LOGO_SIZE_FACTOR);
		}
	}
	
	private void drawFutureMissionPatch(PGraphics g) {
		g.fill(ColorTemplate.FUTURE_MISSION_BACKGROUND.getRGB());
//		p.stroke(ColorTemplate.FUTURE_MISSION_STROKE.getRGB());
		g.strokeWeight(DEFAULT_SIZE / 35f);
		
		g.tint(ColorTemplate.PATCH_SYMBOL_BRIGHT_TINT.getRGB());
		if(orbitSymbol != null) {
			float orbitLogoRatio = orbitSymbol.height / (orbitSymbol.width * 1f);
			
			if(landingSymbol != null) {
				float landingLogoRatio = landingSymbol.height / (orbitSymbol.width * 1f);
				
				g.rect(position.x, position.y, size.x / 2f, size.y, DEFAULT_SIZE / 5f, 0, 0, DEFAULT_SIZE / 5f);
				g.image(orbitSymbol, position.x + size.x / 4f, position.y + size.y / 2f, DEFAULT_SIZE * LOGO_SIZE_FACTOR, (DEFAULT_SIZE * orbitLogoRatio) * LOGO_SIZE_FACTOR);
				
				g.rect(position.x + size.x / 2f, position.y, size.x / 2f, size.y, 0, DEFAULT_SIZE / 5f, DEFAULT_SIZE / 5f, 0);
				g.image(landingSymbol, position.x + (3 * size.x / 4f), position.y + size.y / 2f, DEFAULT_SIZE * LOGO_SIZE_FACTOR, (DEFAULT_SIZE * landingLogoRatio) * LOGO_SIZE_FACTOR);
			} else {
				g.rect(position.x, position.y, size.x, size.y, DEFAULT_SIZE / 5f);
				g.image(orbitSymbol, position.x + size.x / 2f, position.y + size.y / 2f, DEFAULT_SIZE * LOGO_SIZE_FACTOR, (DEFAULT_SIZE * orbitLogoRatio) * LOGO_SIZE_FACTOR);
			}
		} else {
			g.rect(position.x, position.y, size.x, size.y, DEFAULT_SIZE / 5f);
		}
		g.noTint();
	}
	
	public void drawFeatureBadges(PGraphics g) {
		float badgesSectionWidth = featureSymbols.size() * (BADGES_SIZE + BADGES_MARGIN) - BADGES_MARGIN;
		float startPosX = position.x + size.x / 2f - badgesSectionWidth / 2f;
		float posY = position.y + size.y - BADGES_SIZE * 0.74f;
		
		g.fill(255);
		g.stroke(ColorTemplate.BACKGROUND.getRGB());
		g.strokeWeight(BADGES_SIZE * 0.16f);
		g.noTint();
		
		int symbolIndex = 0;
		
		for (PImage featureSymbol : featureSymbols.values()) {
			float posX = startPosX + symbolIndex * (BADGES_SIZE + BADGES_MARGIN);
			
			float fittingRatio = BADGES_SIZE / (featureSymbol.width * 1f);
			
			g.rect(posX, posY, BADGES_SIZE, BADGES_SIZE, BADGES_SIZE * 0.35f);
			
			g.tint(ColorTemplate.BACKGROUND.getRGB());
			g.image(featureSymbol, posX + BADGES_SIZE / 2f, posY + BADGES_SIZE / 2f, featureSymbol.width * fittingRatio, featureSymbol.height * fittingRatio);
			
			symbolIndex++;
		}
	}
	
	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public MilestoneResults getLiftoffResult() {
		return this.liftoffResult;
	}

	public void setLiftoffResult(MilestoneResults liftoffResult) {
		this.liftoffResult = liftoffResult;
	}

	public MilestoneResults getLandingResult() {
		return this.landingResult;
	}

	public void setLandingResult(MilestoneResults landingResult) {
		this.landingResult = landingResult;
	}
	
	public boolean isAccomplished() {
		return isAccomplished;
	}
}
