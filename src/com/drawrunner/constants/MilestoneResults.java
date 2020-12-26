package com.drawrunner.constants;

public enum MilestoneResults {
	FAILURE,
	PARTIAL_FAILURE,
	SUCCESS;
	
	public static MilestoneResults stringToMilestoneResult(String resultData) {
		if(resultData != null) {
			return MilestoneResults.valueOf(resultData);
		} else {
			return null;
		}
	}
}
