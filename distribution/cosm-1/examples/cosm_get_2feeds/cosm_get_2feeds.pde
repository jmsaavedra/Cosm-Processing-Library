/****************
 Cosm example to manually pull the current value of any datastream
from 2 different feeds.

 http://cosm.com

 jos.ph may 2012 
 ****************/

import cosm.*;

DataIn feed1;
DataIn feed2;

String apiKey = "YOUR_COSM_API_KEY";
String feed1id = "37080"; //feed URL: http://cosm.com/feeds/37080
String feed2id = "504";   //feed URL: http://cosm.com/feeds/504

void setup() {
  
  feed1 = new DataIn(this, apiKey, feed1id); //set up feeds
  feed2 = new DataIn(this, apiKey, feed2id);
  requestData();
}

void draw() {
  //awesome stuff goes here
}

void requestData(){

  //send requests
  feed1.connect(); //you will get a server response 
  feed2.connect(); //for each connection made
  
  int dataStreamNum = 0;
  
  float myVar1 = feed1.getValue(dataStreamNum); // get current values
  float myVar2 = feed2.getValue(dataStreamNum);

  print("Datastream "+ dataStreamNum +" from feed: "+ feed1.getTitle() +", current value:  ");
  println(myVar1);
  print("Datastream "+ dataStreamNum +" from feed: "+ feed2.getTitle() +", current value:  ");
  println(myVar2);
}