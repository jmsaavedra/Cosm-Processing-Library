/* 
Cosm example to pull all metadata from a feed and then from a datastream.
All methods return a String, unless otherwise noted.

http://cosm.com
jos.ph 2012 
*/
import cosm.*;

DataIn feed;

//String apiKey = "YOUR_COSM_API_KEY";
String apiKey = "HF9qW31hUvgQ9b9RnUoM5za3BrCSAKxDd1l3MVpLS094ST0g";
String feedId = "58023"; //feed URL: https://cosm.com/feeds/58023

void setup() {

  feed = new DataIn(this, apiKey, feedId); //instantiate feed
  feed.setVerbose(true); //optional debug info toggle
  requestData();
}

void draw() {
  //awesome stuff goes here
}

void requestData() {

  feed.connect(); //send a request for data

  println("\n--- feed info ---");
  println("title: "+       feed.getTitle());
  println("status: "+      feed.getStatus());
  println("description: "+ feed.getDescription());
  println("feed: "+        feed.getFeed());
  println("website: "+     feed.getWebsite());
  println("updated: "+     feed.getUpdated());
  println("created: "+     feed.getCreated());
  println("version: "+     feed.getVersion());
  println("user: "+        feed.getUser());

  println("\n--- datasource description ---");
  println("location name: "+   feed.getLocationName());
  println("domain: "+          feed.getDomain());
  println("disposition: "+     feed.getDisposition());
  println("elevation: "+       feed.getElevation());
  println("latitude: "+        feed.getLat());  //returns a double
  println("longitude: "+       feed.getLon());  //returns a double
  println("exposure: "+        feed.getExposure());
  println("datastreamCount: "+ feed.getDatastreamCount());
  
  println("\n--- datastream info ---");
  
  int currDatastream = 0;
  
  String id = feed.getStreamId(currDatastream);
  String lastUpdate = feed.getLastUpdateTimestamp(currDatastream);
  println("Datastream '"+ id +"' was last updated at "+ lastUpdate);
  
  float currentVal = feed.getValue(currDatastream);    //returns a float
  println("currVal: "+currentVal);
  
  float maxVal = feed.getStreamMaxVal(currDatastream); //returns a float
  println("maxVal: "+maxVal);

  float minVal = feed.getStreamMinVal(currDatastream); //returns a float
  println("minVal: "+minVal);
}