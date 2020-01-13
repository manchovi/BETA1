#include <LiquidCrystal_I2C.h>         //Libreria Pantalla I2C
LiquidCrystal_I2C lcd(0x27,20,4);      // Configuracion pantalla I2C
//#include <dht.h>                     // Libreria Sensor DTH11
#include "DHT.h"
#include <SPI.h>                       // Libreria de Sincronizacion arduino/pc
#include <Ethernet.h>                  // Liberia SHIELD Ethernet
EthernetClient client;
byte mac[] = { 0xDE, 0xAD, 0xBE, 0xEF, 0xFE, 0xED };// MAC SHIELD ETHERNET 
//char server[] = "192.168.0.1";       // 
char server[] = "192.168.100.2"; 
//dht DHT;
//#define DHT11_PIN 3                  // PIN DE DTH11

float h=0.0;
float t=0.0;
int levelValueSensibility=250;

#define DHTPIN 2  
#define DHTTYPE DHT22 
//Inicializa el sensor DHTxx
DHT dht(DHTPIN, DHTTYPE);

int LDR = A15;                  //Pin analogico del arduino donde ira conectada LDR
int LDR1 = A14;                 //Pin analogico del arduino donde ira conectada LDR 
int LDR2 = A13;                 //Pin analogico del arduino donde ira conectada LDR
int LDR3 = A12;                 //Pin analogico del arduino donde ira conectada LDR
int LDR4 = A11;                 //Pin analogico del arduino donde ira conectada LDR
int LDR5 = A10;                 //Pin analogico del arduino donde ira conectada LDR

 int s1;
  int s2;
   int s3;
    int s4;
     int s5;
      int s6;
 
void setup()
{
lcd.init();                    //Iniciar pantalla LCD                  
lcd.backlight();               //Reset pantalla LCD
Ethernet.begin(mac);           //Inicia Ethernet Shield
Serial.begin(115200);          //Inicia Monitor Serial 
}
void loop(){
//    if(sensorValueA0 <= levelValueSensibility){
//      datoSensorLuz1 = 1;
//  }else{
//      datoSensorLuz1 = 0;
//  }

    delay(1000);
    s1 = analogRead(LDR);
    s2 = analogRead(LDR1);
    s3 = analogRead(LDR2);
    s4 = analogRead(LDR3);
    s5 = analogRead(LDR4);
    s6 = analogRead(LDR5);
    Serial.println();
    Serial.print("LDR1: ");
    Serial.print(s1);
    Serial.print(" - LDR2: ");
    Serial.print(s2);
    Serial.print(" - LDR3: ");
    Serial.print(s3);
    Serial.print(" - LDR4: ");
    Serial.print(s4);
    Serial.print(" - LDR5: ");
    Serial.print(s5);
    Serial.print(" - LDR6: ");
    Serial.print(s6);
    Serial.println();

    delay(2000);
    h = dht.readHumidity();
    t = dht.readTemperature();
    if(isnan(h) || isnan(t)){
      Serial1.println(F("Failed to read from DHT sensor!"));
      return;
    }
  
    lcd.setCursor(1, 0);
    lcd.print("Humedad: ");
    //lcd.print(DHT.humidity);
    lcd.print(h);
    lcd.print("%  ");
    lcd.setCursor(0, 1);
    lcd.print("Tempera: ");
    //lcd.print(DHT.temperature);
    lcd.print(t); 
    lcd.println("C  ");
  
    Serial.print("Temperatura = ");
    //Serial.print(DHT.temperature);
    Serial.print(t);
    Serial.println(" *C");
    Serial.print("Humidity = ");
    //Serial.print(DHT.humidity);
    Serial.print(h);
    Serial.println(" %");

   // Leer datos
   //int chk = DHT.read11(DHT11_PIN);
   if (client.connect(server, 80)) {
    client.print( "GET /UTLA-2020/portalutla/processdata.php?");
    client.print("temperatura=");
    //client.print(DHT.temperature);
    client.print(t);
    client.print("&&");
    client.print("luz1=");
    
  if (analogRead(LDR)<= levelValueSensibility){
     client.print("1");
  } else {
    client.print("0");
  }
    client.print("&&");
    client.print("luz2=");
  if (analogRead(LDR1)<= levelValueSensibility){
     client.print("1");
  } else {
    client.print("0");
  }
    client.print("&&");
    client.print("luz3=");
  if (analogRead(LDR2)<= levelValueSensibility){
     client.print("1");
  } else {
    client.print("0");
  }
    client.print("&&");
    client.print("luz4=");
  if (analogRead(LDR3)<= levelValueSensibility){
     client.print("1");
  } else {
    client.print("0");
  }
    client.print("&&");
    client.print("luz5=");
  if (analogRead(LDR4)<= levelValueSensibility){
     client.print("1");
  } else {
    client.print("0");
  }
    client.print("&&");
    client.print("luz6=");
  if (analogRead(LDR5)<= levelValueSensibility){
     client.print("1");
  } else {
    client.print("0");
  }
    client.print("&&");
    client.print("humedad=");
    //client.print(DHT.humidity);
    client.print(h);
    
    client.println("HTTP/1.1");
    client.println("Connection: close");
    client.stop();
    Serial.println("CONECTADO");
  }
  else {
    Serial.println("–> FALLO DE CONECCION/n");
  }
  delay(6000);
}
