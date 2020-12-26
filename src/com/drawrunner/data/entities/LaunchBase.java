package com.drawrunner.data.entities;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.drawrunner.data.retrieval.DataEntryDownloader;
import com.drawrunner.data.retrieval.DatasetDownloader;
import com.drawrunner.data.retrieval.DownloadListener;

import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class LaunchBase extends EntityBase implements DownloadListener {
	private final static String API_LAUNCHES_URL = "https://api.spacexdata.com/v4/launches";
	private final static String API_NEXT_LAUNCH_URL = API_LAUNCHES_URL + "/next";
	
	private Map<String, Launch> launches;
	private Launch nextLaunch;
	
	private LaunchBase() {
		launches = new HashMap<String, Launch>();
		nextLaunch = null;
	}
	
	public static LaunchBase getInstance() {
		return LaunchBaseHolder.instance;
	}
	
	@Override
	public void startDownload(PApplet applet) {
		DatasetDownloader launchDownloader = new DatasetDownloader(applet, API_LAUNCHES_URL, this);
		DataEntryDownloader nextLaunchDownloader = new DataEntryDownloader(applet, API_NEXT_LAUNCH_URL, this);
		
		new Thread(launchDownloader).start();
		new Thread(nextLaunchDownloader).start();
	}
	
	@Override
	public void onDataEntryDownloaded(JSONObject launchData) {
		nextLaunch = this.parseLaunch(launchData);
		
		if(!launches.isEmpty()) {
			this.notifyPopulated();
		}
	}
	
	@Override
	public void onDatasetDownloaded(JSONArray launchDataset) {
		for (int i = 0; i < launchDataset.size(); i++) {
			JSONObject launchData = launchDataset.getJSONObject(i);
			boolean isUpcoming = launchData.getBoolean("upcoming");
			
			if(!isUpcoming) {
				Launch newLaunch = this.parseLaunch(launchData);
				
				if(!Objects.isNull(newLaunch)) {
					launches.put(newLaunch.getId(), newLaunch);
				}
			}
		}
		
		if(!Objects.isNull(nextLaunch)) {
			this.notifyPopulated();
		}
	}
	
	private Launch parseLaunch(JSONObject launchData) {
		String launchId = launchData.getString("id");
		
		Launch newLaunch = new Launch(launchId);
		
		if(!launchData.isNull("flight_number")) {
			newLaunch.setFlightNumber(launchData.getInt("flight_number"));
		}
		
		newLaunch.setName(launchData.getString("name"));
		newLaunch.setLaunchDate(launchData.getString("date_utc"));
		
		if(!launchData.isNull("success")) {
			newLaunch.setSuccess(launchData.getBoolean("success"));
		}
		
		newLaunch.setUpcoming(launchData.getBoolean("upcoming"));
		
		JSONArray coresData = launchData.getJSONArray("cores");
		newLaunch.setCoreContexts(this.extractCoreContexts(coresData));
		
		newLaunch.setRocketId(launchData.getString("rocket"));
		
		JSONArray payloadsData = launchData.getJSONArray("payloads");
		newLaunch.setPayloadIds(payloadsData.getStringArray());
		
		JSONArray capsulesData = launchData.getJSONArray("capsules");
		newLaunch.setCapsuleIds(capsulesData.getStringArray());
		
		JSONArray crewData = launchData.getJSONArray("crew");
		newLaunch.setCrewIds(crewData.getStringArray());
		
		newLaunch.setLaunchpadId(launchData.getString("launchpad"));
		
		return newLaunch;
	}
	
	private CoreContext[] extractCoreContexts(JSONArray coresData) {
		List<CoreContext> coreContexts = new ArrayList<CoreContext>();
		
		for (int i = 0; i < coresData.size(); i++) {
			JSONObject coreData = coresData.getJSONObject(i);
			
			if(!coreData.isNull("core")) {
				String coreId = coreData.getString("core");

				CoreContext newCoreContext = new CoreContext(coreId);
				newCoreContext.setFlightCount(coreData.getInt("flight"));
				
				newCoreContext.setLandingAttempt(coreData.getBoolean("landing_attempt"));
				
				if(!coreData.isNull("landing_success")) {
					newCoreContext.setLandingSuccess(coreData.getBoolean("landing_success"));
				}
				
				if(!coreData.isNull("landing_type")) {
					newCoreContext.setLandingType(coreData.getString("landing_type"));
				}
				
				coreContexts.add(newCoreContext);
			}
		}
		
		CoreContext[] coreContextsArray = new CoreContext[coreContexts.size()];
		return coreContexts.toArray(coreContextsArray);
	}
	
	public Iterator<Launch> getLaunches() {
		return launches.values().iterator();
	}
	
	public List<Launch> getLaunches(String[] launchIds) {
		List<Launch> matchingLaunches = new LinkedList<Launch>();
		
		for (String launchId : launchIds) {
			Launch matchingLaunch = this.getLaunch(launchId);
			matchingLaunches.add(matchingLaunch);
		}
		
		return matchingLaunches;
	}
	
	public Launch getLaunch(String launchId) {
		return launches.get(launchId);
	}
	
	public int launchCount() {
		return launches.size();
	}
	
	public Launch getNextLaunch() {
		return this.nextLaunch;
	}

	private static class LaunchBaseHolder {
		private static LaunchBase instance = new LaunchBase();
	}
}
