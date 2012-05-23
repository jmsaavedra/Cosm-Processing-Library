/****************
 Cosm example to manually pull the current value of any datastream
 in the simplest possible way.

 In order to read a feed from Cosm, you need an API Key. Get one by
 logging in to Cosm and clicking your username > Keys.

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

  feed.connect(); //send pull request     

  /* get current value */
  
  float myVariable = feed.getValue(0); //getValue by stream number (int)
  //float myVariable = feed.getValue("Cactus");   //or by id name (String)
  
  println("current value: ");
  println(myVariable);
}