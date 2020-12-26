package com.drawrunner.data.entities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import com.drawrunner.constants.Landings;
import com.drawrunner.constants.MilestoneResults;

public class Launch extends SpaceEntity {
	private static final Integer[] PARTIALLY_FAILED_FLIGHT_IDS = new Integer[] { 9 };
	
	private int flightNumber;
	private String name;
	private Date launchDate;
	
	private boolean isSuccess; 
	private boolean isUpcoming;
	
	private Map<String, CoreContext> coreContexts;
	private String rocketId;
	
	private String[] payloadIds;
	private String[] capsuleIds;
	private String[] crewIds;
	
	private String launchpadId;

	public Launch(String id) {
		super(id);
		
		flightNumber = 0;
		name = null;
		launchDate = new Date();
		
		isSuccess = false;
		isUpcoming = false;
		
		coreContexts = new HashMap<String, CoreContext>();
		rocketId = null;
		
		payloadIds = new String[0];
		capsuleIds = new String[0];
		crewIds = new String[0];
		
		launchpadId = null;
	}

	public MilestoneResults liftoffResult() {
		if(Arrays.asList(PARTIALLY_FAILED_FLIGHT_IDS).contains(flightNumber)) {
			return MilestoneResults.PARTIAL_FAILURE;
		} else {
			if(isSuccess) {
				return MilestoneResults.SUCCESS;
			} else {
				return MilestoneResults.FAILURE;
			}
		}
	}
	
	public MilestoneResults landingResult(String coreId) {
		CoreContext coreContext = this.getCoreContext(coreId);
		
		if(!Objects.isNull(coreContext)) {
			if(coreContext.getLandingType() == Landings.OCEAN) {
				return MilestoneResults.PARTIAL_FAILURE;
			} else {
				if(coreContext.isLandingSuccess()) {
					return MilestoneResults.SUCCESS;
				} else {
					return MilestoneResults.FAILURE;
				}
			}
		} else {
			return null;
		}
	}
	
	public String[] coreIds() {
		String[] coreIds = new String[coreContexts.size()];
		
		int coreContextIndex = 0;
		
		for (String coreId : coreContexts.keySet()) {
			coreIds[coreContextIndex] = coreId;
			coreContextIndex++;
		}
		
		return coreIds;
	}
	
	public CoreContext getCoreContext(String coreId) {
		return coreContexts.get(coreId);
	}
	
	public boolean hasLaunchDate() {
		return !Objects.isNull(launchDate);
	}
	
	public boolean isCrewed() {
		return crewIds.length > 0;
	}
	
	public int getFlightNumber() {
		return this.flightNumber;
	}

	public void setFlightNumber(int flightNumber) {
		this.flightNumber = flightNumber;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getLaunchDate() {
		return this.launchDate;
	}
	
	public void setLaunchDate(Date launchDate) {
		this.launchDate = launchDate;
	}
	
	public void setLaunchDate(String launchDate) {
		try {
			this.launchDate = new SimpleDateFormat("yyyy-MM-dd").parse(launchDate);
		} catch (ParseException e) {
			System.err.println("Error parsing launch date: " + launchDate);
			e.printStackTrace();
		}
	}

	public boolean isSuccess() {
		return this.isSuccess;
	}

	public void setSuccess(boolean isSuccess) {
		this.isSuccess = isSuccess;
	}

	public boolean isUpcoming() {
		return this.isUpcoming;
	}

	public void setUpcoming(boolean isUpcoming) {
		this.isUpcoming = isUpcoming;
	}

	public Map<String, CoreContext> getCoreContexts() {
		return this.coreContexts;
	}

	public void setCoreContexts(CoreContext[] coreContexts) {
		for (CoreContext coreContext : coreContexts) {
			this.coreContexts.put(coreContext.getId(), coreContext);
		}
	}

	public String getRocketId() {
		return this.rocketId;
	}

	public void setRocketId(String rocketId) {
		this.rocketId = rocketId;
	}

	public String[] getPayloadIds() {
		return this.payloadIds;
	}

	public void setPayloadIds(String[] payloadIds) {
		this.payloadIds = payloadIds;
	}

	public String[] getCapsuleIds() {
		return this.capsuleIds;
	}

	public void setCapsuleIds(String[] capsuleIds) {
		this.capsuleIds = capsuleIds;
	}

	public String[] getCrewIds() {
		return this.crewIds;
	}

	public void setCrewIds(String[] crewIds) {
		this.crewIds = crewIds;
	}

	public String getLaunchpadId() {
		return this.launchpadId;
	}

	public void setLaunchpadId(String launchpadId) {
		this.launchpadId = launchpadId;
	}
}
