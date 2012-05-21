package cosm;

import processing.core.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;

import org.json.*;
import org.apache.http.Header;
import org.apache.http.HttpConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.*;
import org.apache.http.client.methods.*;
import org.apache.http.entity.BasicHttpEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.*;

public class DataOut {
	PApplet myParent;
	String apiKey;
	String feedId;
	boolean verbose = false;
	String data;
	JSONObject data2 = new JSONObject("");
	
	public DataOut(PApplet theParent, String key, String id) {
		apiKey = key;
		feedId = id;
		myParent = theParent;
	}
	public void update(int stream, float val) {
		buildHeader(stream, val);
		String host = "http://api.cosm.com";
		String datastream = Integer.toString(stream);
		String operation = "/v2/feeds/" + feedId + "/datastreams/" + datastream;
		String url = host + operation;
		try {
			String response = sendData(url, data);

			if (verbose) {
				System.out.println("--------------response------------------");
				System.out.println(response);
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
	private String sendData(String url, String data) throws IOException {
		URL _url = new URL(url);
		HttpURLConnection httpCon = (HttpURLConnection) _url.openConnection();
		httpCon.setDoOutput(true);
		httpCon.setRequestMethod("PUT");
		httpCon.addRequestProperty("X-ApiKey", apiKey);
		httpCon.addRequestProperty("Host", "api.cosm.com");
		OutputStreamWriter out = new OutputStreamWriter(
		    httpCon.getOutputStream());
			
		out.write("1,35");
		out.close();
		return "hello";
/*		HttpClient httpclient = new DefaultHttpClient();
		BasicHttpEntity myEntity = new BasicHttpEntity();
		InputStream instream = new ByteArrayInputStream(data.getBytes("1.0.0"));
		myEntity.setContentType(data);
		myEntity.setContentLength(data.length());
		myEntity.setContent(instream);
		try {
			HttpPut httpput = new HttpPut(url);
			//httpput.addHeader("data-binary", data );
			httpput.setHeader("Version", "1.0.0");
			httpput.setHeader("Host", "api.cosm.com");
			httpput.setHeader("X-ApiKey", apiKey);
			httpput.setEntity(myEntity);
			
			
			if (verbose) {
				System.out.println("--- open connection ---");
				System.out.println("executing request " + httpput.getURI());
			}
			// create a response handler
		ResponseHandler<String> responseHandler = new BasicResponseHandler();
			//responseHandler = data.format(responseHandler, null) ;
		String responseBody = httpclient.execute(httpput, responseHandler);
			//String responseBody = httpclient.execute(httpput, data);
			//httpclient.
		return responseBody;
	} finally {
			// shut down the connection manager to ensure
			// immediate deallocation of all system resources
		if (verbose)
				System.out.println("--- close connection ---");
			//httpclient.getConnectionManager().shutdown();
	}*/
	}
	
	private void buildHeader(int stream, float val){
		JSONObject thisData = new JSONObject("");
		thisData.put("current_value",Float.toString(val));
		thisData.put("id", Integer.toString(stream));
		data2 = thisData;
		data = thisData.toString();
	}
	
	// to see all server requests/responses
	public void setVerbose(boolean v) {
		verbose = v;
	}
}
