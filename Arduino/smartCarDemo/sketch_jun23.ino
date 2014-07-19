#include <SoftwareSerial.h>

// Blueduino comunicate with BLE module through pin11, pin12 as soft serial.
SoftwareSerial mySerial(11, 12); //RX,TX
/**
         FORWARD = "f";
	 BACK = "b";
	 FORWARD_LEFT = "l";
	 FORWARD_RIGHT = "r";
	 ORIGIN_LEFT = "z";
	 ORIGIN_RIGHT = "y";
	 BACK_LEFT = "n";
	 BACK_RIGHT = "m";
	 STOP = "s";
	 LIGHT_ON = "o";
	 LIGHT_OFF = "c";
	 FLASH_LIGHT_ON = "t";
*/

char tmp; 
int ledPin = 6;
int rightPin = 5;
int backPin = 9;
int leftPin = 10;
void setup() {
  
  /*pinMode(ledPin, OUTPUT);
  
  Serial.begin(9600);
  while (!Serial) {
    ; // wait for serial port to connect. Needed for Leonardo only
  }*/
  
  // Start soft serial;
  mySerial.begin(9600); 
};

void loop() {
  while (mySerial.available() > 0)  {
    tmp = char(mySerial.read());
 if(tmp == 's') {
       analogWrite(ledPin, 0);
       analogWrite(rightPin,0);
       analogWrite(backPin, 0);
      analogWrite(leftPin,0);
   }
  else if(tmp == 'f') {
       analogWrite(backPin, 255);
      analogWrite(leftPin,255);
      analogWrite(ledPin, 0);
      analogWrite(rightPin,0);
   }   else if(tmp == 'b') {
        analogWrite(backPin, 0);
      analogWrite(leftPin,0);
      analogWrite(ledPin, 255);
      analogWrite(rightPin,255);
   }  else if(tmp == 'y') {
      analogWrite(backPin,0);
      analogWrite(leftPin,255);
       analogWrite(ledPin, 255);
      analogWrite(rightPin,0);
   }  else if(tmp == 'z') {
       analogWrite(backPin,255);
      analogWrite(leftPin,0);
      analogWrite(ledPin, 0);
      analogWrite(rightPin,255);
   }  else if(tmp == 'l') {
       analogWrite(backPin,255);
      analogWrite(leftPin,80);
      analogWrite(ledPin, 0);
      analogWrite(rightPin,0);
   }  else if(tmp == 'r') {
       analogWrite(backPin,80);
      analogWrite(leftPin,255);
      analogWrite(ledPin, 0);
      analogWrite(rightPin,0);
   }  else if(tmp == 'n') {
       analogWrite(backPin,0);
      analogWrite(leftPin,0);
      analogWrite(ledPin, 255);
      analogWrite(rightPin,80);
   }  else if(tmp == 'm') {
       analogWrite(backPin,0);
      analogWrite(leftPin,0);
      analogWrite(ledPin, 80);
      analogWrite(rightPin,255);
   }
    
   // Serial.println(tmp);
  }
  
  //delay(5);
}
