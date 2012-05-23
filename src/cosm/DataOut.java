package cosm;

import processing.core.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

import org.json.*;
import org.apache.http.Header;
import org.apache.http.HttpConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.*;
import org.apache.http.util.EntityUtils;

public class DataOut {
	PApplet myParent;
	String apiKey;
	String feedId;
	boolean verbose = false;
	String data;
	String host = "http://api.cosm.com/v2/feeds/";
	JSONObject dataToSend;

	// testing with this:
	JSONObject dataToSendTest = new JSONObject(
			"{'current_value':'294','max_value':'697.0','min_value':'0.0','id':'1'}");

	public DataOut(PApplet theParent, String key, String id) {
		apiKey = key;
		feedId = id;
		myParent = theParent;
		System.out.println("##library.name## ##library.prettyVersion## http://cosm.com");
		System.out.println("##author## 2012");
		System.out.println("---------------------------------");
	}

	/*
	 * Retrieve response using commons HttpClient API.
	 * 
	 * @return String
	 */
	private String sendData(String url, JSONObject dataToSend)
			throws IOException {
		String cosmResponse = "";
		String statusLine = "";
		boolean requestFailed = false;
		HttpResponse response;
		HttpEntity entity;

		StringEntity myData = new StringEntity(dataToSend.toString());
		myData.setChunked(false);
		HttpClient httpclient = new DefaultHttpClient();
		HttpPut httpput = new HttpPut(url);
		httpput.setHeader("X-ApiKey", apiKey);
		httpput.setHeader("Accepts", "application/json");
		httpput.setHeader("Content-Type", "application/json");
		httpput.setHeader("Host", "api.cosm.com");
		httpput.setHeader("Version", "HTTP/1.1");
		httpput.setEntity(myData);

		try {
			if (verbose) {
				System.out.println("-------- open cosmic connection --------");
				System.out.println("executing request: " + httpput.getURI());
				System.out.println("data to send: " + dataToSend);
				System.out.println("using ApiKey: " + apiKey);
			}
			response = httpclient.execute(httpput); // execute this mother
			entity = response.getEntity();
			statusLine = response.getStatusLine().toString() + "\n";
			String delims = "[ ]+";
			String[] tokens = statusLine.split(delims);

			if (tokens[1].equals("200")) { // success
				System.out.println("\nserver response:");
				System.out.println(statusLine);
				cosmResponse = EntityUtils.toString(entity);
			} else { // bad request
				System.out.println("\nCosm says bad request. status code: \n");
				System.out.println(statusLine);
				System.out
						.println("check the HTTP status codes at http://cosm.com/docs/v2/\n");
				cosmResponse = EntityUtils.toString(entity) + "\n\n";
				Header[] respHeader;
				respHeader = response.getAllHeaders();
				for (int i = 0; i < respHeader.length; i++) {
					cosmResponse += respHeader[i].toString() + "\n";
				}
				if (verbose)
					System.out.print(cosmResponse);
				requestFailed = true;
				return "***failed request***";
			}
		} catch (ClientProtocolException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			// shut down the connection manager to ensure
			// immediate deallocation of all system resources
			httpclient.getConnectionManager().shutdown();
			if (verbose) {
				System.out
						.println("----------- connection closed ----------\n");
			}
			if (requestFailed)
				System.exit(0);
		}
		return cosmResponse;
	}

	// --------- update methods --------
	// --- set Stream Value!
	public void setStream(int stream, float val) {
		String datastream = Integer.toString(stream);
		String operation = feedId;
		String url = host + operation;
		dataToSend = new JSONObject("{'version':'1.0.0','datastreams':[{'id':'"
				+ datastream + "','current_value':'" + val + "'}]}");
		try {
			String response = sendData(url, dataToSend);
			// there is an empty response for a 200
			// and app quits itself if we get anything other than a 200!
			/*
			 * if (verbose) {
			 * System.out.println("--------------response------------------");
			 * System.out.println(response);
			 * System.out.println("----------------------------------------"); }
			 */
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setStream(int stream, String val) {
		String datastream = Integer.toString(stream);
		String operation = feedId;
		String url = host + operation;
		dataToSend = new JSONObject("{'version':'1.0.0','datastreams':[{'id':'"
				+ datastream + "','current_value':'" + val + "'}]}");
		try {
			sendData(url, dataToSend);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setStream(String stream, String val) {
		String operation = feedId;
		String url = host + operation;
		dataToSend = new JSONObject("{'version':'1.0.0','datastreams':[{'id':'"
				+ stream + "','current_value':'" + val + "'}]}");
		try {
			sendData(url, dataToSend);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setStream(String stream, float val) {
		String value = Float.toString(val);
		String operation = feedId;
		String url = host + operation;
		dataToSend = new JSONObject("{'version':'1.0.0','datastreams':[{'id':'"
				+ stream + "','current_value':'" + value + "'}]}");
		try {
			sendData(url, dataToSend);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// --- change the min/max values
	public void setStreamMinMax(int stream, float min, float max) {
		String datastream = Integer.toString(stream);
		String newMaxVal = Float.toString(max);
		String newMinVal = Float.toString(min);
		dataToSend = new JSONObject("{'version':'1.0.0','datastreams':[{'id':'"
				+ datastream + "','max_value':'" + newMaxVal
				+ "','min_value':'" + newMinVal + "'}]}");
		String operation = feedId;
		String url = host + operation;
		try {
			sendData(url, dataToSend);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setStreamMinMax(String stream, float min, float max) {
		String newMaxVal = Float.toString(max);
		String newMinVal = Float.toString(min);
		dataToSend = new JSONObject("{'version':'1.0.0','datastreams':[{'id':'"
				+ stream + "','max_value':'" + newMaxVal + "','min_value':'"
				+ newMinVal + "'}]}");
		String operation = feedId;
		String url = host + operation;
		try {
			sendData(url, dataToSend);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// --- change the ID of the stream
	public void setStreamId(int stream, String idName) {
		String datastream = Integer.toString(stream);
		dataToSend = new JSONObject("{'id':'" + idName + "'}");
		String operation = feedId + "/datastreams/" + datastream;
		String url = host + operation;
		try {
			sendData(url, dataToSend);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setStreamId(String stream, String idName) {
		// String datastream = Integer.toString(stream);
		dataToSend = new JSONObject("{'id':'" + idName + "'}");
		String operation = feedId + "/datastreams/" + stream;
		String url = host + operation;
		try {
			sendData(url, dataToSend);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//--- set tags for stream
	public void setStreamTags(String stream, String tag1, String tag2, String tag3) {
		dataToSend = new JSONObject("{'version':'1.0.0','datastreams':[{'id':'"+stream+"','tags':['" + tag1
				+ "','" + tag2 + "','" + tag3 + "']}]}");
		String operation = feedId;
		String url = host + operation;
		try {
			sendData(url, dataToSend);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	// --- set tags for feed
	public void setFeedTags(String tag1, String tag2, String tag3) {
		dataToSend = new JSONObject("{'version':'1.0.0','tags':['" + tag1
				+ "','" + tag2 + "','" + tag3 + "']}");
		String operation = feedId;
		String url = host + operation;
		try {
			sendData(url, dataToSend);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	public void setFeedLocation(double lat, double lon){
		dataToSend = new JSONObject("{'version':'1.0.0','location':{'lat':" + lat
				+ ",'lon':" + lon + "}}");
		String operation = feedId;
		String url = host + operation;
		try {
			sendData(url, dataToSend);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setFeedLocationName(String name){
		dataToSend = new JSONObject("{'version':'1.0.0','location':{'name':'" + name+"'}}");
		String operation = feedId;
		String url = host + operation;
		try {
			sendData(url, dataToSend);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public void setFeedElevation(double lat, double lon, float elevation){
		String ele = Float.toString(elevation);
		dataToSend = new JSONObject("{'version':'1.0.0','location':{'lat':" + lat
				+ ",'lon':" + lon + ",'ele':'"+ele+"'}}");
		String operation = feedId;
		String url = host + operation;
		try {
			sendData(url, dataToSend);
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}

	// to see all server requests/responses
	public void setVerbose(boolean v) {
		verbose = v;
	}
}
