package com.drawrunner.data.retrieval;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import processing.core.PApplet;

public abstract class DataDownloader implements Runnable {
	protected PApplet applet;
	
	protected String datasetApiUrl;
	protected DownloadListener downloadListener;
	
	public DataDownloader(PApplet applet, String datasetApiUrl, DownloadListener downloadListener) {
		this.applet = applet;
		
		this.datasetApiUrl = datasetApiUrl;
		this.downloadListener = downloadListener;
	}
	
	protected String downloadData() {
		StringBuffer responseContent = new StringBuffer();
		
		try {
			URL apiUrl = new URL(datasetApiUrl);
			HttpURLConnection apiConnection = (HttpURLConnection) apiUrl.openConnection();
			apiConnection.setRequestMethod("GET");
			
			BufferedReader reponseReader = new BufferedReader(new InputStreamReader(apiConnection.getInputStream()));
			
			String currentResponseLine = reponseReader.readLine();
			while (currentResponseLine != null) {
			    responseContent.append(currentResponseLine);
			    currentResponseLine = reponseReader.readLine();
			}
		} catch (MalformedURLException e) {
			System.err.println("Incorrect URL format: " + datasetApiUrl);
			e.printStackTrace();
		} catch (IOException e) {
			System.err.println("Error during data requesting: " + datasetApiUrl);
			e.printStackTrace();
		}
		
		return responseContent.toString();
	}
}
