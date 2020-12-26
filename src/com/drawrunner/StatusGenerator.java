package com.drawrunner;

import java.awt.Point;
import java.util.Arrays;
import java.util.Objects;

import com.drawrunner.data.entities.CoreBase;
import com.drawrunner.data.entities.LaunchBase;
import com.drawrunner.data.entities.PayloadBase;
import com.drawrunner.data.entities.RocketBase;
import com.drawrunner.ui.ColorTemplate;
import com.drawrunner.ui.status.BoostersStatusWindow;
import com.drawrunner.ui.status.Mission;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PGraphics;
import processing.core.PImage;
import processing.core.PVector;

public class StatusGenerator extends ChartGenerator {
	public static float RIGHT_BLANK = 50;
	
	private boolean block5Only;
	
	private CoreBase coreBase;
	private RocketBase rocketBase;
	private LaunchBase launchBase;
	private PayloadBase payloadBase;
	
	private BoostersStatusWindow statusWindow;
	private PGraphics outputImage;

	private PImage caption;
	
	private PFont titleFont;
	private PFont axisIndicatorFont;
	
	public static void main(String[] args) {
		PApplet.main("com.drawrunner.StatusGenerator", args);
	}
	
	@Override
	public void settings() {
		this.size(1920, 1080, P2D);
		this.smooth(3);
	}
	
	@Override
	public void setup() {
		block5Only = false;
		
		if(!Objects.isNull(args)) {
			block5Only = Arrays.asList(args).contains("BLOCK-5");
		}
		
		this.loadFonts();
		
		if(block5Only) {
			caption = this.loadImage("data/images/Caption - Short.png");
		} else {
			caption = this.loadImage("data/images/Caption - Long.png");
		}
		
		coreBase = CoreBase.getInstance();
		rocketBase = RocketBase.getInstance();
		launchBase = LaunchBase.getInstance();
		payloadBase = PayloadBase.getInstance();

		if(block5Only) {
			coreBase.setMinBlock(5);
		}
		
		this.startDownloads();
		this.awaitDataDownload();
		this.generateChart();
		
		System.out.println("Drawn !");
	}

	@Override
	protected void loadFonts() {
		axisIndicatorFont = this.createFont("Arial bold", Mission.DEFAULT_SIZE / 2.8f);
		titleFont = this.createFont("Arial bold", Mission.DEFAULT_SIZE * 0.54f);
	}
	
	@Override
	protected void startDownloads() {
		coreBase.startDownload(this);
		rocketBase.startDownload(this);
		launchBase.startDownload(this);
		payloadBase.startDownload(this);
	}
	
	@Override
	protected boolean isReadyToGenerate() {
		return coreBase.isPopulated()
			&& rocketBase.isPopulated()
			&& launchBase.isPopulated()
			&& payloadBase.isPopulated();
	}
	
	@Override
	protected void generateChart() {
		statusWindow = new BoostersStatusWindow(this, titleFont, axisIndicatorFont, caption, block5Only);
		statusWindow.setSortByDate(true);
		statusWindow.build(this);
		
		this.drawAll();
		this.exportAsImage();
	}

	@Override
	protected void drawAll() {
		outputImage = this.createGraphics(width, height, P2D);
		outputImage.smooth(3);
		
		Point outputImageSize = this.outputImageSize();
		outputImage.setSize(outputImageSize.x, outputImageSize.y);
		
		outputImage.beginDraw();
		outputImage.background(ColorTemplate.BACKGROUND.getRGB());
		statusWindow.draw(outputImage);
		outputImage.endDraw();
	}
	
	@Override
	protected Point outputImageSize() {
		PVector statusSize = statusWindow.getBoostersStatus().getSize();
		float captionTotalHeight = statusWindow.captionHeight() + BoostersStatusWindow.CAPTION_MARGIN * 2f;
		
		int outputImageWidth = (int) (BoostersStatusWindow.ORDINATE_INDICATOR_WIDTH + statusSize.x + RIGHT_BLANK);
		int outputImageHeight = (int) (BoostersStatusWindow.TITLE_HEIGHT + statusSize.y + BoostersStatusWindow.ABSCISSA_INDICATOR_HEIGHT + captionTotalHeight);
		return new Point(outputImageWidth, outputImageHeight);
	}
	
	@Override
	protected void exportAsImage() {
		String sortedNamePart = (statusWindow.isSortByDate() ? " [SORTED]" : "");
		String block5NamePart = (block5Only ? " [BLOCK 5]" : "");
		
		String outputImagePath = this.localPath() + "/data/Records/" + statusWindow.getTitle() + sortedNamePart + block5NamePart + ".png";
		outputImage.save(outputImagePath);
		
		this.exit();
	}
}
