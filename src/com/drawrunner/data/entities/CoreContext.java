package com.drawrunner.data.entities;

import com.drawrunner.constants.Landings;

public class CoreContext extends SpaceEntity {
	private int flightCount;
	private boolean isReused;
	
	private boolean isLandingAttempt;
	private boolean isLandingSuccess;
	private Landings landingType;
	
	public CoreContext(String id) {
		super(id);
		
		flightCount = 0;
		isReused = false;
		
		isLandingAttempt = false;
		isLandingSuccess = false;
		landingType = null;
	}

	public int getFlightCount() {
		return this.flightCount;
	}

	public void setFlightCount(int flightCount) {
		this.flightCount = flightCount;
	}

	public boolean isReused() {
		return this.isReused;
	}

	public void setReused(boolean isReused) {
		this.isReused = isReused;
	}

	public boolean isLandingAttempt() {
		return this.isLandingAttempt;
	}

	public void setLandingAttempt(boolean isLandingAttempt) {
		this.isLandingAttempt = isLandingAttempt;
	}

	public boolean isLandingSuccess() {
		return this.isLandingSuccess;
	}

	public void setLandingSuccess(boolean isLandingSuccess) {
		this.isLandingSuccess = isLandingSuccess;
	}

	public Landings getLandingType() {
		return this.landingType;
	}

	public void setLandingType(Landings landingType) {
		this.landingType = landingType;
	}
	
	public void setLandingType(String landingType) {
		try {
			this.landingType = Enum.valueOf(Landings.class, landingType.toUpperCase());
		} catch (IllegalArgumentException iae) {
			this.landingType = Landings.EXPENDED;
			
			System.err.println("Unknown core context landingType: " + landingType);
			iae.printStackTrace();
		}
	}
}