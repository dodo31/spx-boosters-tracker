package com.drawrunner.data.entities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import com.drawrunner.constants.Pads;
import com.drawrunner.data.retrieval.DatasetDownloader;
import com.drawrunner.data.retrieval.DownloadListener;

import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class LaunchpadBase extends EntityBase implements DownloadListener {
	private final static String API_LAUNCHES_URL = "https://api.spacexdata.com/v4/launchpads/";
	
	private Map<String, Launchpad> launchpads;
	
	private LaunchpadBase() {
		launchpads = new HashMap<String, Launchpad>();
	}
	
	public static LaunchpadBase getInstance() {
		return LaunchpadBaseHolder.instance;
	}
	
	@Override
	public void startDownload(PApplet applet) {
		DatasetDownloader launchDownloader = new DatasetDownloader(applet, API_LAUNCHES_URL, this);
		new Thread(launchDownloader).start();
	}
	
	@Override
	public void onDataEntryDownloaded(JSONObject launchpadData) {
		
	}
	
	@Override
	public void onDatasetDownloaded(JSONArray launchpadDataset) {
		for (int i = 0; i < launchpadDataset.size(); i++) {
			JSONObject launchpdData = launchpadDataset.getJSONObject(i);
			Launchpad newLaunchpad = this.parseLaunchpad(launchpdData);
			
			if(!Objects.isNull(newLaunchpad)) {
				launchpads.put(newLaunchpad.getId(), newLaunchpad);
			}
		}
		
		this.notifyPopulated();
	}
	
	private Launchpad parseLaunchpad(JSONObject launchpadData) {
		String launchpadId = launchpadData.getString("id");
		String lauchpadName = launchpadData.getString("name");
		
		Launchpad newLaunchpad = new Launchpad(launchpadId);
		newLaunchpad.setName(lauchpadName);
		newLaunchpad.setFullName(launchpadData.getString("full_name"));
		
		String normalizedName = lauchpadName.replace(" ", "").toLowerCase();
		
		if(normalizedName.contains("lc39a")) {
			newLaunchpad.setShortName(Pads.LC39A.name);
			newLaunchpad.setSymbolPath(Pads.LC39A.symbolPath);
		} else if(normalizedName.contains("slc40")) {
			newLaunchpad.setShortName(Pads.SLC40.name);
			newLaunchpad.setSymbolPath(Pads.SLC40.symbolPath);
		} else if(normalizedName.contains("slc4e")) {
			newLaunchpad.setShortName(Pads.SLC4E.name);
			newLaunchpad.setSymbolPath(Pads.SLC4E.symbolPath);
		}
			
		return newLaunchpad;
	}
	
	public Iterator<Launchpad> getLaunchpads() {
		return launchpads.values().iterator();
	}
	
	public List<Launchpad> getLaunchpads(String[] launchpadIds) {
		List<Launchpad> Launchpads = new LinkedList<Launchpad>();
		
		for (String launchId : launchpadIds) {
			Launchpad matchingLaunchpad = this.getLaunchpad(launchId);
			Launchpads.add(matchingLaunchpad);
		}
		
		return Launchpads;
	}
	
	public Launchpad getLaunchpad(String launchpadId) {
		return launchpads.get(launchpadId);
	}
	
	public int launchpadCount() {
		return launchpads.size();
	}
	
	private static class LaunchpadBaseHolder {
		private static LaunchpadBase instance = new LaunchpadBase();
	}
}
