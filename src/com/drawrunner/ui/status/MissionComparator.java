package com.drawrunner.ui.status;

import java.util.Comparator;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.drawrunner.constants.CoreStatus;

public class MissionComparator implements Comparator<BoosterHistory> {
	private boolean sortByDate;
	private boolean sortByState;
	
	public MissionComparator(boolean sortByDate, boolean sortByState) {
		this.sortByDate = sortByDate;
		this.sortByState = sortByState;
	}

	public int compare(BoosterHistory history1, BoosterHistory history2) {
		if(history1.isEmpty() && history2.isEmpty()) {
			return 0;
		} else if(history1.isEmpty() && !history2.isEmpty()) {
			return 1;
		} else if(!history1.isEmpty() && history2.isEmpty()) {
			return -1;
		} else {
			if(history1.hasAccomplishedMissions() == history2.hasAccomplishedMissions()) {
				if(sortByState) {
					boolean isAlive1 = history1.isCoreAlive();
					boolean isAlive2 = history2.isCoreAlive();
					
					if(isAlive1 && !isAlive2) {
						return 1;
					} else if(!isAlive1 && isAlive2) {
						return -1;
					} else {
						if(sortByDate) {
							return this.compareDates(history1, history2);
						} else {
							return 0;
						}
					}
				}
				
				if(sortByDate) {
					return this.compareDates(history1, history2);
				} else {
					return 0;
				}
			} else if(!history1.hasAccomplishedMissions() && history2.hasAccomplishedMissions()) {
				return 1;
			} else {
				return -1;
			}
		}
	};
	
	private int compareDates(BoosterHistory history1, BoosterHistory history2) {
		Date currentDate1 = history1.getMission(0).getDate();
		Date currentDate2 = history2.getMission(0).getDate();

		if(currentDate1 == null && currentDate2 == null) {
			Pattern numberExtractionPattern = Pattern.compile("[0-9]");
			Matcher serialMatcher1 = numberExtractionPattern.matcher(history1.getCoreSerial());
			Matcher serialMatcher2 = numberExtractionPattern.matcher(history2.getCoreSerial());

			int serialNumber1 = Integer.parseInt(serialMatcher1.group());
			int serialNumber2 = Integer.parseInt(serialMatcher2.group());

			return serialNumber1 - serialNumber2;
		} else if(currentDate1 == null && currentDate2 != null) {
			return 1;
		} else if (currentDate1 != null && currentDate2 == null) {
			return -1;
		} else {
			return currentDate1.compareTo(currentDate2);
		}
	}
}
