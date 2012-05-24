/****************
 Simple way to get all datastreams from the same feed with a for loop.
 
 http://cosm.com
 ****************/
 
import cosm.*;

DataIn feed;

String apiKey = "YOUR_COSM_API_KEY";
String feedId = "37080"; //http://cosm.com/feeds/37080

void setup() {

  feed = new DataIn(this, apiKey, feedId); //instantiate feed
  feed.setVerbose(true); //optional debug info toggle
  requestData();
}

void draw() {
  //awesome stuff goes here
}

void keyPressed(){
  requestData(); //hit a key to refresh data
}

void requestData() {

  feed.connect(); //send a request for data
  println("Feed Title: "+ feed.getTitle());
  println("Feed Description: "+ feed.getDescription()+"\n");
  
  for (int i=0; i < feed.getDatastreamCount(); i++) {  
    int currDatastream = i;

    String id = feed.getStreamId(currDatastream);
    String lastUpdate = feed.getLastUpdateTimestamp(currDatastream);
    println("Datastream '"+ id +"' was last updated at "+ lastUpdate);

    float currVal = feed.getValue(currDatastream);
    println("\tcurVal: "+currVal);

    float maxV = feed.getStreamMaxVal(currDatastream);
    println("\tmaxVal: "+maxV);

    float minV = feed.getStreamMinVal(currDatastream);
    println("\tminVal: "+minV);
    
    List<String> tags = feed.getStreamTagsAsList(currDatastream);
    println("\ttags: "+tags);
    println("-------------------");
  }
}
