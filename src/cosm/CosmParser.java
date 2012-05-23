package cosm;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Vector;
import processing.core.*;
import org.json.*;

@SuppressWarnings("unused")
public class CosmParser {

	String disposition;
	String elevation;
	double latitude;
	double longitude;
	String exposure;
	String domain;
	String locationName;
	int datastreamCount;

	DataIn myParent;

	JSONObject data;
	JSONArray streams;

	Vector<String> updatedAt = new Vector<String>();
	Vector<String> currVal = new Vector<String>();
	Vector<String> idn = new Vector<String>();
	Vector<String> maxVal = new Vector<String>();
	Vector<String> minVal = new Vector<String>();
	Vector<String> feedTags = new Vector<String>();
	String[][] streamTags = new String[50][50]; //this is horrible
	String[] stags = new String[50];	

	public CosmParser(DataIn theParent, JSONObject root) {
		myParent = theParent;
		data = root;
		parseLocation(root);
		parseStreams(root);
	}

	// ----------- feed parsing ------------//
	// --- location metadata
	private void parseLocation(JSONObject data) {
		if (myParent.verbose)
			System.out.println("CosmParser: parseLocation()");
		JSONObject location = data.getJSONObject("location");
		@SuppressWarnings("static-access")
		String[] rows = location.getNames(location);
		for (int i = 0; i < rows.length; i++) {
			String row = rows[i];
			if (row.equals("disposition"))
				disposition = location.getString("disposition");
			else if (row.equals("ele"))
				elevation = location.getString("ele");
			else if (row.equals("lat"))
				latitude = location.getDouble("lat");
			else if (row.equals("lon"))
				longitude = location.getDouble("lon");
			else if (row.equals("exposure"))
				exposure = location.getString("exposure");
			else if (row.equals("domain"))
				domain = location.getString("domain");
			else if (row.equals("name"))
				locationName = location.getString("name");
		}
		parseFeedTags();
	}

	// parsing tags are the ugliest part of this code, by far.
	private void parseFeedTags() {
		if (data.has("tags")) {
			JSONArray ftags = data.getJSONArray("tags");
			String dtags = ftags.toString();
			stags = dtags.split("\"");
			int index = 0;
			for (int l = 1; l < stags.length; l += 2) {
				// System.out.println(stags[l]);
				feedTags.add(index, stags[l]);
				index++;
			}
		}
	}

	// --- two ways to getFeedTags: as String[] or
	public String[] getFeedTags() {
		if (!feedTags.isEmpty()) {
			String[] fTags = new String[feedTags.size()];
			for (int l = 0; l < feedTags.size(); l++) {
				fTags[l] = feedTags.get(l);
			}
			return fTags;
		} else
			return null;
	}

	// --- getFeedTags as List<String>
	public List<String> getFeedTagsAsList() {
		if (!feedTags.isEmpty()) {
			String[] fTags = new String[feedTags.size()];
			for (int l = 0; l < feedTags.size(); l++) {
				fTags[l] = feedTags.get(l);
			}
			List<String> lTags = Arrays.asList(fTags);
			return lTags;
		} else
			return null;
	}

	// --- get number of datastreams
	public int getDataStreamCount() {
		if (data.has("datastreams")) {
			JSONArray streams = data.getJSONArray("datastreams");
			datastreamCount = streams.length();
			return datastreamCount;
		} else
			return 0;
	}

	// ------------ datastream parsing -----------//

	// --- identify, parse all datastreams
	private void parseStreams(JSONObject data) {
		if (myParent.verbose)
			System.out.println("CosmParser: parseStreams()\n\n\n");

		// TODO: make this work with id parameter that is a String
		if (data.has("datastreams")) {
			streams = data.getJSONArray("datastreams");
			datastreamCount = streams.length();
			for (int i = 0; i < datastreamCount; i++) {
				JSONObject stream;
				try {
					stream = streams.getJSONObject(i);
					// System.out.println("JSONObject stream #: "+i);
					// System.out.println(stream);
					@SuppressWarnings("static-access")
					String[] srows = stream.getNames(stream);

					for (int j = 0; j < srows.length; j++) {
						String row = srows[j];
						if (row.equals("at"))
							updatedAt.add(stream.getString("at"));
						else if (row.equals("current_value"))
							currVal.add(stream.getString("current_value"));
						else if (row.equals("id"))
							idn.add(stream.getString("id"));
						else if (row.equals("max_value"))
							maxVal.add(stream.getString("max_value"));
						else if (row.equals("min_value"))
							minVal.add(stream.getString("min_value"));
						else if (row.equals("tags")) { // parsing tags is the
														// ugliest part of this
														// code.
							// System.out.println("hit parse tags");
							JSONArray ttags = stream.getJSONArray("tags");
							// System.out.println("made JSONarray");
							String dtags = ttags.toString();
							stags = dtags.split("\"");
							// System.out.println(stags.length);
							int count = 0;
							for (int l = 1; l < stags.length; l += 2) {
								streamTags[i][count] = stags[l];
								count++;
							}
						}

					}
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}
	}

	// --- get last time updated
	public String getLastUpdateTimestamp(int id) {
		if (data.has("datastreams")) {
			if (id < data.getJSONArray("datastreams").length()) {
				String data;
				data = updatedAt.get(id);
				return data;
			} else
				return null;
		} else
			return null;
	}

	// TODO: find solution- can't overload a method of return type String...
	/*
	 * would like to be able call: public static String
	 * getLastUpdateTimestamp(String id) { String data = null; if
	 * (idn.contains(id)) { int index = idn.indexOf(id); data =
	 * updatedAt.get(index); } else return data; }
	 */

	// --- get stream Id Name
	public String getStreamId(int id) {
		if (data.has("datastreams")) {
			// System.out.println(data.getJSONArray("datastreams").length());
			if (id < data.getJSONArray("datastreams").length()) {
				String data;
				data = idn.get(id);
				return data;
			} else
				return "datastream does not exist";
		} else
			return "no datastreams found in this feed";
	}

	// TODO: find solution- can't overload a method with return type String...
	/*
	 * public String getStreamId (String id) { String data = null; if
	 * (idn.contains(id)) { int index = idn.indexOf(id); data = idn.get(index);
	 * } else return data; }
	 */

	// --- get stream value (alternate method, not used in examples)
	public float getStreamCurrVal(int id) {
		if (data.has("datastreams")) {
			float data = 0.0f;
			String d = currVal.get(id);
			data = Float.valueOf(d.trim()).floatValue();
			return data;
		} else
			return 0.0f;
	}

	// --- get stream max val
	public float getStreamMaxVal(int id) {
		if (data.has("datastreams")) {
			if (id < data.getJSONArray("datastreams").length()) {
				float data = 0.0f;
				String d = maxVal.get(id);
				data = Float.valueOf(d.trim()).floatValue();
				return data;
			} else
				return 0.0f;
		} else
			return 0.0f;
	}

	// --- get stream min val
	public float getStreamMinVal(int id) {
		if (data.has("datastreams")) {
			if (id < data.getJSONArray("datastreams").length()) {
				float data = 0.0f;
				String d = minVal.get(id);
				data = Float.valueOf(d.trim()).floatValue();
				return data;
			} else
				return 0.0f;
		} else
			return 0.0f;
	}

	// --- return stream tags as List<String>
	public List<String> getStreamTagsAsList(int stream) {
		// public String[] getStreamTags(int stream) { //could not get this to
		// return String[]. See below
		int numTags = 0;
		for (int i = 0; i < 50; i++) {
			if (streamTags[stream][i] != null && stream < streamTags.length)
				numTags++;
		}
		if (numTags > 0) {
			String[] m;
			String[] taggs = new String[numTags];
			for (int i = 0; i < numTags; i++) {
				taggs[i] = streamTags[stream][i]; // this works great.
			}
			// String[] sthing = (String[]) tagList.toArray();
			/*
			 * System.out.println(taggs); //no idea why taggs is broken here
			 * System.out.println(taggs[0]);//it is populated.
			 * System.out.println(taggs[1]);//it is populated.
			 * System.out.println(taggs[2]);//it is populated.
			 * System.out.println(taggs[3]);//it is populated.
			 */
			// return taggs;
			List<String> tagList = Arrays.asList(taggs); // last resort
			return tagList;

		} else
			return null;
	}

	/* could not get this to return String[] properly. See below (super ugly) */
	public String[] getStreamTags(int stream) {
		int numTags = 0;
		for (int i = 0; i < 50; i++) {
			if (streamTags[stream][i] != null)
				numTags++;
		}
		if (numTags > 0) {
			String[] taggs = new String[numTags];
			for (int i = 0; i < numTags; i++) {
				taggs[i] = streamTags[stream][i]; // this works great.
			}
			// String[] sthing = (String[]) tagList.toArray();
			/*
			 * System.out.println(taggs); //no idea why taggs is broken here
			 * System.out.println(taggs[0]);//it is populated.
			 * System.out.println(taggs[1]);//it is populated.
			 * System.out.println(taggs[2]);//it is populated.
			 * System.out.println(taggs[3]);//it is populated.
			 */
			return taggs; // returns noise
		} else
			return null;
	}
}
