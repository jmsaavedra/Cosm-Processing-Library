/****************
 Cosm example to add and update metadata of a feed and datastream.

 http://cosm.com
 ****************/

import cosm.*;

DataOut feed;

String apiKey = "YOUR_COSM_API_KEY";
String feedId = "YOUR_FEED_ID";

void setup() {
  
  feed = new DataOut(this, apiKey, feedId);  //intantiate feed
  feed.setVerbose(false);  //optional debug info
}

void draw() {
  //something crazy cool
}

void keyPressed() {
  switch(key) {
  case '1': //hit '1' to create a datastream called firstStream
    feed.setStream("firstStream", 50.21); //initial value of 50.21
    break;

  case '2': //hit '2' to change the ID to "somethingMoreDescriptive" - avoid spaces!
    feed.setStreamId("firstStream", "somethingMoreDescriptive");
    break;

  case '3': //hit '3' to set the min/max range to 5 - 100
    feed.setStreamMinMax("somethingMoreDescriptive", 5, 100);
    break;

  case '4': //set datastream tags. right now only supports 3 tags, no more no less
    feed.setStreamTags("somethingMoreDescriptive", "tag1", "tag2", "tag3");
    break;

  case '5': //set location alone coordinates of the feed //datatype is 'double'
    feed.setFeedLocation(40.72759917538862, -73.97953033447266);
    break;
  
  case '6': //set elevation and location //datatype is 'float'
    feed.setFeedElevation(51.5235375648154, -0.0807666778564453, 23.1);
    break;
    
  case '7': //set location name of the feed
    feed.setFeedLocationName("the office");
    break;

  case '8': //hit '5' to add tags to feed, also only supports 3 tags
    feed.setFeedTags("work", "indoor", "air");
    break;
  }
}
