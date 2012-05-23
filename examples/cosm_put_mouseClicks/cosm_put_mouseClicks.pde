/****************
 Cosm example that puts your mouse clicks on the internet.
 
 note: setStream() will automatically create the 
 datastream if it does not already exist. 
 
 http://cosm.com
 ****************/

import cosm.*;

DataOut feed;

String apiKey = "YOUR_COSM_API_KEY";
String feedId = "YOUR_FEED_ID"; //feed URL: http://cosm.com/feeds/37080

void setup() {
  size(600, 800);
  background(0);
  fill(255);

  feed = new DataOut(this, apiKey, feedId);  //intantiate feed
  feed.setVerbose(true);  //optional debug info

  feed.setStreamMinMax("mouseX", 0, width); //set the min to 0, max to width/height of sketch
  feed.setStreamMinMax("mouseY", 0, height);//for both of new datastreams
}

void draw() {

  text("click me! ", width/2, height/2);
}

void mousePressed() {
  background(0);
  text("mouseX: "+ mouseX, 50, 50);
  text("mouseY: "+ mouseY, 50, 75);

  //each time you click, streams gets updated with mouse coordinates
  feed.setStream("mouseX", mouseX); //update "mouseX" datastream with value of mouseX
  feed.setStream("mouseY", mouseY); //update "mouseY" datastream with value of mouseY
}
