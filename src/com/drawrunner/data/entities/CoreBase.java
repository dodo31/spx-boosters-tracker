package com.drawrunner.data.entities;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.drawrunner.data.retrieval.DatasetDownloader;
import com.drawrunner.data.retrieval.DownloadListener;

import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class CoreBase extends EntityBase implements DownloadListener {
	private final static String API_CORES_URL = "https://api.spacexdata.com/v4/cores/";
	
	private Map<String, Core> cores;
	private int minBlock;
	
	private CoreBase() {
		cores = new HashMap<String, Core>();
		minBlock = 0;
	}
	
	public static CoreBase getInstance() {
		return CoreBaseHolder.instance;
	}
	
	@Override
	public void startDownload(PApplet applet) {
		DatasetDownloader coreDownloader = new DatasetDownloader(applet, API_CORES_URL, this);
		new Thread(coreDownloader).start();
	}
	
	@Override
	public void onDataEntryDownloaded(JSONObject datasetEntry) {
		
	}
	
	@Override
	public void onDatasetDownloaded(JSONArray coreDataset) {
		for (int i = 0; i < coreDataset.size(); i++) {
			JSONObject coreData = coreDataset.getJSONObject(i);
			
			if(!coreData.isNull("block")) {
				String coreId = coreData.getString("id");
				int coreBlock = coreData.getInt("block");
				
				if(coreBlock >= minBlock) {
					Core newCore = new Core(coreId);
					
					if(!coreData.isNull("serial")) {
						newCore.setSerialNumber(coreData.getString("serial"));
					} else {
						newCore.setSerialNumber("B10??");
					}
					
					newCore.setStatus(coreData.getString("status"));
				
					newCore.setBlockNumber(coreBlock);
				
					newCore.setReuseCount(coreData.getInt("reuse_count"));
					
					newCore.setRtlsAttemptCount(coreData.getInt("rtls_attempts"));
					newCore.setRtlsLandingCount(coreData.getInt("rtls_landings"));
					
					newCore.setAsdsAttemptCount(coreData.getInt("asds_attempts"));
					newCore.setAsdsLandingCount(coreData.getInt("asds_landings"));
					
					JSONArray launchesData = coreData.getJSONArray("launches"); 
					newCore.setLaunchIds(launchesData.getStringArray());
					
					cores.put(coreId, newCore);
				}
			}
		}
		
		this.notifyPopulated();
	}
	
	public int maxLaunchCount() {
		int maxLaunchCount = 0;
		
		for (Core core : cores.values()) {
			int launchCount = core.launchCount();
			
			if(launchCount > maxLaunchCount) {
				maxLaunchCount = launchCount;
			}
		}
		
		return maxLaunchCount;
	}
	
	public Iterator<Core> getCores() {
		return cores.values().iterator();
	}
	
	public List<Core> getCores(String[] coreIds) {
		List<Core> matchingCores = new LinkedList<Core>();
		
		for (String coreId : coreIds) {
			Core matchingCore = this.getCore(coreId);
			matchingCores.add(matchingCore);
		}
		
		return matchingCores;
	}
	
	public Core getCore(String coreId) {
		return cores.get(coreId);
	}
	
	public int coreCount() {
		return cores.size();
	}
	
	public void setMinBlock(int minBlock) {
		this.minBlock = minBlock;
	}

	private static class CoreBaseHolder {
		private static CoreBase instance = new CoreBase();
	}
}
