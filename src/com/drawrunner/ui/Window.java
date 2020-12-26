package com.drawrunner.ui;

import processing.core.PApplet;
import processing.core.PFont;
import processing.core.PVector;

public abstract class Window extends Drawable {
	protected String title;
	protected PFont titleFont;
	
	public Window(PApplet p, PFont titleFont) {
		super(new PVector(0, 0), new PVector(p.width, p.height));
		
		this.title = this.generateTitle();
		this.titleFont = titleFont;
	}
	
	protected abstract String generateTitle();
	
	public String getTitle() {
		return this.title;
	}
}
