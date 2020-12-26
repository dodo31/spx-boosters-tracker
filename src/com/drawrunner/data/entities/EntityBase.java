package com.drawrunner.data.entities;

import processing.core.PApplet;

public abstract class EntityBase {
	protected boolean isPopulated;
	
	public EntityBase() {
		isPopulated = false;
	}
	
	public abstract void startDownload(PApplet applet);
	
	protected void notifyPopulated() {
		isPopulated = true;
	}

	public boolean isPopulated() {
		return this.isPopulated;
	}
}
