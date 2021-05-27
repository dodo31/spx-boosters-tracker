package com.drawrunner.ui.status;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.drawrunner.constants.CoreStatus;
import com.drawrunner.data.entities.Core;
import com.drawrunner.ui.ColorTemplate;
import com.drawrunner.ui.Drawable;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PVector;

public class BoosterHistory extends Drawable {
	public static final float SERIAL_LABEL_HEIGHT = Mission.DEFAULT_SIZE * 0.42f;
	public static final float STATUS_LABEL_HEIGHT = Mission.DEFAULT_SIZE * 0.46f;
	public static final float STATUS_LABEL_MARGIN = 6;
	
	private String coreSerial;
	private CoreStatus coreStatus;
	
	private List<Mission> missions;
	
	private PFont historyLabelFont;
	private PFont historyLabelFontBold;
	
	public BoosterHistory(Core core, PFont historyLabelFont, PFont historyLabelFontBold) {
		this.coreSerial = core.getSerialNumber();
		this.coreStatus = core.getStatus();
		
		this.missions = new ArrayList<Mission>();
		
		this.historyLabelFont = historyLabelFont;
		this.historyLabelFontBold = historyLabelFontBold;
	}
	
	public void addMission(Mission mission) {
		float missionPosX = position.x;
		float missionPosY = (position.y + size.y) - SERIAL_LABEL_HEIGHT - STATUS_LABEL_HEIGHT - ((missions.size() + 1) * (Mission.DEFAULT_SIZE + BoostersStatus.HISTORY_MARGINS.y * 2));
		
		mission.setPosition(new PVector(missionPosX, missionPosY));
		missions.add(mission);
	}

	public void removeMission(Mission mission) {
		missions.remove(mission);
	}
	
	public Mission getMission(int index) {
		return missions.get(index);
	}
	
	public int missionCount() {
		return missions.size();
	}
	
	public boolean isEmpty() {
		return missions.isEmpty();
	}
	
	@Override
	public void draw(PGraphics g) {
		this.drawSerialLabel(g);
		this.drawStatusLabel(g);
		
		for (int y = 0; y < missions.size(); y++) {
			Mission mission = missions.get(y);
			mission.draw(g);
		}
		
		this.drawCountIndicator(g);
	}

	private void drawSerialLabel(PGraphics g) {
		g.textFont(historyLabelFont);
		g.textAlign(PApplet.CENTER, PApplet.BOTTOM);
		
		g.fill(255);
		g.text(coreSerial, position.x + size.x / 2f, position.y + size.y - STATUS_LABEL_HEIGHT);
	}

	private void drawStatusLabel(PGraphics g) {
		float statusTextSize = historyLabelFont.getSize() * 0.75f;
		g.textFont(historyLabelFontBold, statusTextSize);
		
		float statusCenterX = position.x + size.x / 2f;
		float statusBottomY = position.y + size.y;
		
		float statusLabelWidth = Mission.DEFAULT_SIZE * 0.94f;
		float statusLabelHeight = statusTextSize * 1.25f;
		
		Color mainColor = Color.WHITE;
		String statusAbbrebation = null;
		
		switch (coreStatus) {
		case ACTIVE:
			mainColor = ColorTemplate.LANDING_SUCCESS;
			statusAbbrebation = "ACTIVE";
			break;
		case INACTIVE:
			mainColor = ColorTemplate.LIFTOFF_TOTAL_FAILURE;
			statusAbbrebation = "INACT.";
			break;
		case EXPENDED:
			mainColor = ColorTemplate.LIFTOFF_TOTAL_FAILURE;
			statusAbbrebation = "EXPD.";
			break;
		case LOST:
			mainColor = ColorTemplate.LIFTOFF_TOTAL_FAILURE;
			statusAbbrebation = "LOST";
			break;
		case UNKNOWN:
			mainColor = ColorTemplate.LANDING_SUCCESS;
			statusAbbrebation = "UNKN.";
			break;
		}
		
		g.noStroke();
		g.fill(mainColor.getRGB(), 127);
		g.rect(statusCenterX - statusLabelWidth * 0.5f - STATUS_LABEL_MARGIN,
			   statusBottomY - statusLabelHeight - STATUS_LABEL_MARGIN,
			   statusLabelWidth + (STATUS_LABEL_MARGIN * 2),
			   statusLabelHeight + (STATUS_LABEL_MARGIN * 2)/*, 10*/);
		
		g.fill(mainColor.brighter().brighter().getRGB());
		g.text(statusAbbrebation, statusCenterX, statusBottomY);
	}
	
	private void drawCountIndicator(PGraphics g) {
		int missionCount = missions.size();
		
		if(!missions.isEmpty()) {
			Mission lastMission = missions.get(missionCount - 1);
			PVector missionPosition = lastMission.getPosition();
			PVector missionSize = lastMission.getSize();
			
			if(this.isCoreDead()) {
				g.fill(ColorTemplate.MISSION_COUNT_INDICATOR_UNKNOWN.getRGB());
			} else if(this.isCoreAlive()) {
				g.fill(ColorTemplate.MISSION_COUNT_INDICATOR_UNKNOWN.getRGB());
			} else {
				g.fill(ColorTemplate.MISSION_COUNT_INDICATOR_UNKNOWN.getRGB());
			}
			
			if(missionCount % 10 == 0) {
				g.fill(210, 194, 114);
			}
			
			g.textSize(missionSize.y * 0.45f);
			g.textAlign(PApplet.CENTER, PApplet.BOTTOM);
			g.text(missionCount, missionPosition.x + missionSize.x * 0.5f, missionPosition.y - missionSize.y * 0.2f);
			
			g.stroke(ColorTemplate.SEPARATOR.getRGB());
			g.strokeWeight(3);
			g.line(missionPosition.x + missionSize.x * 0.2f, missionPosition.y - missionSize.y * 0.15f,
				   missionPosition.x + missionSize.x * 0.8f, missionPosition.y - missionSize.y * 0.15f);
		}
	}
	
	public boolean hasAccomplishedMissions() {
		if(missions.isEmpty()) {
			return false;
		} else {
			boolean hasAccomplishedMissions = !missions.isEmpty();
			
			for (int i = 0; i < missions.size() && !hasAccomplishedMissions; i++) {
				if(missions.get(i).isAccomplished()) {
					hasAccomplishedMissions = true;
				}
			}
			
			return hasAccomplishedMissions;
		}
	}
	
	public boolean isCoreAlive() {
		return coreStatus == CoreStatus.ACTIVE
			|| coreStatus == CoreStatus.UNKNOWN;
	}
	
	public boolean isCoreDead() {
		return coreStatus == CoreStatus.EXPENDED
			|| coreStatus == CoreStatus.INACTIVE
			|| coreStatus == CoreStatus.LOST;
	}
	
	public String getCoreSerial() {
		return this.coreSerial;
	}

	public void setCoreSerial(String boosterId) {
		this.coreSerial = boosterId;
	}
}
