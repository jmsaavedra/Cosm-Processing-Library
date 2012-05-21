package cosm;

import processing.core.*;
import java.io.*;
import org.json.*;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.impl.client.*;

/*TODO: 
 * - build simple timer for auto-requests
 * - make all methods for datastream info work with String paramater
 */
public class DataIn {

	PApplet myParent;
	String apiKey;
	String feedId;
	boolean verbose = false;

	JSONObject root;
	CosmParser parser;

	public DataIn(PApplet theParent, String key, String id) {
		apiKey = key;
		feedId = id;
		myParent = theParent;
	}

	public void connect() {
		String host = "http://api.cosm.com";
		String operation = "/v2/feeds/" + feedId + ".json";
		String url = host + operation;
		try {
			String response = getResponse(url);

			// create root JSONObject
			root = new JSONObject(response);

			// handles all parsing
			parser = new CosmParser(this, root);

			if (verbose) {
				System.out.println("--------------response------------------");
				System.out.println(root);
				System.out.println("----------------------------------------");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	/* 
	 * Retrieve response using commons HttpClient API.
	 * return the version of the library.
	 * @return String
	 */
	private String getResponse(String url) throws IOException {

		HttpClient httpclient = new DefaultHttpClient();

		try {
			HttpGet httpget = new HttpGet(url);
			httpget.setHeader("Version", "HTTP/1.1");
			httpget.setHeader("Host", "api.cosm.com");
			httpget.setHeader("X-ApiKey", apiKey);
			if (verbose) {
				System.out.println("--- open connection ---");
				System.out.println("executing request " + httpget.getURI());
			}
			// create a response handler
			ResponseHandler<String> responseHandler = new BasicResponseHandler();
			String responseBody = httpclient.execute(httpget, responseHandler);
			return responseBody;
		} finally {
			// shut down the connection manager to ensure
			// immediate deallocation of all system resources
			if (verbose)
				System.out.println("--- close connection ---");
			httpclient.getConnectionManager().shutdown();
		}
	}
	
//------ feed info return methods ------ //
	// --- get feed status
	public String getStatus() {
		String status = root.getString("status");
		return status;
	}
	// --- returns number of datastreams
	public int getDatastreamCount() {
		return parser.datastreamCount;
	}
	// --- get feed status
	public String getTitle() {
		String meta = root.getString("title");
		return meta;
	}
	// --- get feed desription
	public String getDescription() {
		if(root.getString("description").isEmpty()) return null;
		else return root.getString("description");
	}
	// --- get feed URL
	public String getFeed() {
		String meta = root.getString("feed");
		return meta;
	}
	// --- get feed site
	public String getWebsite() {
		if(root.getString("website").isEmpty()) return null;
		else return root.getString("website");
	}
	// --- get feed last updated
	public String getUpdated() {
		String meta = root.getString("updated");
		return meta;
	}
	// --- get feed created
	public String getCreated() {
		String meta = root.getString("created");
		return meta;
	}
	// --- get feed version
	public String getVersion() {
		String meta = root.getString("version");
		return meta;
	}
	// --- get feed user
	public String getUser() {
		String meta = root.getString("creator");
		return meta;
	}

//------- location info return methods ------//
	// --- get lat
	public double getLat() {
		return parser.latitude;
	}
	// --- get lon
	public double getLon() {
		return parser.longitude;
	}
	// --- get dispostion
	public String getDisposition() {
		return parser.disposition;
	}
	// --- getElevation
	public String getElevation() {
		return parser.elevation;
	}
	// --- getLocationName
	public String getLocationName() {
		return parser.locationName;
	}
	// --- getExposure
	public String getExposure() {
		return parser.exposure;
	}
	// --- getDomain
	public String getDomain() {
		return parser.domain;
	}
	

//------- datastream info return methods ---------//
	//--- getCurrVal by stream name (String)
	public float getValue(String dStream) {
		String currVal = null;
		float value = 0.0f;
		JSONArray streams = root.getJSONArray("datastreams");
		for (int i = 0; i < streams.length(); i++) {
			JSONObject row;
			try {
				row = streams.getJSONObject(i);
				if (row.getString("id").equals(dStream)) {
					currVal = row.getString("current_value");
					value = Float.valueOf(currVal.trim()).floatValue();
				} //else
					//value = 0.0f;
			} catch (JSONException e){ e.printStackTrace();}
		}
		if (currVal == null)
			System.out.println("DATASTREAM DOES NOT EXIST");
		return value;
	}

	// --- getCurrVal by stream number (int)
	@SuppressWarnings("unused")
	public float getValue(int streamNum) {
		String currVal = "null";
		float value = 0.0f;
		JSONArray streams = root.getJSONArray("datastreams");
		// streams.getJSONObject(streamNum);
		JSONObject row;
		try {
			row = streams.getJSONObject(streamNum);
			currVal = row.getString("current_value");
			value = Float.valueOf(currVal.trim()).floatValue();
		} catch (JSONException e) {	e.printStackTrace();}
		if (currVal == null)
			System.out.println("DATASTREAM DOES NOT EXIST");
		return value;
	}
	
	//--- get last time updated
	public String getLastUpdateTimestamp(int id) {
		return parser.getLastUpdateTimestamp(id);
	}
	//--- get stream Id Name
	public String getStreamId (int id) {
		return parser.getStreamId(id);
	}
	//--- get stream value (alternate method)
	public float getStreamCurrVal(int id) {
		return parser.getStreamCurrVal(id);
	}
	//--- get stream max val
	public float getStreamMaxVal(int id) {
		return parser.getStreamMaxVal(id);
	}
	//--- get stream min val
	public float getStreamMinVal(int id) {
		return parser.getStreamMinVal(id);
	}

	//--- TODO:get stream tags

	// to see all server requests/responses
	public void setVerbose(boolean v) {
		verbose = v;
	}
}


