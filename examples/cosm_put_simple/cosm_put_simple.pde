/****************
 Cosm example to manually update a datastream with a random value.

 http://cosm.com
 ****************/

import cosm.*;

DataOut feed;

String apiKey = "YOUR_COSM_API_KEY";
String feedId = "YOUR_FEED_ID";

void setup() {
  
  feed = new DataOut(this, apiKey, feedId);  //intantiate feed
  feed.setVerbose(false);  //optional debug info
  
  float randomVal = random(1023); //create a random value
  feed.updateStream(1, randomVal); //send request (datastream id, new value)
  
  println(randomVal); //look at number in processing, then check your feed on cosm!
}

void draw() {
  //awesome stuff goes here
}