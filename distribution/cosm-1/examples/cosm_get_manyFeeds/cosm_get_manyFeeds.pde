/****************
 Cosm example to pull from several Feeds at one time. 
 
 http://cosm.com
 
 jos.ph may 2012 
 ****************/

import cosm.*;

int numFeeds = 5; //how many feeds do you want to pull
DataIn[] feed = new DataIn[numFeeds];

String apiKey = "HF9qW31hUvgQ9b9RnUoM5za3BrCSAKxDd1l3MVpLS094ST0g";
String[] feedid = {
  "49544", "504", "53914", "37080", "40315"  //FeedIDs we are interested in
}; 

void setup() {

  for (int i=0; i<numFeeds; i++) { //set up feeds
    feed[i] = new DataIn(this, apiKey, feedid[i]);
  }
  requestData();
}

void draw() {
  //awesome stuff goes here
}

void requestData() {

  float[] myVar = new float[numFeeds];
  int datastreamNum = 1; // which stream do you want to look at from all the feeds

  //send requests, parse data
  for (int i=0; i<numFeeds; i++) {//you will get a server response 
    feed[i].connect();            //for each connection made (each feed)
    myVar[i] = feed[i].getValue(datastreamNum); //the array myVar stores all feeds' data
    println("Datastream "+ datastreamNum +" from feed: "+ feed[i].getTitle() +" current value: ");
    println(myVar[i]);
    println("-----------");
  }
}

