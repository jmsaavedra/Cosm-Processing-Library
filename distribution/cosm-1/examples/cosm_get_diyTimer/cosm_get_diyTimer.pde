/****************
 Example with a simple timer. Pulls new data every 10 seconds.

 http://cosm.com
 ****************/

import cosm.*;

DataIn feed;

String apiKey = "YOUR_COSM_API_KEY";
String feedId = "37080"; //feed URL: http://cosm.com/feeds/37080

long interval = 10; //10 seconds
long timeStamp;

void setup() {
  
  feed = new DataIn(this, apiKey, feedId);
  requestData();
}

void draw() {
  //awesome stuff goes here
  
  int currSeconds = millis()/1000;
  if(currSeconds - timeStamp > interval){
    timeStamp = currSeconds;
    requestData();
  }
}

void requestData() {

  feed.connect(); //send pull request     

  /* get current value */
  
  float myVariable = feed.getValue(0); //getValue by stream number (int)
  //float myVariable = feed.getValue("Cactus");   //or by id name (String)
  
  println("current value: ");
  println(myVariable);
}