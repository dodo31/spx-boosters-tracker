package com.drawrunner.data.entities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import com.drawrunner.data.retrieval.DatasetDownloader;
import com.drawrunner.data.retrieval.DownloadListener;

import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class RocketBase extends EntityBase implements DownloadListener {
	private final static String API_ROCKETS_URL = "https://api.spacexdata.com/v4/rockets/";
	
	private Map<String, Rocket> rockets;
	
	private RocketBase() {
		rockets = new HashMap<String, Rocket>();
	}
	
	public static RocketBase getInstance() {
		return RocketBaseHolder.instance;
	}
	
	@Override
	public void startDownload(PApplet applet) {
		DatasetDownloader rocketDownloader = new DatasetDownloader(applet, API_ROCKETS_URL, this);
		new Thread(rocketDownloader).start();
	}
	
	@Override
	public void onDataEntryDownloaded(JSONObject datasetEntry) {
		
	}
	
	@Override
	public void onDatasetDownloaded(JSONArray rocketDataset) {
		for (int i = 0; i < rocketDataset.size(); i++) {
			JSONObject rocketData = rocketDataset.getJSONObject(i);
			String rocketId = rocketData.getString("id");
			
			Rocket newRocket = new Rocket(rocketId);
			newRocket.setName(rocketData.getString("name"));
			newRocket.setFirstFlightDate(rocketData.getString("first_flight"));
			
			rockets.put(rocketId, newRocket);
		}
		
		this.notifyPopulated();
	}
	
	public Iterator<Rocket> getRockets() {
		return rockets.values().iterator();
	}
	
	public Rocket getRocket(String rocketId) {
		return rockets.get(rocketId);
	}
	
	public int rocketCount() {
		return rockets.size();
	}
	
	private static class RocketBaseHolder {
		private static RocketBase instance = new RocketBase();
	}
}
