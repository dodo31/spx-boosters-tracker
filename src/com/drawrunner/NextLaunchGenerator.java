package com.drawrunner;

import java.awt.Point;

import com.drawrunner.data.entities.CoreBase;
import com.drawrunner.data.entities.LaunchBase;
import com.drawrunner.data.entities.LaunchpadBase;
import com.drawrunner.data.entities.PayloadBase;
import com.drawrunner.data.entities.RocketBase;
import com.drawrunner.ui.SymbolBase;
import com.drawrunner.ui.nextlaunch.NextLaunchWindow;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;

public class NextLaunchGenerator extends ChartGenerator {
	private CoreBase coreBase;
	private RocketBase rocketBase;
	private LaunchBase launchBase;
	private PayloadBase payloadBase;
	private LaunchpadBase launchpadBase;
	
	private SymbolBase symbolBase;
	
	private NextLaunchWindow launchWindow;
	private PGraphics outputImage;

	private PFont nextLaunchIndicatorFont;
	private PFont nextLaunchFont;
	private PFont boosterIdFont;
	private PFont flightInfoFont;

	public static void main(String[] args) {
		PApplet.main("com.drawrunner.NextLaunchGenerator");
	}
	
	@Override
	public void settings() {
		this.size(1024, 512);
		this.smooth(3);
	}
	
	@Override
	public void setup() {
		this.loadFonts();
		
		coreBase = CoreBase.getInstance();
		rocketBase = RocketBase.getInstance();
		launchBase = LaunchBase.getInstance();
		payloadBase = PayloadBase.getInstance();
		launchpadBase = LaunchpadBase.getInstance();
		
		symbolBase = new SymbolBase(this);
		
		this.startDownloads();
		this.awaitDataDownload();
		this.generateChart();
		
		System.out.println("Drawn !");
	}

	@Override
	protected void loadFonts() {
		nextLaunchIndicatorFont = this.createFont("Arial bold", 24);
		nextLaunchFont = this.createFont("Arial bold", 52);
		boosterIdFont = this.createFont("Arial", 96);
		flightInfoFont = this.createFont("Arial", 36);
	}
	
	@Override
	protected void startDownloads() {
		coreBase.startDownload(this);
		rocketBase.startDownload(this);
		launchBase.startDownload(this);
		payloadBase.startDownload(this);
		launchpadBase.startDownload(this);
	}
	
	@Override
	protected boolean isReadyToGenerate() {
		return coreBase.isPopulated()
			&& rocketBase.isPopulated()
			&& launchBase.isPopulated()
			&& payloadBase.isPopulated()
			&& launchpadBase.isPopulated();
	}
	
	@Override
	protected void generateChart() {
		launchWindow = new NextLaunchWindow(this, nextLaunchIndicatorFont, nextLaunchFont, flightInfoFont, boosterIdFont, symbolBase);
		launchWindow.build(this);
		
		this.drawAll();
		this.exportAsImage();
	}

	@Override
	protected void drawAll() {
		Point outputImageSize = this.outputImageSize();
		outputImage = this.createGraphics(outputImageSize.x, outputImageSize.y);
		outputImage.smooth(3);
		
		surface.setSize(outputImage.width, outputImage.height);
		
		outputImage.beginDraw();
		launchWindow.draw(outputImage);
		outputImage.endDraw();
	}
	
	@Override
	protected Point outputImageSize() {
		int totalHeight = (int) launchWindow.getSize().y;
		return new Point(width, totalHeight);
	}
	
	@Override
	protected void exportAsImage() {
		String missionName = launchWindow.getTitle();
		String outputFilePath = this.localPath() + "/data/records/NEXT LAUNCH - " + missionName.replace("/", "#") + ".png";
		outputImage.save(outputFilePath);
		
		this.exit();
	}
}
