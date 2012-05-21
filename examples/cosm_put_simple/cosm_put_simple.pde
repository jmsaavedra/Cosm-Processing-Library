/* 
Cosm example to manually pull the current value of any datastream
in the simplest possible way.

http://cosm.com
*/
import cosm.*;

DataOut feed;

//String apiKey = "YOUR_COSM_API_KEY";
String apiKey = "HF9qW31hUvgQ9b9RnUoM5za3BrCSAKxDd1l3MVpLS094ST0g";
String feedId = "60410"; //feed URL: http://cosm.com/feeds/37080

void setup() {
  
  feed = new DataOut(this, apiKey, feedId);
  feed.setVerbose(true);
  feed.update(0, random(1023)); //send request
}

void draw() {
  //awesome stuff goes here
}
