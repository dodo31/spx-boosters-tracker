package com.drawrunner.data.retrieval;

import processing.core.PApplet;
import processing.data.JSONObject;

public class DataEntryDownloader extends DataDownloader {
	public DataEntryDownloader(PApplet applet, String datasetApiUrl, DownloadListener downloadListener) {
		super(applet, datasetApiUrl, downloadListener);
	}
	
	@Override
	public void run() {
		String dataEntryRaw = this.downloadData();
		JSONObject dataEntry = applet.parseJSONObject(dataEntryRaw);
		downloadListener.onDataEntryDownloaded(dataEntry);
	}
}
