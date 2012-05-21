package cosm;


import java.io.*;
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

	Vector<String> updatedAt = new Vector<String>();
	Vector<String> currVal = new Vector<String>();
	Vector<String> idn = new Vector<String>();
	Vector<String> maxVal = new Vector<String>();
	Vector<String> minVal = new Vector<String>();

	public CosmParser(DataIn theParent, JSONObject root) {
		myParent = theParent;
		parseLocation(root);
		parseStreams(root);
	}

	private void parseLocation(JSONObject data) {
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
	}
	
	// ------- datastream parsing -------//
	// --- identify, parse all datastreams
	private void parseStreams(JSONObject data) {

		// TODO: make this work with ids that are Strings
		JSONArray streams = data.getJSONArray("datastreams");
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
					/*
					 * TODO: parse datastream tags (multiple values under single
					 * name) else if(row.equals("tags")){
					 * System.out.println(stream.getJSONArray("tags"));
					 * 
					 * JSONArray taggs = stream.getJSONArray("tags");
					 * System.out.println(taggs.hashCode()); for(int k = 0; k <
					 * taggs.length(); k++){ tags[j][k] = taggs.getString(k); }
					 * } else System.out.println("something else!");
					 */
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	//--- get last time updated
	public String getLastUpdateTimestamp(int id) {
		String data;
		data = updatedAt.get(id);
		return data;
	}
	//TODO: find solution- can't overload a method of return type String...
	/*public static String getLastUpdateTimestamp(String id) {
		String data = null;
		if (idn.contains(id)) {
			int index = idn.indexOf(id);
			data = updatedAt.get(index);
		} else
		return data;
	}*/
	
	//--- get stream Id Name
	public String getStreamId (int id) {
		String data;
		data = idn.get(id);
		return data;
	}
	//TODO: find solution- can't overload a method of return type String...		
	/*public String getStreamId (String id) {
		String data = null;
		if (idn.contains(id)) {
			int index = idn.indexOf(id);
			data = idn.get(index);
		} else
		return data;
	}*/
	
	//--- get stream value (alternate method, not used in examples)
	public float getStreamCurrVal(int id) {
		float data = 0.0f;
		String d = currVal.get(id);
		data = Float.valueOf(d.trim()).floatValue();
		return data;
	}
	//--- get stream max val
	public float getStreamMaxVal(int id) {
		float data = 0.0f;
		String d = maxVal.get(id);
		data = Float.valueOf(d.trim()).floatValue();
		return data;
	}
	//--- get stream min val
	public float getStreamMinVal(int id) {
		float data = 0.0f;
		String d = minVal.get(id);
		data = Float.valueOf(d.trim()).floatValue();
		return data;
	}

	//--- TODO:get stream tags
}
