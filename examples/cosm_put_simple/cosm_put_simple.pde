/****************
 Cosm example to manually update a datastream with a random value.
 All you need to do is create a feed on Cosm by logging in and
 clicking "+Device/Feed". setStream() will automatically create the 
 datastream if it does not already exist. 
 
 http://cosm.com
 ****************/

import cosm.*;

DataOut feed;

String apiKey = "YOUR_COSM_API_KEY";
String feedId = "YOUR_FEED_ID";

void setup() {

  feed = new DataOut(this, apiKey, feedId);  //intantiate feed
  feed.setVerbose(false);  //optional debug info

  sendRandomVal();
}

void draw() {
  //awesome stuff goes here
}

void sendRandomVal() {
  float randomVal = random(1023); //create a random value
  feed.setStream(0, randomVal); //send request (datastream id, new value)
  println(randomVal); //look at number printed, then check your feed on cosm!
}

void keyPressed() {
  sendRandomVal();
}