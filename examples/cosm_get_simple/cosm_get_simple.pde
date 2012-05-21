/* 
Cosm example to manually pull the current value of any datastream
in the simplest possible way.

http://cosm.com
*/
import cosm.*;

DataIn feed;

//String apiKey = "YOUR_COSM_API_KEY";
String apiKey = "HF9qW31hUvgQ9b9RnUoM5za3BrCSAKxDd1l3MVpLS094ST0g";
String feedId = "37080"; //feed URL: http://cosm.com/feeds/37080

void setup() {
  
  feed = new DataIn(this, apiKey, feedId);
  requestData();
}

void draw() {
  //awesome stuff goes here
}

void requestData(){

  feed.connect(); //send request
  
  int currDataStream = 0;               //getValue by stream number (int)
  //String currDataStream = "Cactus";   //or by id name! (String)
  
  float myVariable = feed.getValue(currDataStream); /* get current value */

  println("Datastream '"+ currDataStream +"' current value: ");
  println(myVariable);
}