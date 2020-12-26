package com.drawrunner.data.entities;

import com.drawrunner.constants.Orbits;

public class Payload extends SpaceEntity {
	private String name;
	private String type;
	
	private Orbits orbit;
	private float mass;
	
	private boolean isReused;

	public Payload(String id) {
		super(id);
		
		name = null;
		type = null;
		
		orbit = null;
		mass = -1;
		
		isReused = false;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Orbits getOrbit() {
		return this.orbit;
	}

	public void setOrbit(Orbits orbit) {
		this.orbit = orbit;
	}
	
	public void setOrbit(String orbit) {
		this.orbit = Orbits.stringToOrbit(orbit);
	}

	public float getMass() {
		return this.mass;
	}

	public void setMass(float mass) {
		this.mass = mass;
	}

	public boolean isReused() {
		return this.isReused;
	}

	public void setReused(boolean reused) {
		this.isReused = reused;
	}
}
