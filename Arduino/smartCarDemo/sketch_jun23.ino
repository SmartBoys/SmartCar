#include <SoftwareSerial.h>

// Blueduino comunicate with BLE module through pin11, pin12 as soft serial.
SoftwareSerial mySerial(11, 12); //RX,TX

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
   }  else if(tmp == 'r') {
       analogWrite(backPin,100);
      analogWrite(leftPin,255);
       analogWrite(ledPin, 0);
      analogWrite(rightPin,0);
   }  else if(tmp == 'l') {
       analogWrite(backPin,255);
      analogWrite(leftPin,100);
      analogWrite(ledPin, 0);
      analogWrite(rightPin,0);
   }
    
   // Serial.println(tmp);
  }
  
  //delay(5);
}
