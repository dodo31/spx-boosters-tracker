package com.drawrunner.ui.nextlaunch;

import java.awt.Color;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.drawrunner.constants.Landings;
import com.drawrunner.constants.Orbits;
import com.drawrunner.data.entities.Core;
import com.drawrunner.data.entities.CoreBase;
import com.drawrunner.data.entities.CoreContext;
import com.drawrunner.data.entities.Launch;
import com.drawrunner.data.entities.LaunchBase;
import com.drawrunner.data.entities.Launchpad;
import com.drawrunner.data.entities.LaunchpadBase;
import com.drawrunner.data.entities.PayloadBase;
import com.drawrunner.ui.ColorTemplate;
import com.drawrunner.ui.Drawable;
import com.drawrunner.ui.RoundedRectangleFactory;
import com.drawrunner.ui.SymbolBase;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

public class NextLaunch extends Drawable {
	public static final float HEADER_HEIGHT = 105;
	public static final float BOOSTER_SECTIONS_MARGIN = 55;
	public static final float FRAME_CORNER_RADIUS = HEADER_HEIGHT * 0.5f;

	private List<BoosterSection> boosterSections;
	
	private PFont flightInfoFont;
	private PFont boosterIdFont;
	private PGraphics botRoundedRect;
	
	private LaunchBase launchBase;
	private CoreBase coreBase;
	private PayloadBase payloadBase;
	private LaunchpadBase launchpadBase;
	
	private SymbolBase symbolBase;
	
	public NextLaunch(PApplet p, PFont flightInfoFont, PFont boosterIdFont, SymbolBase symbolBase) {
		super(new PVector(0, 0), new PVector(p.width, p.height));
		
		boosterSections = new LinkedList<BoosterSection>();
		
		this.flightInfoFont = flightInfoFont;
		this.boosterIdFont = boosterIdFont;
		
		botRoundedRect = null;
		
		launchBase = LaunchBase.getInstance();
		coreBase = CoreBase.getInstance();
		payloadBase = PayloadBase.getInstance();
		launchpadBase = LaunchpadBase.getInstance();
		
		this.symbolBase = symbolBase;
	}

	public void build(PApplet p) {
		Launch nextLaunch = launchBase.getNextLaunch();

		String[] coreIds = nextLaunch.coreIds();
		List<Core> cores = coreBase.getCores(coreIds);
		
		Map<String, CoreContext> coreContexts = nextLaunch.getCoreContexts();
		
		String launchpadId = nextLaunch.getLaunchpadId();
		Launchpad launchpad = launchpadBase.getLaunchpad(launchpadId);
		
		String[] payloadIds = nextLaunch.getPayloadIds();
		
		int payloadsMass = Math.round(payloadBase.totalSetMass(payloadIds));
		boolean isRealMass = payloadBase.hasSetRealMass(payloadIds);
		
		Orbits highestOrbit = payloadBase.highestOrbit(payloadIds);
		
		float cursorY = HEADER_HEIGHT;
		cursorY = this.buildBoosterSection(p, launchpad, coreContexts, cores, payloadsMass, isRealMass, highestOrbit, cursorY);
		
		Color bottomRectangleColor = null;
		
		if(boosterSections.size() % 2 == 0) {
			bottomRectangleColor = ColorTemplate.ODD_BOOSTER_SECTION_BACKGROUND;
		} else {
			bottomRectangleColor = ColorTemplate.EVEN_BOOSTER_SECTION_BACKGROUND;
		}
		
		botRoundedRect = RoundedRectangleFactory.createTopRoundedRect(p, (int) size.x, (int) (FRAME_CORNER_RADIUS * 1.15f), bottomRectangleColor);
		
		size.set(size.x, cursorY);
	}
	
	private float buildBoosterSection(PApplet p, Launchpad launchpad, Map<String, CoreContext> coreContexts,
			List<Core> cores, int payloadsMass, boolean isRealMass, Orbits highestOrbit, float cursorY) {
		PImage padSymbol = symbolBase.infoToSymbol(launchpad.getSymbolPath());
		PImage payloadSymbol = symbolBase.getPayloadSymbol();
		
		int coreIndex = 0;
		
		for(Core core : cores) {
			CoreContext coreContext = coreContexts.get(core.getId());
			String coreSerial = core.getSerialNumber();
			int coreFlightNumber = coreContext.getFlightCount();
			String coreDemonimation = coreSerial + "." + (coreFlightNumber >= 0 ? coreFlightNumber : "?");
			
			BoosterSection boosterSection = new BoosterSection(p, coreDemonimation, coreFlightNumber, cursorY, boosterIdFont);
			boosterSections.add(boosterSection);
			
			String padName = launchpad.getShortName();
			
			PImage orbitSymbol = symbolBase.infoToSymbol(highestOrbit.symbolPath);
			
			Landings landing = coreContext.getLandingType();
			PImage landingSymbol = symbolBase.infoToSymbol(landing.symbolPath);
			
			String massText = (isRealMass ? "" : "> ") + payloadsMass + "kg";
			
			FlightInfo orbitInfo = new FlightInfo(p, orbitSymbol, highestOrbit.name, (highestOrbit != Orbits.SO ? "Orbit" : "Trajectory"), ColorTemplate.LIFTOFF_INFO, flightInfoFont);
			FlightInfo landingInfo = new FlightInfo(p, landingSymbol, landing.name, (landing != Landings.EXPENDED ? "Landing" : "(No landing)"), ColorTemplate.LANDING_INFO, flightInfoFont);
			FlightInfo padInfo = new FlightInfo(p, padSymbol, padName.replace(" ", "-"), "Launch pad", ColorTemplate.PAD_INFO, flightInfoFont);
			FlightInfo payloadInfo = new FlightInfo(p, payloadSymbol, massText, "Payload mass", ColorTemplate.PAYLOAD_INFO, flightInfoFont);
			
			boosterSection.addFlightInfo(orbitInfo);
			boosterSection.addFlightInfo(landingInfo);
			boosterSection.addFlightInfo(padInfo);
			boosterSection.addFlightInfo(payloadInfo);

			cursorY += boosterSection.getSize().y;
			
			if(coreIndex == cores.size() - 1) {
				cursorY += BOOSTER_SECTIONS_MARGIN;
			}
			
			coreIndex++;
		}
		
		return cursorY;
	}
	
	@Override
	public void draw(PGraphics g) {
		this.drawFooterBackground(g);
		this.drawBoosterSection(g);
	}
	
	private void drawBoosterSection(PGraphics g) {
		for(int i = 0; i < boosterSections.size(); i++) {
			BoosterSection boosterSection = boosterSections.get(i);
			
			float separatorPosY = boosterSection.getPosition().y - BOOSTER_SECTIONS_MARGIN * 0.35f;
			
			this.drawBoosterSectionBackground(g, i, boosterSection, separatorPosY);
			boosterSection.draw(g);
			
			if(i > 0) {
				this.drawBoosterSectionsSeparator(g, separatorPosY);
			}
		}
	}

	private void drawFooterBackground(PGraphics g) {
		g.pushMatrix();
		g.translate(g.width, size.y);
		g.rotate(PApplet.PI);
		g.noTint();
		g.image(botRoundedRect, 0, 0);
		g.popMatrix();
	}
	
	private void drawBoosterSectionBackground(PGraphics g, int sectionIndex, BoosterSection boosterSection, float separatorPosY) {
		Color boosterSectionBackground = ColorTemplate.BACKGROUND;
		if(sectionIndex % 2 == 0) {
			boosterSectionBackground = ColorTemplate.EVEN_BOOSTER_SECTION_BACKGROUND;
		} else {
			boosterSectionBackground = ColorTemplate.ODD_BOOSTER_SECTION_BACKGROUND;
		}
		
		float boosterSectionBackgroundWidth = g.width;
		float boosterSectionBackgroundHeight = boosterSection.getSize().y + 1;
		
		if(sectionIndex < boosterSections.size() - 1) {
			boosterSectionBackgroundHeight += BOOSTER_SECTIONS_MARGIN;
		} else {
			boosterSectionBackgroundHeight += BOOSTER_SECTIONS_MARGIN * 0.35f;
		}
		
		g.noStroke();
		g.fill(boosterSectionBackground.getRGB());
		g.rect(0, separatorPosY, boosterSectionBackgroundWidth, boosterSectionBackgroundHeight);
	}
	
	private void drawBoosterSectionsSeparator(PGraphics g, float posY) {
		final float DASHES_WIDTH = 30;
		
		g.stroke(255, 90);
		g.strokeWeight(4);
		g.strokeCap(PApplet.SQUARE);
		
		for(int x = 0; x < g.width; x += DASHES_WIDTH) {
			g.line(x, posY, x + DASHES_WIDTH * 0.65f, posY);
		}
	}
}
