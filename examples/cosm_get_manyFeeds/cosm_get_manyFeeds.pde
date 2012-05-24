/****************
 Cosm example to pull from several Feeds at one time. 
 dev note:the threading class will be great for this.
 
 http://cosm.com
 ****************/

import cosm.*;

int numFeeds = 5; //how many feeds do you want to pull
DataIn[] feed = new DataIn[numFeeds];

String apiKey = "YOUR_COSM_API_KEY";
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
  //crazy viz
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

