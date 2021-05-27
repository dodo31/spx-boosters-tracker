package com.drawrunner.ui.status;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.drawrunner.constants.Features;
import com.drawrunner.constants.Landings;
import com.drawrunner.constants.Orbits;
import com.drawrunner.data.entities.Core;
import com.drawrunner.data.entities.CoreBase;
import com.drawrunner.data.entities.CoreContext;
import com.drawrunner.data.entities.Launch;
import com.drawrunner.data.entities.LaunchBase;
import com.drawrunner.data.entities.PayloadBase;
import com.drawrunner.data.entities.Rocket;
import com.drawrunner.data.entities.RocketBase;
import com.drawrunner.ui.ColorTemplate;
import com.drawrunner.ui.Drawable;
import com.drawrunner.ui.PatchFactory;
import com.drawrunner.ui.SymbolBase;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

public class BoostersStatus extends Drawable {
	public static final PVector HISTORY_MARGINS = new PVector(12, 12);
	
	private PApplet applet;
	
	private SymbolBase symbolBase;

	private CoreBase coreBase;
	private RocketBase rocketBase;
	private LaunchBase launchBase;
	private PayloadBase payloadBase;

	private List<BoosterHistory> boosterHistories;
	private Map<String, String> labels;
	
	private Map<Features, PImage> features;
	
	private PGraphics fullStripedPatch;
	private PGraphics halfStripedPatch;
	
	private PFont historyLabelFont;
	private PFont historyLabelFontBold;
	
	private PFont flagFont;
	
	public BoostersStatus(PApplet applet) {
		this.applet = applet;
		
		symbolBase = new SymbolBase(applet);
		
		coreBase = CoreBase.getInstance();
		rocketBase = RocketBase.getInstance();
		launchBase = LaunchBase.getInstance();
		payloadBase = PayloadBase.getInstance();

		historyLabelFont = this.applet.createFont("Arial", Mission.DEFAULT_SIZE / 3.3f);
		historyLabelFontBold = this.applet.createFont("Arial bold", Mission.DEFAULT_SIZE / 3.3f);
		
		flagFont = this.applet.createFont("Arial bold", Mission.DEFAULT_SIZE / 2.5f);

		boosterHistories = new ArrayList<BoosterHistory>();
		labels = new HashMap<String, String>();
		
		features = new HashMap<Features, PImage>();
		features.put(Features.FALCON_HEAVY, symbolBase.getFalconHeavySymbol());
		features.put(Features.CREW_DRAGON, symbolBase.getCrewDragonSymbol());
		
		this.createSpecialPatchs();
	}
	
	private void createSpecialPatchs() {
		float stripsOrientation = 45;
		PVector stripsBrightnessRange = new PVector(255, 210);
		float stripsHeight = 16;
		float stripsRatio = 0.45f;

		halfStripedPatch = PatchFactory.createHalphStripedPatch(applet, stripsOrientation, stripsHeight, stripsRatio, stripsBrightnessRange);
		fullStripedPatch = PatchFactory.createFullStripedPatch(applet, stripsOrientation, stripsHeight, stripsRatio, stripsBrightnessRange);
	}
	
	public void buildStatus() {
		int maxMissionCount = coreBase.maxMissionCount();
		int coreCount = coreBase.coreCount();
		
		float totalWidth = coreCount * (Mission.DEFAULT_SIZE + BoostersStatus.HISTORY_MARGINS.x * 2);
		float totalHeight = Mission.DEFAULT_SIZE * 0.5f + (maxMissionCount + 0.5f) * (Mission.DEFAULT_SIZE + BoostersStatus.HISTORY_MARGINS.y * 2) + BoostersStatus.HISTORY_MARGINS.y * 10;
		size = new PVector(totalWidth, totalHeight);
		
		int coreIndex = 0;
		
		for (Iterator<Core> itCores = coreBase.getCores(); itCores.hasNext();) {
			Core core = itCores.next();
			this.buildSingleCoreHistory(core, coreIndex);
			
			coreIndex++;
		}
		
		System.out.println("Built !");
	}

	private void buildSingleCoreHistory(Core core, int historyIndex) {
		List<Launch> coreLaunches = launchBase.getLaunches(core.getLaunchIds());
		
		BoosterHistory boosterHistory = new BoosterHistory(core, historyLabelFont, historyLabelFontBold);
		this.addHistory(boosterHistory);
		
		float boosterWidth = Mission.DEFAULT_SIZE;
		float boosterHeight = size.y;
		boosterHistory.setSize(new PVector(boosterWidth, boosterHeight));
		
		float boosterPosX = position.x + (historyIndex - 0) * (boosterWidth + BoostersStatus.HISTORY_MARGINS.x * 2) + BoostersStatus.HISTORY_MARGINS.x;
		float boosterPosY = position.y;
		boosterHistory.setPosition(new PVector(boosterPosX, boosterPosY));

//		boolean hasUpcomingLaunches = false;
		
		for (Launch launch : coreLaunches) {
			this.buildSingleCoreMission(core, launch, boosterHistory);
			
//			if(launch.isUpcoming()) {
//				hasUpcomingLaunches = true;
//			}
		}
		
//		if((core.isActive() && !hasUpcomingLaunches) || coreLaunches.isEmpty()) {
//			boosterHistory.addMission(new Mission());
//		}
	}

	private void buildSingleCoreMission(Core core, Launch launch, BoosterHistory boosterHistory) {
		Rocket rocket = rocketBase.getRocket(launch.getRocketId());
		Mission mission = new Mission();
		
		if(launch.hasLaunchDate() && (!launch.isUpcoming() || (launch.isUpcoming() && core.isActive()))) {
			mission = this.buildPopulatedMission(core, launch, rocket, boosterHistory);
		}
		
		boosterHistory.addMission(mission);
	}

	private Mission buildPopulatedMission(Core core, Launch launch, Rocket rocket, BoosterHistory boosterHistory) {
		String coreId = core.getId();
		CoreContext coreContext = launch.getCoreContext(coreId);
		
		Orbits orbit = payloadBase.highestOrbit(launch.getPayloadIds());
		PImage orbitSymbol = symbolBase.infoToSymbol(orbit.symbolPath);
		PImage landingSymbol = this.establishLandingResult(coreContext.getLandingType());
		
		Mission mission = new Mission(core, launch, orbitSymbol, landingSymbol, halfStripedPatch, fullStripedPatch);
		
		if(rocket.isFalconHeavy()) {
			mission.addFeature(Features.FALCON_HEAVY, symbolBase.getFalconHeavySymbol());
		}
		
		if(launch.isCrewed()) {
			mission.addFeature(Features.CREW_DRAGON, symbolBase.getCrewDragonSymbol());
		}
		
		return mission;
	}

	private PImage establishLandingResult(Landings landing) {
		if(landing != null && landing != Landings.OCEAN) {
			return symbolBase.infoToSymbol(landing.symbolPath);
		} else if(landing == Landings.OCEAN) {
			return symbolBase.getAsdsSymbol();
		} else {
			return null;
		}
	}

	public void sortHistories() {
		Collections.sort(boosterHistories);
		
		for (int i = 0; i < boosterHistories.size(); i++) {
			BoosterHistory boosterHistory = boosterHistories.get(i);
			
			float boosterPosX = position.x + i * (Mission.DEFAULT_SIZE + BoostersStatus.HISTORY_MARGINS.x * 2) + BoostersStatus.HISTORY_MARGINS.x;
			float boosterPosY = position.y;
			boosterHistory.setPosition(new PVector(boosterPosX, boosterPosY));
			
			List<Mission> boosterMissionsBuffer = new ArrayList<Mission>();
			for (int j = 0; j < boosterHistory.missionCount(); j++) {
				Mission mission = boosterHistory.getMission(j);
				boosterMissionsBuffer.add(mission);
			}
			
			for (Mission boosterMission : boosterMissionsBuffer) {
				boosterHistory.removeMission(boosterMission);
			}
			
			for (Mission boosterMission : boosterMissionsBuffer) {
				boosterHistory.addMission(boosterMission);
			}
		}
	}
	
	public void addHistory(BoosterHistory boosterHistory) {
		boosterHistories.add(boosterHistory);
	}
	
	public int historiesCount() {
		return boosterHistories.size();
	}
	
	public void addLabel(String boosterId, String text) {
		int i = 0;
		for(; i < boosterHistories.size() && !boosterHistories.get(i).getCoreSerial().equals(boosterId); i++);
		
		if(i < boosterHistories.size()) {
			labels.put(boosterId, text);
		}
	}
	
	@Override
	public void draw(PGraphics g) {
//		g.fill(255, 0, 0, 127);
//		g.rect(position.x, position.y, size.x, size.y);
		
		for (BoosterHistory boosterHistory : boosterHistories) {
			boosterHistory.draw(g);
		}
		
		for(String boosterName : labels.keySet()) {
			String labelText = labels.get(boosterName);
			this.drawFlag(g, boosterName, labelText);
		}
		
		this.drawSeparator(g);
	}
	
	private void drawSeparator(PGraphics g) {
		float seperatorPosX = position.x;
		float seperatorPosY = position.y + size.y - (BoosterHistory.SERIAL_LABEL_HEIGHT + BoosterHistory.STATUS_LABEL_HEIGHT) * 1.05f;
		
		float seperatorWidth = size.x;
		float seperatorHeight = -Mission.DEFAULT_SIZE / 16f;
		
		g.fill(ColorTemplate.SEPARATOR.getRGB());
		g.noStroke();
		g.rect(seperatorPosX, seperatorPosY, seperatorWidth, seperatorHeight);
	}
	
	private void drawFlag(PGraphics g, String boosterId, String text) {
		final PVector TIMELINE_LABEL_TEXT_MARING = new PVector(30, 20);
		final float TIMELINE_CONNECTOR_WEIGHT = 5;
		
		int i = 0;
		for(; i < boosterHistories.size() && !boosterHistories.get(i).getCoreSerial().equals(boosterId); i++);
		
		if(i < boosterHistories.size()) {
			BoosterHistory boosterHistory = boosterHistories.get(i);
			
			g.textFont(flagFont);
			
			float flagPosX = boosterHistory.getPosition().x - HISTORY_MARGINS.x - TIMELINE_CONNECTOR_WEIGHT / 2f;
			float flagPosY = position.y;

			float flagWidth = g.textWidth(text) + TIMELINE_LABEL_TEXT_MARING.x;
			float flagHeight = flagFont.getSize() + TIMELINE_LABEL_TEXT_MARING.y;
			
			g.fill(255, 220);
			g.noStroke();
			
			float dashedLineHeight = size.y - TIMELINE_CONNECTOR_WEIGHT - flagHeight - Mission.DEFAULT_SIZE * 0.5f;
			for (float j = 0; j < dashedLineHeight; j += 20) {
				float dashHeight = Math.min((size.y - TIMELINE_CONNECTOR_WEIGHT) - j, 12);
				g.rect(flagPosX, flagPosY + flagHeight + j, TIMELINE_CONNECTOR_WEIGHT, dashHeight);
			}
			
			g.beginShape();
			g.vertex(flagPosX, flagPosY);
			g.vertex(flagPosX + flagWidth, flagPosY);
		    g.vertex(flagPosX + flagWidth + flagHeight / 2f, flagPosY + flagHeight / 2f);
		    g.vertex(flagPosX + flagWidth, flagPosY + flagHeight);
		    g.vertex(flagPosX, flagPosY + flagHeight);
		    g.endShape();
			
			g.fill(ColorTemplate.BACKGROUND.getRGB(), 220);
			g.textAlign(PApplet.LEFT, PApplet.TOP);
			g.text(text, flagPosX + TIMELINE_LABEL_TEXT_MARING.x / 2f, flagPosY + TIMELINE_LABEL_TEXT_MARING.y / 5f + 4);
		}
	}
}
