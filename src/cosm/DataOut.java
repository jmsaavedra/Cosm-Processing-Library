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
	
	JSONObject dataToSend;
	
	// testing with this:
	JSONObject dataToSendTest = new JSONObject(
			"{'current_value':'294','max_value':'697.0','min_value':'0.0','id':'1'}");

	public DataOut(PApplet theParent, String key, String id) {
		apiKey = key;
		feedId = id;
		myParent = theParent;
	}

	/*
	 * Retrieve response using commons HttpClient API.
	 * 
	 * @return String
	 */
	private String sendData(String url, JSONObject dataToSend) throws IOException {
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
				System.out.println("-------- open cosmic connection --------");System.out.println("executing request: " + httpput.getURI());
				System.out.println("using ApiKey: "+apiKey);
			}
			response = httpclient.execute(httpput); //execute this mother
			entity = response.getEntity();
			statusLine = response.getStatusLine().toString() + "\n";
			String delims = "[ ]+"; 
			String[] tokens = statusLine.split(delims);
			
			if (tokens[1].equals("200")) { //success
				System.out.println("\nserver response:");
				System.out.println(statusLine);
				cosmResponse = EntityUtils.toString(entity);
			}
			else { //bad request
				System.out.println("\nCosm says bad request. status code: \n");
				System.out.println(statusLine);
				System.out.println("check the HTTP status codes at http://cosm.com/docs/v2/\n");
				cosmResponse = EntityUtils.toString(entity)+"\n\n";
				Header[] respHeader;
				respHeader = response.getAllHeaders();
				for (int i = 0; i < respHeader.length; i++) {
					cosmResponse += respHeader[i].toString()+"\n";
				}
				if(verbose) System.out.print(cosmResponse);
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
			if (verbose){
				System.out.println("----------- connection closed ----------\n");
			}
			if(requestFailed) System.exit(0);
		}
		return cosmResponse;
	}
	
//--------- update methods --------
	public void updateStream(int stream, float val) {
		buildHeader(stream, val);
		String host = "http://api.cosm.com";
		String datastream = Integer.toString(stream);
		String operation = "/v2/feeds/" + feedId + "/datastreams/" + datastream;
		String url = host + operation;
		dataToSend = new JSONObject("{'current_value':'"+val+"'}");
		try {
			String response = sendData(url, dataToSend);
			//there is an empty response for a 200
			//and app quits itself if we get anything other than a 200!
			/*if (verbose) {
				System.out.println("--------------response------------------");
				System.out.println(response);
				System.out.println("----------------------------------------");
			}*/
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void buildHeader(int stream, float val) {

	}

	// to see all server requests/responses
	public void setVerbose(boolean v) {
		verbose = v;
	}
}
