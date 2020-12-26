package com.drawrunner.ui.nextlaunch;

import java.util.ArrayList;
import java.util.List;

import com.drawrunner.ui.Drawable;

import processing.core.PGraphics;
import processing.core.PVector;

public class FlightInfosSection extends Drawable {
	public final static float TOP_MARGIN = FlightInfo.SYMBOL_SIZE * 0.15f;
	public final static float INFOS_MARGIN = FlightInfo.SYMBOL_SIZE * 0.5f;
	
	private List<FlightInfo> flightInfos;
	
	public FlightInfosSection() {
		flightInfos = new ArrayList<FlightInfo>();
		size = new PVector(size.x, TOP_MARGIN + FlightInfo.SYMBOL_SIZE + FlightInfo.TEXT_HEIGHT);
	}
	
	public void addFlightInfo(FlightInfo newFlightInfo) {
		flightInfos.add(newFlightInfo);
		this.updateFlightInfosPosition();
	}
	
	public void updateFlightInfosPosition() {
		float totalWidth = this.allFlightInfosWidth();
		
		float centerX = position.x + size.x * 0.5f;
		float cursorX = centerX - totalWidth * 0.5f;
		
		for(int i = 0; i < flightInfos.size(); i++) {
			FlightInfo flightInfo = flightInfos.get(i);
			flightInfo.setPosition(new PVector(cursorX, position.y));

			cursorX += flightInfo.getSize().x;
			
			if(i < flightInfos.size() - 1) {
				cursorX += INFOS_MARGIN;
			}
		}
	}
	
	private float allFlightInfosWidth() {
		float totalWidth = 0;
		
		for(FlightInfo flightInfo : flightInfos) {
			totalWidth += flightInfo.getSize().x;
		}
		
		return totalWidth + (INFOS_MARGIN * (flightInfos.size() - 1));
	}
	
	@Override
	public void draw(PGraphics g) {
		for (FlightInfo flightInfo : flightInfos) {
			flightInfo.draw(g);
		}
	}
}
