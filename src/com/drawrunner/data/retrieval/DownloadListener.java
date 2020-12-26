package com.drawrunner.data.retrieval;

import processing.data.JSONArray;
import processing.data.JSONObject;

public interface DownloadListener {
	public void onDataEntryDownloaded(JSONObject datasetEntry);
	public void onDatasetDownloaded(JSONArray dataset);
}
