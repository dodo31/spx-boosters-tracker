package com.drawrunner.data.entities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.drawrunner.constants.Orbits;
import com.drawrunner.data.retrieval.DatasetDownloader;
import com.drawrunner.data.retrieval.DownloadListener;

import processing.core.PApplet;
import processing.data.JSONArray;
import processing.data.JSONObject;

public class PayloadBase extends EntityBase implements DownloadListener {
	private final static String API_PAYLOADS_URL = "https://api.spacexdata.com/v4/payloads/";
	
	private Map<String, Payload> payloads;
	
	private PayloadBase() {
		payloads = new HashMap<String, Payload>();
	}
	
	public static PayloadBase getInstance() {
		return PayloadBaseHolder.instance;
	}
	
	@Override
	public void startDownload(PApplet applet) {
		DatasetDownloader payloadDownloader = new DatasetDownloader(applet, API_PAYLOADS_URL, this);
		new Thread(payloadDownloader).start();
	}
	
	@Override
	public void onDataEntryDownloaded(JSONObject datasetEntry) {
		
	}
	
	@Override
	public void onDatasetDownloaded(JSONArray payloadDataset) {
		for (int i = 0; i < payloadDataset.size(); i++) {
			JSONObject payloadData = payloadDataset.getJSONObject(i);
			String payloadId = payloadData.getString("id");
			String payloadName = payloadData.getString("name");
			
			Payload newPayload = new Payload(payloadId);
			newPayload.setName(payloadData.getString("name"));
			newPayload.setType(payloadData.getString("type"));
			
			if(!payloadData.isNull("orbit")) {
				newPayload.setOrbit(payloadData.getString("orbit"));
			} else {
				String standardizedPayloadName = payloadName.toLowerCase(); 
				
				if(standardizedPayloadName.contains("nrol-108")) {
					newPayload.setOrbit(Orbits.LEO);
				}
			}
			
			if(!payloadData.isNull("mass_kg")) {
				newPayload.setMass(payloadData.getFloat("mass_kg"));
			}
			
			newPayload.setReused(payloadData.getBoolean("reused"));
			
			payloads.put(payloadId, newPayload);
		}
		
		this.notifyPopulated();
	}
	
	public Orbits highestOrbit(String[] payloadIds) {
		List<Orbits> orbits = new ArrayList<Orbits>();
		
		for (String payloadId : payloadIds) {
			Payload payload = this.getPayload(payloadId);
			
			Orbits orbit = payload.getOrbit();
			orbits.add(orbit);
		}
		
		Collections.sort(orbits);
		
		return orbits.get(orbits.size() - 1);
	}
	
	public float totalSetMass(String[] payloadIds) {
		List<Payload> payloads = this.getPayloads(payloadIds);
		
		float totalMass = 0;
		
		for(Payload payload : payloads) {
			float payloadMass = payload.getMass();
			
			if(payloadMass >= 0) {
				totalMass += payloadMass;
			}
		}
		
		return totalMass;
	}
	
	public boolean hasSetRealMass(String[] payloadIds) {
		List<Payload> payloads = this.getPayloads(payloadIds);
		
		boolean isRealMass = true;
		
		for(Payload payload : payloads) {
			if(payload.getMass() < 0) {
				isRealMass = false;
			}
		}
		
		return isRealMass;
	}
	
	public Iterator<Payload> getPayloads() {
		return payloads.values().iterator();
	}
	
	public List<Payload> getPayloads(String[] payloadIds) {
		List<Payload> matchingPayloads = new LinkedList<Payload>();
		
		for (String payloadId : payloadIds) {
			Payload matchingPayload = this.getPayload(payloadId);
			matchingPayloads.add(matchingPayload);
		}
		
		return matchingPayloads;
	}
	
	public Payload getPayload(String payloadId) {
		return payloads.get(payloadId);
	}
	
	public int payloadCount() {
		return payloads.size();
	}
	
	private static class PayloadBaseHolder {
		private static PayloadBase instance = new PayloadBase();
	}
}
