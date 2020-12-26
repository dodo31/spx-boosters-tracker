package com.drawrunner.data.retrieval;

import processing.core.PApplet;
import processing.data.JSONArray;

public class DatasetDownloader extends DataDownloader {
	public DatasetDownloader(PApplet applet, String datasetApiUrl, DownloadListener downloadListener) {
		super(applet, datasetApiUrl, downloadListener);
	}
	
	@Override
	public void run() {
		String datasetRaw = this.downloadData();
		JSONArray dataset = applet.parseJSONArray(datasetRaw);
		downloadListener.onDatasetDownloaded(dataset);
	}
}
