package com.drawrunner.ui.status;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.drawrunner.constants.Status;
import com.drawrunner.data.entities.Core;
import com.drawrunner.ui.ColorTemplate;
import com.drawrunner.ui.Drawable;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PVector;

public class BoosterHistory extends Drawable implements Comparable<BoosterHistory> {
	public static final float SERIAL_LABEL_HEIGHT = Mission.DEFAULT_SIZE * 0.42f;
	public static final float STATUS_LABEL_HEIGHT = Mission.DEFAULT_SIZE * 0.46f;
	public static final float STATUS_LABEL_MARGIN = 6;
	
	private String coreSerial;
	private Status coreStatus;
	
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
		
		if(!coreSerial.contains("1057") || (coreSerial.contains("1057") && mission.isAccomplished())) {
			missions.add(mission);
		}
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
			mainColor = ColorTemplate.LIFTOFF_PARTIAL_FAILURE;
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
	
	@Override
	public int compareTo(BoosterHistory boosterHistory) {
		if(missions.isEmpty() && boosterHistory.isEmpty()) {
			return 0;
		} else if(missions.isEmpty() && !boosterHistory.isEmpty()) {
			return -1;
		} else if(!missions.isEmpty() && boosterHistory.isEmpty()) {
			return 1;
		} else {
			if(this.hasAccomplishedMissions() == boosterHistory.hasAccomplishedMissions()) {
				Date firstCurrentDate = missions.get(0).getDate();
				Date firstOtherDate = boosterHistory.getMission(0).getDate();

				if(firstCurrentDate == null && firstOtherDate == null) {
					return 0;
				} else if(firstCurrentDate == null && firstOtherDate != null) {
					return 1;
				} else if (firstCurrentDate != null && firstOtherDate == null) {
					return -1;
				} else {
					return firstCurrentDate.compareTo(firstOtherDate);
				}
			} else if(!this.hasAccomplishedMissions() && boosterHistory.hasAccomplishedMissions()) {
				return 1;
			} else {
				return -1;
			}
			
//			if(this.hasAccomplishedMissions() == boosterHistory.hasAccomplishedMissions()) {
//				Date lastCurrentDate = missions.get(missions.size() - 1).getDate();
//				Date lastOtherDate = boosterHistory.getMission(boosterHistory.missionCount() - 1).getDate();
//
//				if(lastCurrentDate == null && lastOtherDate == null) {
//					return 0;
//				} else if(lastCurrentDate == null && lastOtherDate != null) {
//					return 1;
//				} else if (lastCurrentDate != null && lastOtherDate == null) {
//					return -1;
//				} else {
//					return lastCurrentDate.compareTo(lastOtherDate);
//				}
//			} else if(!this.hasAccomplishedMissions() && boosterHistory.hasAccomplishedMissions()) {
//				return 1;
//			} else {
//				return -1;
//			}
		}
	}
	
	public boolean hasAccomplishedMissions() {
		boolean hasAccomplishedMissions = false;
		
		for (int i = 0; i < missions.size() && !hasAccomplishedMissions; i++) {
			if(missions.get(i).isAccomplished()) {
				hasAccomplishedMissions = true;
			}
		}
		
		return hasAccomplishedMissions;
	}
	
	public String getCoreSerial() {
		return this.coreSerial;
	}

	public void setCoreSerial(String boosterId) {
		this.coreSerial = boosterId;
	}
}
