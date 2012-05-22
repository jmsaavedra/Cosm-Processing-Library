/****************
 Cosm example to manually pull the current value of any datastream
 in the simplest possible way.

 In order to read a feed from Cosm, you need an API Key.

 http://cosm.com
 ****************/

import cosm.*;

DataIn feed;

String apiKey = "YOUR_COSM_API_KEY";
String feedId = "37080"; //feed URL: http://cosm.com/feeds/37080

void setup() {
  
  feed = new DataIn(this, apiKey, feedId);
  requestData();
}

void draw() {
  //awesome stuff goes here
}

void requestData() {

  feed.connect(); //send request

  int thisDatastream = 0;               //getValue by stream number (int)
  //String thisDatastream = "JadePlant";   //or by id name! (String)

  float myVariable = feed.getValue(thisDatastream); /* get current value */

  println("Datastream "+ thisDatastream +" current value: ");
  println(myVariable);
}
