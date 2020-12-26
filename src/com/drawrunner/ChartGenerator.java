package com.drawrunner;

import java.awt.Point;

import processing.core.PApplet;

public abstract class ChartGenerator extends PApplet {
	protected abstract void loadFonts();
	protected abstract void startDownloads();
	
	protected void awaitDataDownload() {
		while (!this.isReadyToGenerate()) {
			try {
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("Downloaded !");
	}
	
	protected abstract boolean isReadyToGenerate();
	
	protected abstract void generateChart();
	protected abstract Point outputImageSize();
	
	protected abstract void drawAll();
	protected abstract void exportAsImage();
	
	protected String localPath() {
		return System.getProperty("user.dir");
	}
}
