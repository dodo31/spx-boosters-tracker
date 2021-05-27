package com.drawrunner.data.entities;

import com.drawrunner.constants.CoreStatus;

public class Core extends SpaceEntity {
	private String serialNumber;
	private CoreStatus status;
	
	private int blockNumber;
	private int reuseCount;
	
	private int rtlsAttemptCount;
	private int rtlsLandingCount;
	
	private int asdsAttemptCount;
	private int asdsLandingCount;
	
	private String[] launchIds;
	
	public Core(String id) {
		super(id);
		
		serialNumber = null;
		status = null;
		
		blockNumber = -1;
		reuseCount = 0;
		
		rtlsAttemptCount = 0;
		rtlsLandingCount = 0;
		
		asdsAttemptCount = 0;
		asdsLandingCount = 0;
		
		launchIds = new String[0];
	}

	public boolean isActive() {
		return status == CoreStatus.ACTIVE;
	}
	
	public boolean hasBeenLaunched() {
		return launchIds.length > 0;
	}
	
	public int launchCount() {
		return launchIds.length;
	}
	
	public String getSerialNumber() {
		return this.serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public CoreStatus getStatus() {
		return this.status;
	}

	public void setStatus(CoreStatus status) {
		this.status = status;
	}

	public void setStatus(String status) {
		try {
			this.status = Enum.valueOf(CoreStatus.class, status.toUpperCase());
		} catch (IllegalArgumentException iae) {
			this.status = CoreStatus.UNKNOWN;
			
			System.err.println("Unknown core status: " + status);
			iae.printStackTrace();
		}
	}
	
	public int getBlockNumber() {
		return this.blockNumber;
	}

	public void setBlockNumber(int blockNumber) {
		this.blockNumber = blockNumber;
	}

	public int getReuseCount() {
		return this.reuseCount;
	}

	public void setReuseCount(int reuseCount) {
		this.reuseCount = reuseCount;
	}

	public int getRtlsAttemptCount() {
		return this.rtlsAttemptCount;
	}

	public void setRtlsAttemptCount(int rtlsAttemptCount) {
		this.rtlsAttemptCount = rtlsAttemptCount;
	}

	public int getRtlsLandingCount() {
		return this.rtlsLandingCount;
	}

	public void setRtlsLandingCount(int rtlsLandingCount) {
		this.rtlsLandingCount = rtlsLandingCount;
	}

	public int getAsdsAttemptCount() {
		return this.asdsAttemptCount;
	}

	public void setAsdsAttemptCount(int asdsAttemptCount) {
		this.asdsAttemptCount = asdsAttemptCount;
	}

	public int getAsdsLandingCount() {
		return this.asdsLandingCount;
	}

	public void setAsdsLandingCount(int asdsLandingCount) {
		this.asdsLandingCount = asdsLandingCount;
	}
	
	public String[] getLaunchIds() {
		return this.launchIds;
	}

	public void setLaunchIds(String[] launchIds) {
		this.launchIds = launchIds;
	}
}
