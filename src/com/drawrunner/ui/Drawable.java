package com.drawrunner.ui;
import processing.core.PGraphics;
import processing.core.PVector;

public abstract class Drawable {
	protected PVector position;
	protected PVector size;
	
	public Drawable() {
		this.position = new PVector(0, 0);
		this.size = new PVector(0, 0);
	}
	
	public Drawable(float posX, float posY, PVector size) {
		this.position = new PVector(posX, posY);
		this.size = size;
	}
	
	public Drawable(PVector position, float width, float height) {
		this.position = position;
		this.size = new PVector(width, height);
	}
	
	public Drawable(PVector position, PVector size) {
		this.position = position;
		this.size = size;
	}

	public abstract void draw(PGraphics g);
	
	public PVector getPosition() {
		return this.position;
	}

	public void setPosition(PVector position) {
		this.position = position;
	}

	public PVector getSize() {
		return this.size;
	}

	public void setSize(PVector size) {
		this.size = size;
	}
}
