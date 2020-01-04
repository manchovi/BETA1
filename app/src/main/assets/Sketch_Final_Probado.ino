/*
 * **********************************************************************************
 *           Código fuente de Arduino Mega, Funcionable Versión Final               *
 * * ********************************************************************************
 * TRABAJO DE GRADUACIÓN PARA INGENIERO EN ELECTRÓNICA.
 * FECHA: 13:07:2019, HORA: 05:00:00 p.m.
 * FINAL VERSIÓN: MEMORIA UTILIZADA (31%) DEL ESPACIO PARA ALMACENAMIENTO DEL PROGRAMA
 * FINAL VERSIÓN: LE CAMBIA LA LECTURA DE ESTADO DE LOS FOCOS DE DIGITAL A ANALÓGICO.
 * FINAL VERSIÓN: 04/01/2020 --> Probada
 */
#include <Wire.h>
#include "DHT.h"
 //const byte rxPin = 2;
 //const byte txPin = 3;
 //configura un nuevo objeto serie
 //SoftwareSerial mySerial (rxPin, txPin);
 //SoftwareSerial(rxPin, txPin, inverse_logic)
 //Terminales en la tarjeta GSM/GPRS SIM900 [ D7(TX), D8(RX) ]
#include <SoftwareSerial.h>
//SoftwareSerial SIM900(7, 8);  //En ARDUINO     RX  ,  TX    Configura el puerto serial para el SIM900. Para el Arduino MEGA utilizar pines 10 y 11
SoftwareSerial SIM900(10, 11);  //               RX  ,  TX
//SoftwareSerial gsm(7, 8)      //               RX  ,  TX

#define DHTPIN 2
#define DHTTYPE DHT22
//Inicializa el sensor DHTxx
DHT dht(DHTPIN, DHTTYPE);

String inputString = "";             // a string to hold incoming data
boolean stringComplete = false;      // whether the string is complete

//VARIABLES PARA LOS SENSORES DE ESTADO DE LAS LUCES.
int statuSensorLuz1 = 30;int statuSensorLuz2 = 31;int statuSensorLuz3 = 32;int statuSensorLuz4 = 33;
int statuSensorLuz5 = 34;int statuSensorLuz6 = 35;int statuSensorLuz7 = 36;int statuSensorLuz8 = 37;

/** dos LED indicadores de salida de ciclo setup y sistema listo!  **/
#define indicador2 12
#define indicador1 13

#define pin_to_send_sms_contact 5
int status_pin_sms = 0;

/*INICIO DEL BLOQUE 1 - BT*/
#define foco0 46
#define foco1 47
#define foco2 48
#define foco3 49
#define foco4 50
#define foco5 51
#define foco6 52
#define foco7 53

bool estadofoco1 = true ; //Variable para guardar el estado del LED
bool estadofoco2 = true ;
bool estadofoco3 = true ;
bool estadofoco4 = true ;
bool estadofoco5 = true ;
bool estadofoco6 = true ;
bool estadofoco7 = true ;
bool estadofoco8 = true ;
bool estadofocotodos = true ;

// Analog input pins
int analogInPin0 = A0;int analogInPin1 = A1;int analogInPin2 = A2;
int analogInPin3 = A3;int analogInPin4 = A4;int analogInPin5 = A5;
int analogInPin6 = A6;int analogInPin7 = A7;

int sensorValueA0=0;int sensorValueA1=0;int sensorValueA2=0;int sensorValueA3=0;
int sensorValueA4=0;int sensorValueA5=0;int sensorValueA6=0;int sensorValueA7=0;

//Variable para configurar a que valor de la lectura de los sensores LDR será 1 y 0, para decir
//que hay luminaria en estado ON u OFF respectivamente.
int levelValueSensibility=250;

 int datoSensorLuz1 = 0; int datoSensorLuz2 = 0; int datoSensorLuz3 = 0; int datoSensorLuz4 = 0;
 int datoSensorLuz5 = 0; int datoSensorLuz6 = 0; int datoSensorLuz7 = 0; int datoSensorLuz8 = 0;

int exist_register =0;int position_location=0;

int comprobarFOCO1_ON=0, comprobarFOCO2_ON=0, comprobarFOCO3_ON=0, comprobarFOCO4_ON=0,
    comprobarFOCO5_ON=0, comprobarFOCO6_ON=0, comprobarFOCO7_ON=0, comprobarFOCO8_ON=0, comprobarALLBTON=0;
int comprobarFOCO1_OFF=0, comprobarFOCO2_OFF=0, comprobarFOCO3_OFF=0, comprobarFOCO4_OFF=0,
    comprobarFOCO5_OFF=0,comprobarFOCO6_OFF=0, comprobarFOCO7_OFF=0, comprobarFOCO8_OFF=0, comprobarALLBTOFF=0;

int comprobarCallme=0, comprobarluceslab=0, comprobartemphum=0, comprobarinfotodosensores=0;

bool flag1=false;
boolean bandera = false;
/*
 * CADENAS DE CONTROL DE CARGAS POR SERIAL - BLUETOOTH
 */
char allfocosBTON[] = {'m','a','n','u','e','l','\0'};
char allfocosBTOFF[] = {'j','e','s','u','s','\0'};

char focounoON[]={'f','o','c','o','u','n','o','e','n','c','i','e','n','d','e','\0'};                //cadena de control: focounoenciende
char focounoOFF[]={'f','o','c','o','u','n','o','a','p','a','g','a','t','e','\0'};

char focodosON[]={'f','o','c','o','d','o','s','e','n','c','i','e','n','d','e','\0'};                //cadena de control: focodosenciende
char focodosOFF[]={'f','o','c','o','d','o','s','a','p','a','g','a','t','e','\0'};

char focotresON[]={'f','o','c','o','t','r','e','s','e','n','c','i','e','n','d','e','\0'};           //cadena de control: focotresenciende
char focotresOFF[]={'f','o','c','o','t','r','e','s','a','p','a','g','a','t','e','c','h','o','v','i','\0'};

char fococuatroON[]={'f','o','c','o','c','u','a','t','r','o','e','n','c','i','e','n','d','e','\0'}; //18
char fococuatroOFF[]={'f','o','c','o','c','u','a','t','r','o','a','p','a','g','a','t','e','\0'};    //17

char fococincoON[]={'f','o','c','o','c','i','n','c','o','e','n','c','i','e','n','d','e','\0'};      //17
char fococincoOFF[]={'f','o','c','o','c','i','n','c','o','a','p','a','g','a','t','e','\0'};         //16

char focoseisON[]={'f','o','c','o','s','e','i','s','e','n','c','i','e','n','d','e','\0'};           //16
char focoseisOFF[]={'f','o','c','o','s','e','i','s','a','p','a','g','a','t','e','\0'};              //15

char focosieteON[]={'f','o','c','o','s','i','e','t','e','e','n','c','i','e','n','d','e','\0'};      //17
char focosieteOFF[]={'f','o','c','o','s','i','e','t','e','a','p','a','g','a','t','e','\0'};         //16

char focoochoON[]={'f','o','c','o','o','c','h','o','e','n','c','i','e','n','d','e','\0'};           //16
char focoochoOFF[]={'f','o','c','o','o','c','h','o','a','p','a','g','a','t','e','\0'};              //15

//New 25/06/2018
//char hacerllamada[]={'L','l','a','m','a','m','e','s','i','s','t','e','m','a','\0'};                 //14
char infoluceslab[]={'i','n','f','o','l','u','c','e','s','l','a','b','\0'};                         //12
char infotemphumedad[]={'i','n','f','o','t','e','m','p','h','u','m','e','d','a','d','\0'};          //15

//todosensoresinfoki = 18 caracteres.
char infotodosensores[]={'t','o','d','o','s','e','n','s','o','r','e','s','i','n','f','o','k','i','\0'}; //18

 float h=0.0;
 float t=0.0;

char cadena[24]; // Declaramos una variable para almacenar lo que tenemos en cola en el puerto serial,
                 // que después asignaremos a otras variable en función de la combinación del Mux/Demux(0-3).
byte contador=0; // Variable contador para la asignación de valores del Puerto Serial entrantes,mediante función Case
/*FIN DEL BLOQUE 1 - BT*/

char incoming_char = 0;
String mensaje = "";
int estadoLuz1 = 0;
int estadoLuz2 = 0;
int allLuces = 0;
//CARGAS A CONTROLAR.
//int LUZ1 = 12;
//int LUZ2 = 13;
int dato = 0;
int respuesta;
char aux_str[50];
int senalInicio = 0;
int chovi = 0;
//Contenido del sms que enviamos. \x1A corresponde al caracter de finalizacion

//char sms[] = "SISTEMA DE CONTROL Y MONITOREO GSM LISTO.\n\nTRABAJO DE GRADUACION\nUTLA 2018-2019 \x1A \r\n"; //91 CARACTERES.
char sms1[] = "SISTEMA DE CONTROL Y MONITOREO LISTO!\n\nESTOY MUY ATENTO A SUS PETICIONES. \x1A \r\n";   //91 CARACTERES.

//String agenda[] = {"73216953",
//                   "74112322",
//                   "61107065",
//                   "77646452",
//                   "74112646",
//                   "61319995",
//                   "78305215",
//                   "003468012133"};

/*
String agenda[] = {"78305215",
                   "61107065",
                   "61319995"};

 String agenda[] = {"78305215",
                    "61107065"};  */

 /* String agenda[] = {"61107065",
                    "78305215"};    */

/*String agenda[] = {"78305215",
                   "61107065"}; */

String agenda[] = {"61107065",
                   "74112646",
                   "71660340"};

//int numtotal = 6;                                                  //Cantidad de numeros de telefono autorizados
int size_Agenda = (sizeof(agenda)/sizeof(agenda[0]));
String numDestino="";

String mensajeFTDIserial = "";

//Función para reiniciar sistema por software.
void(* reset) (void)=0;

void setup(){
  /*INICIO DEL BLOQUE 2 - BT*/
  Serial1.begin(9600);
  Serial.begin(9600);
  pinMode(foco0, OUTPUT);
  pinMode(foco1, OUTPUT);
  pinMode(foco2, OUTPUT);
  pinMode(foco3, OUTPUT);
  pinMode(foco4, OUTPUT);
  pinMode(foco5, OUTPUT);
  pinMode(foco6, OUTPUT);
  pinMode(foco7, OUTPUT);

  pinMode(indicador1, OUTPUT);
  pinMode(indicador2, OUTPUT);

  pinMode(pin_to_send_sms_contact, INPUT);

  estadoInicialRelay();                      //Nueva función apartir de la version SMS_jo6

  /*
   * BLOQUE DE CAPTURA DE SEÑALES DE LOS SENSORES. INICIO
   */
   pinMode(statuSensorLuz1, INPUT); pinMode(statuSensorLuz2, INPUT); pinMode(statuSensorLuz3, INPUT); pinMode(statuSensorLuz4, INPUT);
   pinMode(statuSensorLuz5, INPUT); pinMode(statuSensorLuz6, INPUT); pinMode(statuSensorLuz7, INPUT); pinMode(statuSensorLuz8, INPUT);
  /*
   * BLOQUE DE CAPTURA DE SEÑALES DE LOS SENSORES. FIN
   */

  /*FIN DEL BLOQUE 2 - BT*/
  SIM900.begin(19200); //Configura velocidad del puerto serie para el SIM900
  //Serial.begin(19200); //Configura velocidad del puerto serie del Arduino

  //Inicializa Sensor DHTxx
  //Serial1.println("Iniciando Sensor DHT22");
  //Serial.println("Iniciando Sensor DHT22");
  dht.begin();
  delay(1000);

  //Serial1.println("Iniciando...");
  Serial1.println(F("BEGIN SYSTEM 2019-2020"));
  power_on();
  iniciar();

  for(int i=0; i<5; i++){
      print_port(indicador1, HIGH);
      print_port(indicador2, HIGH);
      delay(1000);
      print_port(indicador1, LOW);
      print_port(indicador2, LOW);
      delay(1000);
  }

  Serial1.println(F("CONFIGURACIONES COMPLETADAS."));
  print_port(indicador1, HIGH);
  Serial1.println(F("READY SYSTEM!!!"));

}

void loop(){
  chovi=0;

  /*INICIO DEL BLOQUE 3 - BT*/
  resetCounter();flag1=false;bandera = false;

  /*
   * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*
   * *Función para que el módulo bluetooth este transmitiendo en tiempo real por su canal
   * * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*
   */

  //De aquí en adelante es el control por BT.
  if (Serial.available()>0) {               // Cuando tengamos cola en la entrada del puerto serial, buffer,
    //memset(respuesta, '\0', 100);         // Inicializa el string
    memset(cadena, '\0', sizeof(cadena));   // Primero limpiamos la variable de registros anteriores
    while (Serial.available ()>0) {         // Mientras tengamos un valor en el buffer
    delay(5);                               // Esperamos 5ms para evitar rotura del mensaje, ya que si lo leemos directamente puede generarnos errores en forma de ruido.
    cadena[contador]=Serial.read();         // Asignamos la lectura del buffer en la variable cadena en la posición contador.
                                            // Sumamos +1 al contador para cambiar de posición en la variable cadena.
    //Función para comparar cadenas entrantes con arreglos definidos
    compararBT();
    contador++;
   }
    contador=0;                             // Reiniciamos el contador.
    bandera = true;
    }
    controllerLoadBT();
  /*     Fin del control por BT        */

  status_pin_sms = digitalRead(pin_to_send_sms_contact);
  if(senalInicio == 0 && status_pin_sms==HIGH){
    for(int i=0;i<size_Agenda;i++){
      //mensaje_sms(agenda[i]);
      mensaje_universal(agenda[i], sms1);
      delay(2500);
    }
   //mensaje_sms();                     //MENSAJE DE SISTEMA EN MARCHA Y LISTO PARA EL CONTROL Y MONITOREO.
   senalInicio = 1;
  }

  if (SIM900.available() > 0){
    //char inChar = (char)Serial.read();
    incoming_char = SIM900.read();       //Guardamos el carácter del GPRS
    delay(10);
    /*Estoy pensando en enviar ente serial.print aun módulo FTDI conectado
     *al puerto serial1. :Serial1.print(incoming_char);
    */
    //Serial.print(incoming_char);         //Mostramos el carácter en el monitor serie
    mensaje = mensaje + incoming_char ;    //Añadimos el carácter leído al mensaje

  }
    /*for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        //telefono= agenda[i];          //Función para enviar SMS de respuesta a destinatario autorizado.
        exist_register=1;
        position_location=i;
      }
    }

    if(exist_register==1){
      printerFTDI("\nContacto encontrado en la posicion:" +String(position_location)+ "," +String(agenda[position_location]));
      exist_register=0;
      position_location=0;
    }*/

    identifica_SMS();
    identifica_llamada();

    //Serial.flush();

 } //Fin de loop

 void info_all_sensores(){
  /*
   * CADENA RESULTANTE FINAL: x:datoVariable# datoVariable$ datoVariable% datoVariable
   *                           &datoVariable* datoVariable, datoVariable. datoVariable
  */
  status_luces_BT1();
  Serial.print("x");
  Serial.print(":" + String(datoSensorLuz1) + "#" + String(datoSensorLuz2) + "$" + String(datoSensorLuz3) + "%" + String(datoSensorLuz4));
  Serial.print("&" + String(datoSensorLuz5) + "*" + String(datoSensorLuz6) + "," + String(datoSensorLuz7) + "." + String(datoSensorLuz8));
  /*
   * CADENA RESULTANTE FINAL: +temperatura!humedadrelativa~
  */
  temp_rh_BT1();
  Serial.print("+");
  Serial.print(String(t) + "!" + String(h));
  Serial.print("~");
  //Serial.println();
  delay(10);

     //Patrón de datos sensores:
     //                  x:datoVariable# datoVariable$ datoVariable% datoVariable
     //                   &datoVariable* datoVariable, datoVariable. datoVariable
     //                   +temperatura!humedadrelativa~
 }

 void status_luces_BT1(){
  delay(1000);
  sensorValueA0 = analogRead(analogInPin0);
  sensorValueA1 = analogRead(analogInPin1);
  sensorValueA2 = analogRead(analogInPin2);
  sensorValueA3 = analogRead(analogInPin3);
  sensorValueA4 = analogRead(analogInPin4);
  sensorValueA5 = analogRead(analogInPin5);
  sensorValueA6 = analogRead(analogInPin6);
  sensorValueA7 = analogRead(analogInPin7);

  if(sensorValueA0 <= levelValueSensibility){
      datoSensorLuz1 = 1;
  }else{
      datoSensorLuz1 = 0;
  }

  if(sensorValueA1 <= levelValueSensibility){
      datoSensorLuz2 = 1;
  }else{
      datoSensorLuz2 = 0;
  }

  if(sensorValueA2 <= levelValueSensibility){
      datoSensorLuz3 = 1;
  }else{
      datoSensorLuz3 = 0;
  }

  if(sensorValueA3 <= levelValueSensibility){
      datoSensorLuz4 = 1;
  }else{
      datoSensorLuz4 = 0;
  }

  if(sensorValueA4 <= levelValueSensibility){
      datoSensorLuz5 = 1;
  }else{
      datoSensorLuz5 = 0;
  }

  if(sensorValueA5 <= levelValueSensibility){
      datoSensorLuz6 = 1;
  }else{
      datoSensorLuz6 = 0;
  }

  if(sensorValueA6 <= levelValueSensibility){
      datoSensorLuz7 = 1;
  }else{
      datoSensorLuz7 = 0;
  }

  if(sensorValueA7 <= levelValueSensibility){
      datoSensorLuz8 = 1;
  }else{
      datoSensorLuz8 = 0;
  }

  delay(2);
}

/*
void status_luces_BT1(){
  delay(1000);

  datoSensorLuz1 = digitalRead(statuSensorLuz1);
  datoSensorLuz2 = digitalRead(statuSensorLuz2);
  datoSensorLuz3 = digitalRead(statuSensorLuz3);
  datoSensorLuz4 = digitalRead(statuSensorLuz4);
  datoSensorLuz5 = digitalRead(statuSensorLuz5);
  datoSensorLuz6 = digitalRead(statuSensorLuz6);
  datoSensorLuz7 = digitalRead(statuSensorLuz7);
  datoSensorLuz8 = digitalRead(statuSensorLuz8);
}
*/

void status_luces_BT(){
  status_luces_BT1();
  /*
   * CADENA RESULTANTE FINAL: @:datoVariable# datoVariable$ datoVariable% datoVariable
   *                           &datoVariable* datoVariable, datoVariable. datoVariable~
  */
  //Serial.print(F("@"));
  Serial.print("@");
  Serial.print(":" + String(datoSensorLuz1) + "#" + String(datoSensorLuz2) + "$" + String(datoSensorLuz3) + "%" + String(datoSensorLuz4));
  Serial.print("&" + String(datoSensorLuz5) + "*" + String(datoSensorLuz6) + "," + String(datoSensorLuz7) + "." + String(datoSensorLuz8));
  Serial.print("~"); //con esto damos a conocer la finalización del String de datos
  //Serial.print(F("~")); //con esto damos a conocer la finalización del String de datos
  delay(10);
}

void temp_rh_BT1(){
  //Wait a few seconds between measurements.
  delay(2000);
  //Reading temperature or humedity takes about 250 milliseconds!
  /*
  float h = dht.readHumidity();
  float t = dht.readTemperature();
  */
  h = dht.readHumidity();
  t = dht.readTemperature();

  if(isnan(h) || isnan(t)){
    Serial1.println(F("Failed to read from DHT sensor!"));
    return;
  }
  /*
  Serial.print("Humidity: ");
  Serial.print(h);
  Serial.print("Temperature: ");
  Serial.print(t);
  Serial.print(" *C ");
  */
}

void temp_rh_BT(){
  temp_rh_BT1();
  /*
   * CADENA RESULTANTE FINAL: #:temperatura,humedadrelativa~
  */
  Serial.print("#"); //hay que poner # para el comienzo de los datos, así Android sabe que empieza el String de datos
  Serial.print(":");
  Serial.print(String(t) + "," + String(h));
  Serial.print("~"); //con esto damos a conocer la finalización del String de datos
  //Serial.println();
  delay(10);        //agregamos este delay para eliminar tramisiones faltantes
}

/*INICIO DEL BLOQUE 4 - BT*/
  void controllerLoadBT(){
    //todosensoresinfoki = 18 caracteres.
    //infotodosensores[] = 18 ,  comprobarinfotodosensores
    if (flag1==true && comprobarinfotodosensores==18){
      info_all_sensores();
      Serial1.println("\nSolicitud completa de informacion de sensores.");
   }

  //hacerllamada[]= 14,     int comprobarCallme=0
  /*
  if (flag1==true && comprobarCallme==14){
      //llamar();
      Serial1.println("\nSolicitud de llamada desde la via Bluetooth.");
   }
   */

  //infoluceslab[]= 12,    comprobarluceslab=0
  if (flag1==true && comprobarluceslab==12){
      status_luces_BT();
      Serial1.println("\nSolicitud de SMS del estado de todas las luces via Bluetooth.");
   }

  //infotemphumedad[]= 15,   comprobartemphum=0;
  if (flag1==true && comprobartemphum==15){
      temp_rh_BT();
      Serial1.println("\nSolicitud de SMS del estado de temperatura y humedad relativa via Bluetooth.");
   }


  if (flag1==true && comprobarFOCO1_ON==15){
      //print_port(foco0,HIGH);
      estadofoco1 = ! estadofoco1 ; // cambiamos el estado del LED
      print_port(foco0, estadofoco1) ;
      status_luces_BT();
      Serial1.println(F("\nEstado foco # 1 cambiado correctamente."));
   }
   if (flag1==true && comprobarFOCO1_OFF==14){
          print_port(foco0,LOW);
          status_luces_BT();
   }


   if (flag1==true && comprobarFOCO2_ON==15){
      //print_port(foco1,HIGH);
      estadofoco2 = ! estadofoco2 ; // cambiamos el estado del LED
      print_port(foco1, estadofoco2) ;
      status_luces_BT();
      Serial1.println(F("\nEstado foco # 2 cambiado correctamente."));
   }
   if (flag1==true && comprobarFOCO2_OFF==14){
          print_port(foco1,LOW);
          status_luces_BT();
   }

   if (flag1==true && comprobarFOCO3_ON==16){
      //print_port(foco2,HIGH);
      estadofoco3 = ! estadofoco3 ; // cambiamos el estado del LED
      print_port(foco2, estadofoco3) ;
      status_luces_BT();
      Serial1.println(F("\nEstado foco # 3 cambiado correctamente."));
   }
   if (flag1==true && comprobarFOCO3_OFF==20){
          print_port(foco2,LOW);
          status_luces_BT();
   }

   if (flag1==true && comprobarFOCO4_ON==18){
      //print_port(foco3,HIGH);
      estadofoco4 = ! estadofoco4 ; // cambiamos el estado del LED
      print_port(foco3, estadofoco4) ;
      status_luces_BT();
      Serial1.println(F("\nEstado foco # 4 cambiado correctamente."));
   }
   if (flag1==true && comprobarFOCO4_OFF==17){
      print_port(foco3,LOW);
      status_luces_BT();
   }

   if (flag1==true && comprobarFOCO5_ON==17){
      //print_port(foco4,HIGH);
      estadofoco5 = ! estadofoco5 ; // cambiamos el estado del LED
      print_port(foco4, estadofoco5) ;
      status_luces_BT();
      Serial1.println(F("\nEstado foco # 5 cambiado correctamente."));
   }
   if (flag1==true && comprobarFOCO5_OFF==16){
      print_port(foco4,LOW);
      status_luces_BT();
   }

   if (flag1==true && comprobarFOCO6_ON==16){
      //print_port(foco5,HIGH);
      estadofoco6 = ! estadofoco6 ; // cambiamos el estado del LED
      print_port(foco5, estadofoco6) ;
      status_luces_BT();
      Serial1.println(F("\nEstado foco # 6 cambiado correctamente."));
   }
   if (flag1==true && comprobarFOCO6_OFF==15){
      print_port(foco5,LOW);
      status_luces_BT();
   }

   if (flag1==true && comprobarFOCO7_ON==17){
      //print_port(foco6,HIGH);
      estadofoco7 = ! estadofoco7 ; // cambiamos el estado del LED
      print_port(foco6, estadofoco7) ;
      status_luces_BT();
      Serial1.println(F("\nEstado foco # 7 cambiado correctamente."));
   }
   if (flag1==true && comprobarFOCO7_OFF==16){
      print_port(foco6,LOW);
      status_luces_BT();
   }

   if (flag1==true && comprobarFOCO8_ON==16){
      //print_port(foco7,HIGH);
      estadofoco8 = ! estadofoco8 ; // cambiamos el estado del LED
      print_port(foco7, estadofoco8) ;
      status_luces_BT();
      Serial1.println(F("\nEstado foco # 8 cambiado correctamente."));
   }
   if (flag1==true && comprobarFOCO8_OFF==15){
      print_port(foco7,LOW);
      status_luces_BT();
   }

   if (flag1==true && comprobarALLBTON==6){
      /*print_port(foco0,HIGH);
      print_port(foco1,HIGH);
      print_port(foco2,HIGH);
      print_port(foco3,HIGH);
      print_port(foco4,HIGH);
      print_port(foco5,HIGH);
      print_port(foco6,HIGH);
      print_port(foco7,HIGH);*/
      estadofoco1 = ! estadofoco1 ; // cambiamos el estado del LED
      estadofoco2 = ! estadofoco2 ; // cambiamos el estado del LED
      estadofoco3 = ! estadofoco3 ; // cambiamos el estado del LED
      estadofoco4 = ! estadofoco4 ; // cambiamos el estado del LED
      estadofoco5 = ! estadofoco5 ; // cambiamos el estado del LED
      estadofoco6 = ! estadofoco6 ; // cambiamos el estado del LED
      estadofoco7 = ! estadofoco7 ; // cambiamos el estado del LED
      estadofoco8 = ! estadofoco8 ; // cambiamos el estado del LED
       print_port(foco0, estadofoco1) ;
        print_port(foco1, estadofoco2) ;
         print_port(foco2, estadofoco3) ;
          print_port(foco3, estadofoco4) ;
           print_port(foco4, estadofoco5) ;
            print_port(foco5, estadofoco6) ;
             print_port(foco6, estadofoco7) ;
              print_port(foco7, estadofoco8) ;
              status_luces_BT();
      Serial1.println(F("\nEstado de todas las luces ha sido cambiado correctamente."));
   }

   //TenGo QuE aNaLIzaR yA con El CIRcuIto.
   if (flag1==true && comprobarALLBTOFF==5){
     print_port(foco0,HIGH);
     print_port(foco1,HIGH);
     print_port(foco2,HIGH);
     print_port(foco3,HIGH);
     print_port(foco4,HIGH);
     print_port(foco5,HIGH);
     print_port(foco6,HIGH);
     print_port(foco7,HIGH);
     status_luces_BT();
   }
}

void compararBT(){
  //COMPROBANDO CADENA DE ENTRADA EN RX TX DE ARDUINO POR BLUETOOTH.

  /*DE LO ÚLTIMO*/
  //infotodosensores[] = 18 ,  int comprobarinfotodosensores=0
  if (cadena[contador]==infotodosensores[contador]) {
        flag1=true;
         if (flag1==true){
            comprobarinfotodosensores=comprobarinfotodosensores+1;
           }
    }
      if (comprobarinfotodosensores==18){
          Serial1.println(comprobarinfotodosensores);
          Serial1.println();
        }

  //hacerllamada[]= 14,     int comprobarCallme=0
  /*
    if (cadena[contador]==hacerllamada[contador]) {
        flag1=true;
         if (flag1==true){
            comprobarCallme=comprobarCallme+1;
           }
    }
      if (comprobarCallme==14){
          Serial1.println(comprobarCallme);
          Serial1.println();
        }
   */

   //infoluceslab[]= 12,    comprobarluceslab=0
   if (cadena[contador]==infoluceslab[contador]) {
        flag1=true;
         if (flag1==true){
            comprobarluceslab=comprobarluceslab+1;
           }
      }
      if (comprobarluceslab==12){
          Serial1.println(comprobarluceslab);
          Serial1.println();
        }

    //infotemphumedad[]= 15,   comprobartemphum=0;
    if (cadena[contador]==infotemphumedad[contador]) {
        flag1=true;
         if (flag1==true){
            comprobartemphum=comprobartemphum+1;
           }
      }
      if (comprobartemphum==15){
          Serial1.println(comprobartemphum);
          Serial1.println();
        }


//FOCO #1
    if (cadena[contador]==focounoON[contador]) {
        flag1=true;
         if (flag1==true){
            comprobarFOCO1_ON=comprobarFOCO1_ON+1;
           }
    }
      if (comprobarFOCO1_ON==15){
          Serial1.println(comprobarFOCO1_ON);
          Serial1.println();
        }

     if (cadena[contador]==focounoOFF[contador]) {
        flag1=true;
         if (flag1==true){
            comprobarFOCO1_OFF=comprobarFOCO1_OFF+1;
           }
    }
    if (comprobarFOCO1_OFF==14){
          Serial1.println(comprobarFOCO1_OFF);
          Serial1.println();
        }

//FOCO #2
    if (cadena[contador]==focodosON[contador]) {
        flag1=true;
         if (flag1==true){
            comprobarFOCO2_ON=comprobarFOCO2_ON+1;
           }
    }
      if (comprobarFOCO2_ON==15){
          Serial1.println(comprobarFOCO2_ON);
          Serial1.println();
        }

    if (cadena[contador]==focodosOFF[contador]) {
    flag1=true;
     if (flag1==true){
        comprobarFOCO2_OFF=comprobarFOCO2_OFF+1;
       }
    }
    if (comprobarFOCO2_OFF==14){
          Serial1.println(comprobarFOCO2_OFF);
          Serial1.println();
        }

//FOCO #3
   if (cadena[contador]==focotresON[contador]) {
        flag1=true;
         if (flag1==true){
            comprobarFOCO3_ON=comprobarFOCO3_ON+1;
           }
    }
      if (comprobarFOCO3_ON==16){
          Serial1.println(comprobarFOCO3_ON);
          Serial1.println();
        }

    if (cadena[contador]==focotresOFF[contador]) {
    flag1=true;
     if (flag1==true){
        comprobarFOCO3_OFF=comprobarFOCO3_OFF+1;
       }
    }
    if (comprobarFOCO3_OFF==20){
          Serial1.println(comprobarFOCO3_OFF);
          Serial1.println();
        }

//FOCO #4
   if (cadena[contador]==fococuatroON[contador]) {
        flag1=true;
         if (flag1==true){
            comprobarFOCO4_ON=comprobarFOCO4_ON+1;
           }
    }
      if (comprobarFOCO4_ON==18){
          Serial1.println(comprobarFOCO4_ON);
          Serial1.println();
        }

    if (cadena[contador]==fococuatroOFF[contador]) {
    flag1=true;
     if (flag1==true){
        comprobarFOCO4_OFF=comprobarFOCO4_OFF+1;
       }
    }
    if (comprobarFOCO4_OFF==17){
          Serial1.println(comprobarFOCO4_OFF);
          Serial1.println();
        }

//FOCO #5
   if (cadena[contador]==fococincoON[contador]) {
        flag1=true;
         if (flag1==true){
            comprobarFOCO5_ON=comprobarFOCO5_ON+1;
           }
    }
      if (comprobarFOCO5_ON==17){
          Serial1.println(comprobarFOCO5_ON);
          Serial1.println();
        }

    if (cadena[contador]==fococincoOFF[contador]) {
    flag1=true;
     if (flag1==true){
        comprobarFOCO5_OFF=comprobarFOCO5_OFF+1;
       }
    }
    if (comprobarFOCO5_OFF==16){
          Serial1.println(comprobarFOCO5_OFF);
          Serial1.println();
        }

//FOCO #6
   if (cadena[contador]==focoseisON[contador]) {
        flag1=true;
         if (flag1==true){
            comprobarFOCO6_ON=comprobarFOCO6_ON+1;
           }
    }
      if (comprobarFOCO6_ON==16){
          Serial1.println(comprobarFOCO6_ON);
          Serial1.println();
        }

    if (cadena[contador]==focoseisOFF[contador]) {
    flag1=true;
     if (flag1==true){
        comprobarFOCO6_OFF=comprobarFOCO6_OFF+1;
       }
    }
    if (comprobarFOCO6_OFF==15){
          Serial1.println(comprobarFOCO6_OFF);
          Serial1.println();
        }

//FOCO #7
   if (cadena[contador]==focosieteON[contador]) {
        flag1=true;
         if (flag1==true){
            comprobarFOCO7_ON=comprobarFOCO7_ON+1;
           }
    }
      if (comprobarFOCO7_ON==17){
          Serial1.println(comprobarFOCO7_ON);
          Serial1.println();
        }

    if (cadena[contador]==focosieteOFF[contador]) {
    flag1=true;
     if (flag1==true){
        comprobarFOCO7_OFF=comprobarFOCO7_OFF+1;
       }
    }
    if (comprobarFOCO7_OFF==16){
          Serial1.println(comprobarFOCO7_OFF);
          Serial1.println();
        }

//FOCO #8
   if (cadena[contador]==focoochoON[contador]) {
        flag1=true;
         if (flag1==true){
            comprobarFOCO8_ON=comprobarFOCO8_ON+1;
           }
    }
      if (comprobarFOCO8_ON==16){
          Serial1.println(comprobarFOCO8_ON);
          Serial1.println();
        }

    if (cadena[contador]==focoochoOFF[contador]) {
    flag1=true;
     if (flag1==true){
        comprobarFOCO8_OFF=comprobarFOCO8_OFF+1;
       }
    }
    if (comprobarFOCO8_OFF==15){
          Serial1.println(comprobarFOCO8_OFF);
          Serial1.println();
        }


//FOCO ALL
   if (cadena[contador]==allfocosBTON[contador]) {
        flag1=true;
         if (flag1==true){
            comprobarALLBTON=comprobarALLBTON+1;
           }
    }
      if (comprobarALLBTON==6){
          Serial1.println(comprobarALLBTON);
          Serial1.println();
        }

    if (cadena[contador]==allfocosBTOFF[contador]) {
    flag1=true;
     if (flag1==true){
        comprobarALLBTOFF=comprobarALLBTOFF+1;
       }
    }
    if (comprobarALLBTOFF==5){
          Serial1.println(comprobarALLBTOFF);
          Serial1.println();
        }
}

void estadoInicialRelay(){
     print_port(foco0,estadofoco1);
     print_port(foco1,estadofoco2);
     print_port(foco2,estadofoco3);
     print_port(foco3,estadofoco4);
     print_port(foco4,estadofoco5);
     print_port(foco5,estadofoco6);
     print_port(foco6,estadofoco7);
     print_port(foco7,estadofoco8);
     /*
     print_port(foco0,HIGH);
     print_port(foco1,HIGH);
     print_port(foco2,HIGH);
     print_port(foco3,HIGH);
     print_port(foco4,HIGH);
     print_port(foco5,HIGH);
     print_port(foco6,HIGH);
     print_port(foco7,HIGH);*/
}

void resetCounter(){
  comprobarFOCO1_ON=0;comprobarFOCO1_OFF=0;
  comprobarFOCO2_ON=0;comprobarFOCO2_OFF=0;
  comprobarFOCO3_ON=0;comprobarFOCO3_OFF=0;
  comprobarFOCO4_ON=0;comprobarFOCO4_OFF=0;
  comprobarFOCO5_ON=0;comprobarFOCO5_OFF=0;
  comprobarFOCO6_ON=0;comprobarFOCO6_OFF=0;
  comprobarFOCO7_ON=0;comprobarFOCO7_OFF=0;
  comprobarFOCO8_ON=0;comprobarFOCO8_OFF=0;
  comprobarALLBTON=0;comprobarALLBTOFF=0;
  comprobarCallme=0, comprobarluceslab=0, comprobartemphum=0;
  comprobarinfotodosensores=0;
}
/*FIN DEL BLOQUE 4 - BT*/

/*
 * INICIO:MÉTODO PARA ESTAR CAMBIANDO ESTADOS DE LAS LUCES POR LA OPCIÓN COMBINADAS U ON Y OFF MULTIPLE.
 */

  void cambioTodasLuces(){
      estadofoco1 = ! estadofoco1 ; // cambiamos el estado del Luz1
      estadofoco2 = ! estadofoco2 ; // cambiamos el estado del Luz2
      estadofoco3 = ! estadofoco3 ; // cambiamos el estado del Luz3
      estadofoco4 = ! estadofoco4 ; // cambiamos el estado del Luz4
      estadofoco5 = ! estadofoco5 ; // cambiamos el estado del Luz5
      estadofoco6 = ! estadofoco6 ; // cambiamos el estado del Luz6
      estadofoco7 = ! estadofoco7 ; // cambiamos el estado del Luz7
      estadofoco8 = ! estadofoco8 ; // cambiamos el estado del Luz8
       print_port(foco0, estadofoco1) ;
        print_port(foco1, estadofoco2) ;
         print_port(foco2, estadofoco3) ;
          print_port(foco3, estadofoco4) ;
           print_port(foco4, estadofoco5) ;
            print_port(foco5, estadofoco6) ;
             print_port(foco6, estadofoco7) ;
              print_port(foco7, estadofoco8) ;

       //Serial1.println("\nEstado de todas las luces ha sido cambiado correctamente.");
       //Serial1.println("\nSMS: CON CONFIRMACION.");
  }

 /*
  * FIN.
  */

  /*void buscared(){
    if (enviarAT("AT+CREG?", "+CREG: 0,1", 1000) != 1) //Comprueba la conexion a la red
    reiniciar();
    iniciar();
   }*/


  void identifica_SMS(){
  int resetArduino = mensaje.indexOf("utla0reset0");
  int luz1 = mensaje.indexOf("Manuel luz1"); //Sin confirmación
  int luz1SI = mensaje.indexOf("Maria uno"); //Con confirmación
  int luz2 = mensaje.indexOf("Jesus luz2");
  int luz2SI = mensaje.indexOf("Brendali dos");
  int luz3 = mensaje.indexOf("Daniel luz3");
  int luz3SI = mensaje.indexOf("Beatriz tres");
  int luz4 = mensaje.indexOf("Alejandra lu4");
  int luz4SI = mensaje.indexOf("Jhoseline cut");
  int luz5 = mensaje.indexOf("Jose luz5");
  int luz5SI = mensaje.indexOf("Oscar cinco");
  int luz6 = mensaje.indexOf("Carlos luz6");
  int luz6SI = mensaje.indexOf("Douglas seis");
  int luz7 = mensaje.indexOf("Juan luz7");
  int luz7SI = mensaje.indexOf("Antonio siete");
  int luz8 = mensaje.indexOf("Beatriz luz8");
  int luz8SI = mensaje.indexOf("Olga ocho");
  int luz9 = mensaje.indexOf("info temphum");
  int luz0 = mensaje.indexOf("info luceslab");
  int llamame = mensaje.indexOf("llamame OK");
  int alluces = mensaje.indexOf("Cambiar");     //Sin confirmación
  int allucesOK = mensaje.indexOf("Chovi OK");  //Con confirmación

  //ULTIMA VARIABLE SMS CONTROL
  int comoestas = mensaje.indexOf("comoestas tu");

  /*
   * *****************************************************************************************************************************************
   * INICIO: A continuación meto el código necesario para el encendido de las luminarias en forma combinada u ON y OFF multiple por mensaje  *
   * *****************************************************************************************************************************************
   */
   //                                                            luz8    luz7   luz6   luz5   luz4   luz3  luz2  luz1
    int luz_0=mensaje.indexOf("utla00000000");     //Dec: 1 = Bin 0       0       0      0      0      0     0    0
    int luz_1=mensaje.indexOf("utla00000001");     //Dec: 1 = Bin 0       0       0      0      0      0     0    1
    int luz_2=mensaje.indexOf("utla00000010");     //Dec: 2 = Bin 0       0       0      0      0      0     1    0
    int luz_12=mensaje.indexOf("utla00000011");    //Dec: 3 = Bin 0       0       0      0      0      0     1    1
    int luz_3=mensaje.indexOf("utla00000100");     //Dec: 4 = Bin 00000100
    int luz_13=mensaje.indexOf("utla00000101");    //Dec: 5 = Bin 00000101
    int luz_23=mensaje.indexOf("utla00000110");    //Dec: 6 = Bin 00000110
    int luz_123=mensaje.indexOf("utla00000111");   //Dec: 7 = Bin 00000111
    int luz_4=mensaje.indexOf("utla00001000");     //Dec: 8 = Bin 00001000
    int luz_14=mensaje.indexOf("utla00001001");    //Dec: 9 = Bin 00001001
    int luz_24=mensaje.indexOf("utla00001010");    //Dec: 10= Bin 00001010

    int luz_124=mensaje.indexOf("utla00001011");     //Dec: 11= Bin 00001011
    int luz_34=mensaje.indexOf("utla00001100");      //Dec: 12= Bin 00001100
    int luz_134=mensaje.indexOf("utla00001101");     //Dec: 13= Bin 00001101
    int luz_234=mensaje.indexOf("utla00001110");     //Dec: 14= Bin 00001110
    int luz_1234=mensaje.indexOf("utla00001111");    //Dec: 15= Bin 00001111
    int luz_5=mensaje.indexOf("utla00010000");       //Dec: 16= Bin 00010000
    int luz_15=mensaje.indexOf("utla00010001");      //Dec: 17= Bin 00010001
    int luz_25=mensaje.indexOf("utla00010010");      //Dec: 18= Bin 00010010
    int luz_125=mensaje.indexOf("utla00010011");     //Dec: 19= Bin 00010011
    int luz_35=mensaje.indexOf("utla00010100");      //Dec: 20= Bin 00010100

    int luz_135=mensaje.indexOf("utla00010101");      //Dec: 21= Bin 00010101
    int luz_235=mensaje.indexOf("utla00010110");      //Dec: 22= Bin 00010110
    int luz_1235=mensaje.indexOf("utla00010111");     //Dec: 23= Bin 00010111
    int luz_45=mensaje.indexOf("utla00011000");       //Dec: 24= Bin 00011000
    int luz_145=mensaje.indexOf("utla00011001");      //Dec: 25= Bin 00011001
    int luz_245=mensaje.indexOf("utla00011010");      //Dec: 26= Bin 00011010
    int luz_1245=mensaje.indexOf("utla00011011");     //Dec: 27= Bin 00011011
    int luz_345=mensaje.indexOf("utla00011100");      //Dec: 28= Bin 00011100
    int luz_1345=mensaje.indexOf("utla00011101");     //Dec: 29= Bin 00011101
    int luz_2345=mensaje.indexOf("utla00011110");     //Dec: 30= Bin 00011110

    int luz_12345=mensaje.indexOf("utla00011111");     //Dec: 31= Bin 00011111
     int luz_6=mensaje.indexOf("utla00100000");        //Dec: 32= Bin 00100000
    int luz_16=mensaje.indexOf("utla00100001");        //Dec: 33= Bin 00100001
    int luz_26=mensaje.indexOf("utla00100010");        //Dec: 34= Bin 00100010
    int luz_126=mensaje.indexOf("utla00100011");       //Dec: 35= Bin 00100011
    int luz_36=mensaje.indexOf("utla00100100");        //Dec: 36= Bin 00100100
   int luz_136=mensaje.indexOf("utla00100101");        //Dec: 37= Bin 00100101
   int luz_236=mensaje.indexOf("utla00100110");        //Dec: 38= Bin 00100110
  int luz_1236=mensaje.indexOf("utla00100111");        //Dec: 39= Bin 00100111
    int luz_46=mensaje.indexOf("utla00101000");        //Dec: 40= Bin 00101000

 int luz_146=mensaje.indexOf("utla00101001");         //Dec: 41= Bin 00101001
  int luz_246=mensaje.indexOf("utla00101010");        //Dec: 42= Bin 00101010
 int luz_1246=mensaje.indexOf("utla00101011");        //Dec: 43= Bin 00101011
  int luz_346=mensaje.indexOf("utla00101100");        //Dec: 44= Bin 00101100
 int luz_1346=mensaje.indexOf("utla00101101");        //Dec: 45= Bin 00101101
 int luz_2346=mensaje.indexOf("utla00101110");        //Dec: 46= Bin 00101110
int luz_12346=mensaje.indexOf("utla00101111");        //Dec: 47= Bin 00101111
   int luz_56=mensaje.indexOf("utla00110000");        //Dec: 48= Bin 00110000
  int luz_156=mensaje.indexOf("utla00110001");        //Dec: 49= Bin 00110001
  int luz_256=mensaje.indexOf("utla00110010");        //Dec: 50= Bin 00110010

 int luz_1256=mensaje.indexOf("utla00110011");        //Dec: 51= Bin 00110011
  int luz_356=mensaje.indexOf("utla00110100");        //Dec: 52= Bin 00110100
 int luz_1356=mensaje.indexOf("utla00110101");        //Dec: 53= Bin 00110101
 int luz_2356=mensaje.indexOf("utla00110110");        //Dec: 54= Bin 00110110
int luz_12356=mensaje.indexOf("utla00110111");        //Dec: 55= Bin 00110111
  int luz_456=mensaje.indexOf("utla00111000");        //Dec: 56= Bin 00111000
 int luz_1456=mensaje.indexOf("utla00111001");        //Dec: 57= Bin 00111001
 int luz_2456=mensaje.indexOf("utla00111010");        //Dec: 58= Bin 00111010
int luz_12456=mensaje.indexOf("utla00111011");        //Dec: 59= Bin 00111011
 int luz_3456=mensaje.indexOf("utla00111100");        //Dec: 60= Bin 00111100

int luz_13456=mensaje.indexOf("utla00111101");        //Dec: 61= Bin 00111101
int luz_23456=mensaje.indexOf("utla00111110");        //Dec: 62= Bin 00111110
int luz_123456=mensaje.indexOf("utla00111111");       //Dec: 63= Bin 00111111
    int luz_7=mensaje.indexOf("utla01000000");        //Dec: 64= Bin 01000000
   int luz_17=mensaje.indexOf("utla01000001");        //Dec: 65= Bin 01000001
   int luz_27=mensaje.indexOf("utla01000010");        //Dec: 66= Bin 01000010
  int luz_127=mensaje.indexOf("utla01000011");        //Dec: 67= Bin 01000011
   int luz_37=mensaje.indexOf("utla01000100");        //Dec: 68= Bin 01000100
  int luz_137=mensaje.indexOf("utla01000101");        //Dec: 69= Bin 01000101
  int luz_237=mensaje.indexOf("utla01000110");        //Dec: 70= Bin 01000110

  int luz_1237=mensaje.indexOf("utla01000111");        //Dec: 71= Bin 01000111
    int luz_47=mensaje.indexOf("utla01001000");        //Dec: 72= Bin 01001000
   int luz_147=mensaje.indexOf("utla01001001");        //Dec: 73= Bin 01001001
   int luz_247=mensaje.indexOf("utla01001010");        //Dec: 74= Bin 01001010
  int luz_1247=mensaje.indexOf("utla01001011");        //Dec: 75= Bin 01001011
   int luz_347=mensaje.indexOf("utla01001100");        //Dec: 76= Bin 01001100
  int luz_1347=mensaje.indexOf("utla01001101");        //Dec: 77= Bin 01001101
  int luz_2347=mensaje.indexOf("utla01001110");        //Dec: 78= Bin 01001110
  int luz_12347=mensaje.indexOf("utla01001111");        //Dec: 79= Bin 01001111
    int luz_57=mensaje.indexOf("utla01010000");        //Dec: 80= Bin 01010000

    int luz_157=mensaje.indexOf("utla01010001");        //Dec: 81= Bin 01010001
    int luz_257=mensaje.indexOf("utla01010010");        //Dec: 82= Bin 01010010
   int luz_1257=mensaje.indexOf("utla01010011");        //Dec: 83= Bin 01010011
    int luz_357=mensaje.indexOf("utla01010100");        //Dec: 84= Bin 01010100
   int luz_1357=mensaje.indexOf("utla01010101");        //Dec: 85= Bin 01010101
   int luz_2357=mensaje.indexOf("utla01010110");        //Dec: 86= Bin 01010110
  int luz_12357=mensaje.indexOf("utla01010111");        //Dec: 87= Bin 01010111
    int luz_457=mensaje.indexOf("utla01011000");        //Dec: 88= Bin 01011000
   int luz_1457=mensaje.indexOf("utla01011001");        //Dec: 89= Bin 01011001
   int luz_2457=mensaje.indexOf("utla01011010");        //Dec: 90= Bin 01011010

   int luz_12457=mensaje.indexOf("utla01011011");        //Dec: 91= Bin 01011011
    int luz_3457=mensaje.indexOf("utla01011100");        //Dec: 92= Bin 01011100
   int luz_13457=mensaje.indexOf("utla01011101");        //Dec: 93= Bin 01011101
   int luz_23457=mensaje.indexOf("utla01011110");        //Dec: 94= Bin 01011110
  int luz_123457=mensaje.indexOf("utla01011111");        //Dec: 95= Bin 01011111
      int luz_67=mensaje.indexOf("utla01100000");        //Dec: 96= Bin 01100000
     int luz_167=mensaje.indexOf("utla01100001");        //Dec: 97= Bin 01100001
     int luz_267=mensaje.indexOf("utla01100010");        //Dec: 98= Bin 01100010
    int luz_1267=mensaje.indexOf("utla01100011");        //Dec: 99= Bin 01100011
     int luz_367=mensaje.indexOf("utla01100100");        //Dec:100= Bin 01100100

     int luz_1367=mensaje.indexOf("utla01100101");        //Dec:101= Bin 01100101
     int luz_2367=mensaje.indexOf("utla01100110");        //Dec:102= Bin 01100110
    int luz_12367=mensaje.indexOf("utla01100111");        //Dec:103= Bin 01100111
      int luz_467=mensaje.indexOf("utla01101000");        //Dec:104= Bin 01101000
     int luz_1467=mensaje.indexOf("utla01101001");        //Dec:105= Bin 01101001
     int luz_2467=mensaje.indexOf("utla01101010");        //Dec:106= Bin 01101010
    int luz_12467=mensaje.indexOf("utla01101011");        //Dec:107= Bin 01101011
     int luz_3467=mensaje.indexOf("utla01101100");        //Dec:108= Bin 01101100
    int luz_13467=mensaje.indexOf("utla01101101");        //Dec:109= Bin 01101101
    int luz_23467=mensaje.indexOf("utla01101110");        //Dec:110= Bin 01101110

  int luz_123467=mensaje.indexOf("utla01101111");        //Dec:111= Bin 01101111
     int luz_567=mensaje.indexOf("utla01110000");        //Dec:112= Bin 01110000
    int luz_1567=mensaje.indexOf("utla01110001");        //Dec:113= Bin 01110001
    int luz_2567=mensaje.indexOf("utla01110010");        //Dec:114= Bin 01110010
   int luz_12567=mensaje.indexOf("utla01110011");        //Dec:115= Bin 01110011
    int luz_3567=mensaje.indexOf("utla01110100");        //Dec:116= Bin 01110100
   int luz_13567=mensaje.indexOf("utla01110101");        //Dec:117= Bin 01110101
   int luz_23567=mensaje.indexOf("utla01110110");        //Dec:118= Bin 01110110
  int luz_123567=mensaje.indexOf("utla01110111");        //Dec:119= Bin 01110111
    int luz_4567=mensaje.indexOf("utla01111000");        //Dec:120= Bin 01111000

  int luz_14567=mensaje.indexOf("utla01111001");        //Dec:121= Bin 01111001
  int luz_24567=mensaje.indexOf("utla01111010");        //Dec:122= Bin 01111010
 int luz_124567=mensaje.indexOf("utla01111011");        //Dec:123= Bin 01111011
  int luz_34567=mensaje.indexOf("utla01111100");        //Dec:124= Bin 01111100
 int luz_134567=mensaje.indexOf("utla01111101");        //Dec:125= Bin 01111101
 int luz_234567=mensaje.indexOf("utla01111110");        //Dec:126= Bin 01111110
int luz_1234567=mensaje.indexOf("utla01111111");        //Dec:127= Bin 01111111
      int luz_8=mensaje.indexOf("utla10000000");        //Dec:128= Bin 10000000
     int luz_18=mensaje.indexOf("utla10000001");        //Dec:129= Bin 10000001
     int luz_28=mensaje.indexOf("utla10000010");        //Dec:130= Bin 10000010

      int luz_128=mensaje.indexOf("utla10000011");        //Dec:131= Bin 10000011
       int luz_38=mensaje.indexOf("utla10000100");        //Dec:132= Bin 10000100
      int luz_138=mensaje.indexOf("utla10000101");        //Dec:133= Bin 10000101
      int luz_238=mensaje.indexOf("utla10000110");        //Dec:134= Bin 10000110
     int luz_1238=mensaje.indexOf("utla10000111");        //Dec:135= Bin 10000111
       int luz_48=mensaje.indexOf("utla10001000");        //Dec:136= Bin 10001000
      int luz_148=mensaje.indexOf("utla10001001");        //Dec:137= Bin 10001001
      int luz_248=mensaje.indexOf("utla10001010");        //Dec:138= Bin 10001010
     int luz_1248=mensaje.indexOf("utla10001011");        //Dec:139= Bin 10001011
      int luz_348=mensaje.indexOf("utla10001100");        //Dec:140= Bin 10001100

     int luz_1348=mensaje.indexOf("utla10001101");        //Dec:141= Bin 10001101
     int luz_2348=mensaje.indexOf("utla10001110");        //Dec:142= Bin 10001110
    int luz_12348=mensaje.indexOf("utla10001111");        //Dec:143= Bin 10001111
       int luz_58=mensaje.indexOf("utla10010000");        //Dec:144= Bin 10010000
      int luz_158=mensaje.indexOf("utla10010001");        //Dec:145= Bin 10010001
      int luz_258=mensaje.indexOf("utla10010010");        //Dec:146= Bin 10010010
     int luz_1258=mensaje.indexOf("utla10010011");        //Dec:147= Bin 10010011
      int luz_358=mensaje.indexOf("utla10010100");        //Dec:148= Bin 10010100
     int luz_1358=mensaje.indexOf("utla10010101");        //Dec:149= Bin 10010101
     int luz_2358=mensaje.indexOf("utla10010110");        //Dec:150= Bin 10010110

    int luz_12358=mensaje.indexOf("utla10010111");        //Dec:151= Bin 10010111
      int luz_458=mensaje.indexOf("utla10011000");        //Dec:152= Bin 10011000
     int luz_1458=mensaje.indexOf("utla10011001");        //Dec:153= Bin 10011001
     int luz_2458=mensaje.indexOf("utla10011010");        //Dec:154= Bin 10011010
    int luz_12458=mensaje.indexOf("utla10011011");        //Dec:155= Bin 10011011
     int luz_3458=mensaje.indexOf("utla10011100");        //Dec:156= Bin 10011100
    int luz_13458=mensaje.indexOf("utla10011101");        //Dec:157= Bin 10011101
    int luz_23458=mensaje.indexOf("utla10011110");        //Dec:158= Bin 10011110
   int luz_123458=mensaje.indexOf("utla10011111");        //Dec:159= Bin 10011111
       int luz_68=mensaje.indexOf("utla10100000");        //Dec:160= Bin 10100000

      int luz_168=mensaje.indexOf("utla10100001");        //Dec:161= Bin 10100001
      int luz_268=mensaje.indexOf("utla10100010");        //Dec:162= Bin 10100010
     int luz_1268=mensaje.indexOf("utla10100011");        //Dec:163= Bin 10100011
      int luz_368=mensaje.indexOf("utla10100100");        //Dec:164= Bin 10100100
     int luz_1368=mensaje.indexOf("utla10100101");        //Dec:165= Bin 10100101
     int luz_2368=mensaje.indexOf("utla10100110");        //Dec:166= Bin 10100110
    int luz_12368=mensaje.indexOf("utla10100111");        //Dec:167= Bin 10100111
      int luz_468=mensaje.indexOf("utla10101000");        //Dec:168= Bin 10101000
     int luz_1468=mensaje.indexOf("utla10101001");        //Dec:169= Bin 10101001
     int luz_2468=mensaje.indexOf("utla10101010");        //Dec:170= Bin 10101010

    int luz_12468=mensaje.indexOf("utla10101011");        //Dec:171= Bin 10101011
     int luz_3468=mensaje.indexOf("utla10101100");        //Dec:172= Bin 10101100
    int luz_13468=mensaje.indexOf("utla10101101");        //Dec:173= Bin 10101101
    int luz_23468=mensaje.indexOf("utla10101110");        //Dec:174= Bin 10101110
   int luz_123468=mensaje.indexOf("utla10101111");        //Dec:175= Bin 10101111
      int luz_568=mensaje.indexOf("utla10110000");        //Dec:176= Bin 10110000
     int luz_1568=mensaje.indexOf("utla10110001");        //Dec:177= Bin 10110001
     int luz_2568=mensaje.indexOf("utla10110010");        //Dec:178= Bin 10110010
    int luz_12568=mensaje.indexOf("utla10110011");        //Dec:179= Bin 10110011
     int luz_3568=mensaje.indexOf("utla10110100");        //Dec:180= Bin 10110100

    int luz_13568=mensaje.indexOf("utla10110101");        //Dec:181= Bin 10110101
    int luz_23568=mensaje.indexOf("utla10110110");        //Dec:182= Bin 10110110
   int luz_123568=mensaje.indexOf("utla10110111");        //Dec:183= Bin 10110111
     int luz_4568=mensaje.indexOf("utla10111000");        //Dec:184= Bin 10111000
    int luz_14568=mensaje.indexOf("utla10111001");        //Dec:185= Bin 10111001
    int luz_24568=mensaje.indexOf("utla10111010");        //Dec:186= Bin 10111010
    int luz_124568=mensaje.indexOf("utla10111011");       //Dec:187= Bin 10111011
     int luz_34568=mensaje.indexOf("utla10111100");       //Dec:188= Bin 10111100
    int luz_134568=mensaje.indexOf("utla10111101");       //Dec:189= Bin 10111101
    int luz_234568=mensaje.indexOf("utla10111110");       //Dec:190= Bin 10111110

 int luz_1234568=mensaje.indexOf("utla10111111");        //Dec:191= Bin 10111111
      int luz_78=mensaje.indexOf("utla11000000");        //Dec:192= Bin 11000000
     int luz_178=mensaje.indexOf("utla11000001");        //Dec:193= Bin 11000001
     int luz_278=mensaje.indexOf("utla11000010");        //Dec:194= Bin 11000010
    int luz_1278=mensaje.indexOf("utla11000011");        //Dec:195= Bin 11000011
      int luz_378=mensaje.indexOf("utla11000100");       //Dec:196= Bin 11000100
   int luz_1378=mensaje.indexOf("utla11000101");         //Dec:197= Bin 11000101
   int luz_2378=mensaje.indexOf("utla11000110");         //Dec:198= Bin 11000110
  int luz_12378=mensaje.indexOf("utla11000111");         //Dec:199= Bin 11000111
    int luz_478=mensaje.indexOf("utla11001000");         //Dec:200= Bin 11001000

    int luz_1478=mensaje.indexOf("utla11001001");        //Dec:201= Bin 11001001
    int luz_2478=mensaje.indexOf("utla11001010");        //Dec:202= Bin 11001010
   int luz_12478=mensaje.indexOf("utla11001011");        //Dec:203= Bin 11001011
    int luz_3478=mensaje.indexOf("utla11001100");        //Dec:204= Bin 11001100
   int luz_13478=mensaje.indexOf("utla11001101");        //Dec:205= Bin 11001101
   int luz_23478=mensaje.indexOf("utla11001110");        //Dec:206= Bin 11001110
  int luz_123478=mensaje.indexOf("utla11001111");        //Dec:207= Bin 11001111
     int luz_578=mensaje.indexOf("utla11010000");        //Dec:208= Bin 11010000
    int luz_1578=mensaje.indexOf("utla11010001");        //Dec:209= Bin 11010001
    int luz_2578=mensaje.indexOf("utla11010010");        //Dec:210= Bin 11010010

   int luz_12578=mensaje.indexOf("utla11010011");        //Dec:211= Bin 11010011
    int luz_3578=mensaje.indexOf("utla11010100");        //Dec:212= Bin 11010100
   int luz_13578=mensaje.indexOf("utla11010101");        //Dec:213= Bin 11010101
   int luz_23578=mensaje.indexOf("utla11010110");        //Dec:214= Bin 11010110
  int luz_123578=mensaje.indexOf("utla11010111");        //Dec:215= Bin 11010111
    int luz_4578=mensaje.indexOf("utla11011000");        //Dec:216= Bin 11011000
   int luz_14578=mensaje.indexOf("utla11011001");        //Dec:217= Bin 11011001
   int luz_24578=mensaje.indexOf("utla11011010");        //Dec:218= Bin 11011010
  int luz_124578=mensaje.indexOf("utla11011011");        //Dec:219= Bin 11011011
   int luz_34578=mensaje.indexOf("utla11011100");        //Dec:220= Bin 11011100

  int luz_134578=mensaje.indexOf("utla11011101");        //Dec:221= Bin 11011101
   int luz_234578=mensaje.indexOf("utla11011110");       //Dec:222= Bin 11011110
  int luz_1234578=mensaje.indexOf("utla11011111");       //Dec:223= Bin 11011111
      int luz_678=mensaje.indexOf("utla11100000");       //Dec:224= Bin 11100000
     int luz_1678=mensaje.indexOf("utla11100001");       //Dec:225= Bin 11100001
     int luz_2678=mensaje.indexOf("utla11100010");       //Dec:226= Bin 11100010
    int luz_12678=mensaje.indexOf("utla11100011");       //Dec:227= Bin 11100011
     int luz_3678=mensaje.indexOf("utla11100100");       //Dec:228= Bin 11100100
    int luz_13678=mensaje.indexOf("utla11100101");       //Dec:229= Bin 11100101
    int luz_23678=mensaje.indexOf("utla11100110");       //Dec:230= Bin 11100110

   int luz_123678=mensaje.indexOf("utla11100111");        //Dec:231= Bin 11100111
     int luz_4678=mensaje.indexOf("utla11101000");        //Dec:232= Bin 11101000
    int luz_14678=mensaje.indexOf("utla11101001");        //Dec:233= Bin 11101001
    int luz_24678=mensaje.indexOf("utla11101010");        //Dec:234= Bin 11101010
   int luz_124678=mensaje.indexOf("utla11101011");        //Dec:235= Bin 11101011
    int luz_34678=mensaje.indexOf("utla11101100");        //Dec:236= Bin 11101100
   int luz_134678=mensaje.indexOf("utla11101101");        //Dec:237= Bin 11101101
   int luz_234678=mensaje.indexOf("utla11101110");        //Dec:238= Bin 11101110
  int luz_1234678=mensaje.indexOf("utla11101111");        //Dec:239= Bin 11101111
     int luz_5678=mensaje.indexOf("utla11110000");        //Dec:240= Bin 11110000

    int luz_15678=mensaje.indexOf("utla11110001");        //Dec:241= Bin 11110001
    int luz_25678=mensaje.indexOf("utla11110010");        //Dec:242= Bin 11110010
   int luz_125678=mensaje.indexOf("utla11110011");        //Dec:243= Bin 11110011
    int luz_35678=mensaje.indexOf("utla11110100");        //Dec:244= Bin 11110100
   int luz_135678=mensaje.indexOf("utla11110101");        //Dec:245= Bin 11110101
   int luz_235678=mensaje.indexOf("utla11110110");        //Dec:246= Bin 11110110
  int luz_1235678=mensaje.indexOf("utla11110111");        //Dec:247= Bin 11110111
    int luz_45678=mensaje.indexOf("utla11111000");        //Dec:248= Bin 11111000
   int luz_145678=mensaje.indexOf("utla11111001");        //Dec:249= Bin 11111001
   int luz_245678=mensaje.indexOf("utla11111010");        //Dec:250= Bin 11111010

  int luz_1245678=mensaje.indexOf("utla11111011");        //Dec:251= Bin 11111011
   int luz_345678=mensaje.indexOf("utla11111100");        //Dec:252= Bin 11111100
  int luz_1345678=mensaje.indexOf("utla11111101");        //Dec:253= Bin 11111101
  int luz_2345678=mensaje.indexOf("utla11111110");        //Dec:254= Bin 11111110
 int luz_12345678=mensaje.indexOf("utla11111111");        //Dec:255= Bin 11111111

 //FUNCIÓN PARA RESET DE ARDUINO.
 //resetArduino = "utla0reset0"
 if(resetArduino >= 0){
    for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        reiniciar();
        delay(500);
        reset();
      }
    }
   mensaje = "" ;                      //Bórralo para la próxima vez
 }

 if(comoestas >=0) {
  for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        mensaje_universal(agenda[i], sms1);
      }
    }
   mensaje = "" ;                      //Bórralo para la próxima vez
 }

  //FUNCIONES PARA SOLICITAR INFORMACIÓN AL CIRCUITO INSTALADO EN EL LABORATORIO
  //DE ELECTRÓNICA.
  //Luz # 9: Get info de temperatura y humedad relativa.
  if (luz9 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        //printerFTDI("\nEnviando SMS: Contacto Encontrado en la posicion: \"+String(i+1)+\", \"+String(agenda[i])\"");
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nEstado de temperatura y humedad relativa.");
        mensaje_sms1(agenda[i]);         //Función para enviar SMS de respuesta a destinatario autorizado TEMP Y RH.
        //printerFTDI("SMS Enviado");
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                      //Bórralo para la próxima vez
  }

  //Luz # 0: Get info del estado de todas las luces en el laboratorio de la UTLA.
  if (luz0 >= 0){
    for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        //printerFTDI("\nEnviando SMS: Contacto Encontrado en la posicion: \"+String(i+1)+\", \"+String(agenda[i])\"");
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nEstado de luminarias en el laboratorio.");
        mensaje_sms2(agenda[i]);         //Función para enviar SMS de respuesta a destinatario autorizado.
        //printerFTDI("SMS Enviado.");
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                      //Bórralo para la próxima vez

  }


  //Llamada saliente:
  if (llamame >= 0){
    for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        //printerFTDI("\nLlamando: Contacto Encontrado en la posicion: \"+String(i+1)+\", \"+String(agenda[i])\"");
        //Serial.println("\nLlamando a: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        llamar(agenda[i]);         //Función para enviar SMS de respuesta a destinatario autorizado.
        //printerFTDI("Fin de la llamada");
        //Serial.println("\nFin de la llamada.");
      }
    }
    mensaje = "" ;                      //Bórralo para la próxima vez
  }

  /******************************************************************************************/
  /******************************************************************************************/

  //Función para cambiar estado de todas las luces. Desde la opción luces combinadas.
  //CAMBIA EL ESTADO DE TODAS LAS LUCES. CON CONFIRMACIÓN
  if(luz_0 >= 0){
    for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        cambioTodasLuces();
        //printerFTDI("\nEnviando SMS: Contacto Encontrado en la posicion: \"+String(i+1)+\", \"+String(agenda[i])\"");
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado de todas las luminarias en el laboratorio.");
        mensaje_sms2(agenda[i]);         //Función para enviar SMS de respuesta a destinatario autorizado.
        //printerFTDI("SMS Enviado.");
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                      //Bórralo para la próxima vez
 }

  //CAMBIA EL ESTADO DE TODAS LAS LUCES. SIN CONFIRMACIÓN
  if (alluces >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        cambioTodasLuces();
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado de todas las luminarias en el laboratorio.\nSIN CONFIRMACION.");
        //mensaje_sms2(agenda[i]);         //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
        status_luces_BT();
        Serial1.print("Sin confirmacion");
        Serial1.print("Waiting more...");
      }
    }
    mensaje = "" ;                      //Bórralo para la próxima vez
  }

  //TODAS LAS LUCES ON Y OFF. CON CONFIRMACIÓN
  if (allucesOK >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        cambioTodasLuces();
        //printerFTDI("\nEnviando SMS: Contacto Encontrado en la posicion: \"+String(i+1)+\", \"+String(agenda[i])\"");
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado de todas las luminarias en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);         //Función para enviar SMS de respuesta a destinatario autorizado.
        //printerFTDI("SMS Enviado.");
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                       //Bórralo para la próxima vez
  }

  //Luz # 1. Sin confirmación.
  if (luz1 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco1=!estadofoco1;
        print_port(foco0, estadofoco1) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #1 en el laboratorio.\nSIN CONFIRMACION.");
        //mensaje_sms2(agenda[i]);         //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
        status_luces_BT();
        Serial1.print("Sin confirmacion");
        Serial1.print("Waiting more...");
      }
    }
    mensaje = "" ;                      //Bórralo para la próxima vez
  }

  //Luz # 1: Con confirmación
  if (luz1SI >= 0){
    for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco1=!estadofoco1;
        print_port(foco0, estadofoco1) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #1 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);         //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                      //Bórralo para la próxima vez
  }

  //Luz # 2. Sin confirmación
  if (luz2 >= 0){
    for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco2=!estadofoco2;
        print_port(foco1, estadofoco2) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #2 en el laboratorio.\nSIN CONFIRMACION.");
        //mensaje_sms2(agenda[i]);         //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
        status_luces_BT();
        Serial1.print("Sin confirmacion");
        Serial1.print("Waiting more...");
      }
    }
    mensaje = "" ;                      //Bórralo para la próxima vez
  }

//Luz # 2: Con confirmación
  if (luz2SI >= 0){
    for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco2=!estadofoco2;
        print_port(foco1, estadofoco2) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #2 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);         //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                       //Bórralo para la próxima vez
  }

  //Luz # 3. Sin confirmación
  if (luz3 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco3=!estadofoco3;
        print_port(foco2, estadofoco3) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nSIN CONFIRMACION.");
        //mensaje_sms2(agenda[i]);         //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
        status_luces_BT();
        Serial1.print("Sin confirmacion");
        Serial1.print("Waiting more...");
      }
    }
    mensaje = "" ;                         //Bórralo para la próxima vez
  }

  //Luz # 3: Con confirmación
  if (luz3SI >= 0){
    for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco3=!estadofoco3;
        print_port(foco2, estadofoco3) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);         //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");

      }
    }
    mensaje = "" ;                       //Bórralo para la próxima vez
  }

  //Luz # 4. Sin confirmación
  if (luz4 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco4=!estadofoco4;
        print_port(foco3, estadofoco4) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nSIN CONFIRMACION.");
        //mensaje_sms2(agenda[i]);         //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
        status_luces_BT();
        Serial1.print("Sin confirmacion");
        Serial1.print("Waiting more...");
      }
    }
    mensaje = "" ;                      //Bórralo para la próxima vez
  }

  //Luz # 4: Con confirmación
  if (luz4SI >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco4=!estadofoco4;
        print_port(foco3, estadofoco4) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);         //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                      //Bórralo para la próxima vez
  }

  //Luz # 5.Sin confirmación.
  if (luz5 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco5=!estadofoco5;
        print_port(foco4, estadofoco5) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nSIN CONFIRMACION.");
        //mensaje_sms2(agenda[i]);         //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
        status_luces_BT();
        Serial1.print("Sin confirmacion");
        Serial1.print("Waiting more...");
      }
    }
    mensaje = "" ;                      //Bórralo para la próxima vez
  }

   //Luz # 5. Con confirmación
  if (luz5SI >= 0){
    for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco5=!estadofoco5;
        print_port(foco4, estadofoco5) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);         //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                      //Bórralo para la próxima vez
  }

  //Luz # 6. Sin confirmación.
  if (luz6 >= 0){
    for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco6=!estadofoco6;
        print_port(foco5, estadofoco6) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nSIN CONFIRMACION.");
        //mensaje_sms2(agenda[i]);         //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
        status_luces_BT();
        Serial1.print("Sin confirmacion");
        Serial1.print("Waiting more...");
      }
    }
    mensaje = "" ;                      //Bórralo para la próxima vez
  }

   //Luz # 6. Con confirmación
  if (luz6SI >= 0){
   for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco6=!estadofoco6;
        print_port(foco5, estadofoco6) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);         //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                       //Bórralo para la próxima vez
  }

  //Luz # 7. Sin confirmación
  if (luz7 >= 0){
    for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco7=!estadofoco7;
        print_port(foco6, estadofoco7) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nSIN CONFIRMACION.");
        //mensaje_sms2(agenda[i]);         //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
        status_luces_BT();
        Serial1.print("Sin confirmacion");
        Serial1.print("Waiting more...");
      }
    }
    mensaje = "" ;                      //Bórralo para la próxima vez
  }

  //Luz # 7. Con confirmación
  if (luz7SI >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco7=!estadofoco7;
        print_port(foco6, estadofoco7) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);         //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");

      }
    }
    mensaje = "" ;                      //Bórralo para la próxima vez
  }

  //Luz # 8. Sin confirmación.
  if (luz8 >= 0){
    for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco8=!estadofoco8;
        print_port(foco7, estadofoco8) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nSIN CONFIRMACION.");
        //mensaje_sms2(agenda[i]);         //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
        status_luces_BT();
        Serial1.print("Sin confirmacion");
        Serial1.print("Waiting more...");
      }
    }
    mensaje = "" ;                         //Bórralo para la próxima vez
  }

  //Luz # 8. Con confirmación
  if (luz8SI >= 0){
    for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco8=!estadofoco8;
        print_port(foco7, estadofoco8) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);         //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                      //Bórralo para la próxima vez
  }

   /************************************************************************************************/
   /*A continuación programo las funciones de la activity "CONTROL LUCES VIA SMS MULTIPLE ALEATORIO*/
   /******************************************INICIO***************************************************/

   //Luz # 1: Con confirmación --1
     if (luz_1 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco1=!estadofoco1;               //cambia el estado del led cada 100ms
        print_port(foco0, estadofoco1);
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                      //Bórralo para la próxima vez
  }

   //Luz # 2: Con confirmación --2
      if (luz_2 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco2=!estadofoco2;              //cambia el estado del led cada 100ms
        print_port(foco1, estadofoco2) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);               //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                      //Bórralo para la próxima vez
  }

      //Luz # 3: Con confirmación --4
      if (luz_3 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco3=!estadofoco3;                //cambia el estado del led cada 100ms
        print_port(foco2, estadofoco3) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                      //Bórralo para la próxima vez
 }

    //Luz # 4: Con confirmación --8
    if (luz_4 >= 0){
    for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco4=!estadofoco4;                //cambia el estado del led cada 100ms
        print_port(foco3, estadofoco4) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                      //Bórralo para la próxima vez
  }

     //Luz # 5: Con confirmación --16
     if (luz_5 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco5=!estadofoco5;               //cambia el estado del led cada 100ms
        print_port(foco4, estadofoco5) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                      //Bórralo para la próxima vez
  }

      //Luz # 6: Con confirmación --32
     if (luz_6 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco6=!estadofoco6;                //cambia el estado del led cada 100ms
        print_port(foco5, estadofoco6) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
 }

    //Luz # 7: Con confirmación --64
    if (luz_7 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco7=!estadofoco7;                //cambia el estado del led cada 100ms
        print_port(foco6, estadofoco7) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
 }

    //Luz # 8: Con confirmación --128
    if (luz_8 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco8=!estadofoco8;
        print_port(foco7,estadofoco8) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

   /*|||||||||||||||||||||||||Hasta aquí funciona correctamente|||||||||||||||||||||||||||||||||||*/

    //Luz # 1,2: Con confirmación
      if (luz_12 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco1=!estadofoco1;
        estadofoco2=!estadofoco2;
        print_port(foco0, estadofoco1) ;
        print_port(foco1, estadofoco2) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }


  //Luz # 1,3: Con confirmación
      if (luz_13 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco1=!estadofoco1;  //cambia el estado del led cada 100ms
        estadofoco3=!estadofoco3;  //cambia el estado del led cada 100ms
        print_port(foco0, estadofoco1) ;
        print_port(foco2, estadofoco3) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
 }

 //Luz # 2,3: Con confirmación
      if (luz_23 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco2=!estadofoco2;               //cambia el estado del led cada 100ms
        estadofoco3=!estadofoco3;           //cambia el estado del led cada 100ms
        print_port(foco1, estadofoco2) ;
        print_port(foco2, estadofoco3) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

     //Luz # 1,2,3: Con confirmación
     if (luz_123 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco1=!estadofoco1;
        estadofoco2=!estadofoco2;
        estadofoco3=!estadofoco3;
        print_port(foco0, estadofoco1) ;
        print_port(foco1, estadofoco2) ;
        print_port(foco2, estadofoco3) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

  //Luz # 1,4: Con confirmación
      if (luz_14 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco1=!estadofoco1;
        estadofoco4=!estadofoco4;
        print_port(foco0, estadofoco1) ;
        print_port(foco3, estadofoco4) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }


  //Luz # 2,4: Con confirmación
      if (luz_24 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco2=!estadofoco2;
        estadofoco4=!estadofoco4;
        print_port(foco1, estadofoco2) ;
        print_port(foco3, estadofoco4) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

  /*((((((((((((((((((((((((((((((((((((((((((TODO)))))))))))))))))))))))))))))))))))))))))))))))))))))*/
  /*((((((((((((((((((((((((((((((((((((((((((begin))))))))))))))))))))))))))))))))))))))))))))))))))))*/
  //Luz # 1,2,4: Con confirmación -- 11
     if (luz_124 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco1=!estadofoco1;
        estadofoco2=!estadofoco2;
        estadofoco4=!estadofoco4;
        print_port(foco0, estadofoco1) ;
        print_port(foco1, estadofoco2) ;
        print_port(foco3, estadofoco4) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
   }

    //Luz # 3,4: Con confirmación --12
      if (luz_34 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
         estadofoco3=!estadofoco3;  //cambia el estado del led cada 100ms
         estadofoco4=!estadofoco4;  //cambia el estado del led cada 100ms
         print_port(foco2, estadofoco3) ;
         print_port(foco3, estadofoco4) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

    //Luz # 1,3,4: Con confirmación --13
      if (luz_134 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco1=!estadofoco1;
        estadofoco3=!estadofoco3;
        estadofoco4=!estadofoco4;
        print_port(foco0, estadofoco1) ;
        print_port(foco2, estadofoco3) ;
        print_port(foco3, estadofoco4) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }


   //Luz # 2,3,4: Con confirmación --14
      if (luz_234 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
       estadofoco2=!estadofoco2;
       estadofoco3=!estadofoco3;
       estadofoco4=!estadofoco4;
       print_port(foco1, estadofoco2) ;
       print_port(foco2, estadofoco3) ;
       print_port(foco3, estadofoco4) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
   }

   //Luz # 1,2,3,4: Con confirmación --15
      if (luz_1234 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco1=!estadofoco1;
        estadofoco2=!estadofoco2;
        estadofoco3=!estadofoco3;
        estadofoco4=!estadofoco4;
        print_port(foco0, estadofoco1) ;
        print_port(foco1, estadofoco2) ;
        print_port(foco2, estadofoco3) ;
        print_port(foco3, estadofoco4) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

  /*[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[[16 YA ESTA ARRIBA]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]]*/

  //Luz # 1,5: Con confirmación --17
      if (luz_15 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco1=!estadofoco1;
        estadofoco5=!estadofoco5;
        print_port(foco0, estadofoco1) ;
        print_port(foco4, estadofoco5) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
 }

     //Luz # 2,5: Con confirmación --18
      if (luz_25 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
       estadofoco2=!estadofoco2;
       estadofoco5=!estadofoco5;
       print_port(foco1, estadofoco2) ;
       print_port(foco4, estadofoco5) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
 }

    //Luz # 1,2,5: Con confirmación --19
      if (luz_125 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
       estadofoco1=!estadofoco1;  //cambia el estado del led cada 100ms
       estadofoco2=!estadofoco2;  //cambia el estado del led cada 100ms
       estadofoco5=!estadofoco5;  //cambia el estado del led cada 100ms
       print_port(foco0, estadofoco1) ;
       print_port(foco1, estadofoco2) ;
       print_port(foco4, estadofoco5) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
 }

    //Luz # 3,5: Con confirmación --20
      if (luz_35 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
       estadofoco3=!estadofoco3;  //cambia el estado del led cada 100ms
       estadofoco5=!estadofoco5;  //cambia el estado del led cada 100ms
       print_port(foco2, estadofoco3) ;
       print_port(foco4, estadofoco5) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

    //Luz # 1,3,5: Con confirmación --21
      if (luz_135 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
       estadofoco1=!estadofoco1;
       estadofoco3=!estadofoco3;
       estadofoco5=!estadofoco5;
       print_port(foco0, estadofoco1) ;
       print_port(foco2, estadofoco3) ;
       print_port(foco4, estadofoco5) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

   //Luz # 2,3,5: Con confirmación --22
      if (luz_235 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco2=!estadofoco2;
        estadofoco3=!estadofoco3;
        estadofoco5=!estadofoco5;
        print_port(foco1, estadofoco2) ;
        print_port(foco2, estadofoco3) ;
        print_port(foco4, estadofoco5) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }


     //Luz # 1,2,3,5: Con confirmación --23
     if (luz_1235 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco1=!estadofoco1;  //cambia el estado del led cada 100ms
        estadofoco2=!estadofoco2;  //cambia el estado del led cada 100ms
        estadofoco3=!estadofoco3;  //cambia el estado del led cada 100ms
        estadofoco5=!estadofoco5;  //cambia el estado del led cada 100ms
        print_port(foco0, estadofoco1) ;
        print_port(foco1, estadofoco2) ;
        print_port(foco2, estadofoco3) ;
        print_port(foco4, estadofoco5) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

    //Luz # 4,5: Con confirmación --24
      if (luz_45 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco4=!estadofoco4;  //cambia el estado del led cada 100ms
        estadofoco5=!estadofoco5;  //cambia el estado del led cada 100ms
        print_port(foco3, estadofoco4) ;
        print_port(foco4, estadofoco5) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }


    //Luz # 1,4,5: Con confirmación --25
      if (luz_145 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco1=!estadofoco1;
        estadofoco4=!estadofoco4;
        estadofoco5=!estadofoco5;
        print_port(foco0, estadofoco1) ;
        print_port(foco3, estadofoco4) ;
        print_port(foco4, estadofoco5) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
   }

   //Luz # 2,4,5: Con confirmación --26
      if (luz_245 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco2=!estadofoco2;  //cambia el estado del led cada 100ms
        estadofoco4=!estadofoco4;  //cambia el estado del led cada 100ms
        estadofoco5=!estadofoco5;  //cambia el estado del led cada 100ms
        print_port(foco1, estadofoco2) ;
        print_port(foco3, estadofoco4) ;
        print_port(foco4, estadofoco5) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

   //Luz # 1,2,4,5: Con confirmación --27
      if (luz_1245 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco1=!estadofoco1;  //cambia el estado del led cada 100ms
        estadofoco2=!estadofoco2;  //cambia el estado del led cada 100ms
        estadofoco4=!estadofoco4;  //cambia el estado del led cada 100ms
        estadofoco5=!estadofoco5;  //cambia el estado del led cada 100ms
        print_port(foco0, estadofoco1) ;
        print_port(foco1, estadofoco2) ;
        print_port(foco3, estadofoco4) ;
        print_port(foco4, estadofoco5) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
 }

    //Luz # 3,4,5: Con confirmación --28
     if (luz_345 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco3=!estadofoco3;
        estadofoco4=!estadofoco4;
        estadofoco5=!estadofoco5;
        print_port(foco2, estadofoco3) ;
        print_port(foco3, estadofoco4) ;
        print_port(foco4, estadofoco5) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

  //Luz # 1,3,4,5: Con confirmación --29
     if (luz_1345 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco1=!estadofoco1;
        estadofoco3=!estadofoco3;
        estadofoco4=!estadofoco4;
        estadofoco5=!estadofoco5;
        print_port(foco0, estadofoco1) ;
        print_port(foco2, estadofoco3) ;
        print_port(foco3, estadofoco4) ;
        print_port(foco4, estadofoco5) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

     //Luz # 2,3,4,5: Con confirmación --30
      if (luz_2345 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco2=!estadofoco2;
        estadofoco3=!estadofoco3;
        estadofoco4=!estadofoco4;
        estadofoco5=!estadofoco5;
        print_port(foco1, estadofoco2) ;
        print_port(foco2, estadofoco3) ;
        print_port(foco3, estadofoco4) ;
        print_port(foco4, estadofoco5) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }


   /********************************************************************/

     //Luz # 1,2,3,4,5: Con confirmación --31
      if (luz_12345 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco1=!estadofoco1;
        estadofoco2=!estadofoco2;
        estadofoco3=!estadofoco3;
        estadofoco4=!estadofoco4;
        estadofoco5=!estadofoco5;
        print_port(foco0, estadofoco1) ;
        print_port(foco1, estadofoco2) ;
        print_port(foco2, estadofoco3) ;
        print_port(foco3, estadofoco4) ;
        print_port(foco4, estadofoco5) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

  //Luz # 1,6: Con confirmación --33
      if (luz_16 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco1=!estadofoco1;
        estadofoco6=!estadofoco6;
        print_port(foco0, estadofoco1) ;
        print_port(foco5, estadofoco6) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

  //Luz # 2,6: Con confirmación --34
      if (luz_26 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco2=!estadofoco2;
        estadofoco6=!estadofoco6;
        print_port(foco1, estadofoco2) ;
        print_port(foco5, estadofoco6) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

    //Luz # 1,2,6: Con confirmación --35
      if (luz_126 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco1=!estadofoco1;
        estadofoco2=!estadofoco2;
        estadofoco6=!estadofoco6;
        print_port(foco0, estadofoco1) ;
        print_port(foco1, estadofoco2) ;
        print_port(foco5, estadofoco6) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

  //Luz # 3,6: Con confirmación --36
    if (luz_36 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco3=!estadofoco3;
        estadofoco6=!estadofoco6;
        print_port(foco2, estadofoco3);
        print_port(foco5, estadofoco6);
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

    //Luz # 1,3,6: Con confirmación --37
    if (luz_136 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco1=!estadofoco1;
        estadofoco3=!estadofoco3;
        estadofoco6=!estadofoco6;
        print_port(foco0, estadofoco1) ;
        print_port(foco2, estadofoco3) ;
        print_port(foco5, estadofoco6) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }


     //Luz # 2,3,6: Con confirmación --38
    if (luz_236 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco2=!estadofoco2;
        estadofoco3=!estadofoco3;
        estadofoco6=!estadofoco6;
        print_port(foco1, estadofoco2) ;
        print_port(foco2, estadofoco3) ;
        print_port(foco5, estadofoco6) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

      //Luz # 1,2,3,6: Con confirmación --39
    if (luz_1236 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco1=!estadofoco1;
        estadofoco2=!estadofoco2;
        estadofoco3=!estadofoco3;
        estadofoco6=!estadofoco6;
        print_port(foco0, estadofoco1) ;
        print_port(foco1, estadofoco2) ;
        print_port(foco2, estadofoco3) ;
        print_port(foco5, estadofoco6) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

    //Luz # 4,6: Con confirmación --40
    if (luz_46 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
       estadofoco4=!estadofoco4;  //cambia el estado del led cada 100ms
       estadofoco6=!estadofoco6;  //cambia el estado del led cada 100ms
       print_port(foco3, estadofoco4) ;
       print_port(foco5, estadofoco6) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

    /*********************************************************************/

    //Luz # 1,4,6: Con confirmación --41
    if (luz_146 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
       estadofoco1=!estadofoco1;
       estadofoco4=!estadofoco4;
       estadofoco6=!estadofoco6;
       print_port(foco1, estadofoco1) ;
       print_port(foco3, estadofoco4) ;
       print_port(foco5, estadofoco6) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

  //Luz # 2,4,6: Con confirmación --42
    if (luz_246 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
       estadofoco2=!estadofoco2;
       estadofoco4=!estadofoco4;
       estadofoco6=!estadofoco6;
       print_port(foco1, estadofoco2) ;
       print_port(foco3, estadofoco4) ;
       print_port(foco5, estadofoco6) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

  //Luz # 1,2,4,6: Con confirmación --43
    if (luz_1246 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco1=!estadofoco1;  //cambia el estado del led cada 100ms
        estadofoco2=!estadofoco2;  //cambia el estado del led cada 100ms
        estadofoco4=!estadofoco4;  //cambia el estado del led cada 100ms
        estadofoco6=!estadofoco6;  //cambia el estado del led cada 100ms
        print_port(foco0, estadofoco1) ;
        print_port(foco1, estadofoco2) ;
        print_port(foco3, estadofoco4) ;
        print_port(foco5, estadofoco6) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
 }

    //Luz # 3,4,6: Con confirmación --44
    if (luz_346 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco3=!estadofoco3;
          estadofoco4=!estadofoco4;
          estadofoco6=!estadofoco6;
          print_port(foco2, estadofoco3) ;
          print_port(foco3, estadofoco4) ;
          print_port(foco5, estadofoco6) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }


  //Luz # 1,3,4,6: Con confirmación --45
    if (luz_1346 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco1=!estadofoco1;
        estadofoco3=!estadofoco3;
        estadofoco4=!estadofoco4;
        estadofoco6=!estadofoco6;
        print_port(foco0, estadofoco1) ;
        print_port(foco2, estadofoco3) ;
        print_port(foco3, estadofoco4) ;
        print_port(foco5, estadofoco6) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

     //Luz # 2,3,4,6: Con confirmación --46
    if (luz_2346 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco2=!estadofoco2;
        estadofoco3=!estadofoco3;
        estadofoco4=!estadofoco4;
        estadofoco6=!estadofoco6;
        print_port(foco1, estadofoco2) ;
        print_port(foco2, estadofoco3) ;
        print_port(foco3, estadofoco4) ;
        print_port(foco5, estadofoco6) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
   }

    //Luz # 1,2,3,4,6: Con confirmación --47
    if (luz_12346 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco1=!estadofoco1;
        estadofoco2=!estadofoco2;
        estadofoco3=!estadofoco3;
        estadofoco4=!estadofoco4;
        estadofoco6=!estadofoco6;
        print_port(foco0, estadofoco1) ;
        print_port(foco1, estadofoco2) ;
        print_port(foco2, estadofoco3) ;
        print_port(foco3, estadofoco4) ;
        print_port(foco5, estadofoco6) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
   }

   //Luz # 5,6: Con confirmación --48
    if (luz_56 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco5=!estadofoco5;
        estadofoco6=!estadofoco6;
        print_port(foco4, estadofoco5) ;
        print_port(foco5, estadofoco6) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
   }

    //Luz # 1,5,6: Con confirmación --49
    if (luz_156 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco1=!estadofoco1;
        estadofoco5=!estadofoco5;
        estadofoco6=!estadofoco6;
        print_port(foco0, estadofoco1) ;
        print_port(foco4, estadofoco5) ;
        print_port(foco5, estadofoco6) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

  //FUNCIONA PERFECTAMENTE HASTA ACÁ
    //Luz # 2,5,6: Con confirmación --50
    if (luz_256 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco2=!estadofoco2;  //cambia el estado del led cada 100ms
        estadofoco5=!estadofoco5;  //cambia el estado del led cada 100ms
        estadofoco6=!estadofoco6;  //cambia el estado del led cada 100ms
        print_port(foco1, estadofoco2) ;
        print_port(foco4, estadofoco5) ;
        print_port(foco5, estadofoco6) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

    /***********************************************************/
    //Luz # 1,2,5,6: Con confirmación --51
    if (luz_1256 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco1=!estadofoco1;
        estadofoco2=!estadofoco2;
        estadofoco5=!estadofoco5;
        estadofoco6=!estadofoco6;
        print_port(foco0, estadofoco1) ;
        print_port(foco1, estadofoco2) ;
        print_port(foco4, estadofoco5) ;
        print_port(foco5, estadofoco6) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

  //Luz # 3,5,6: Con confirmación --52
    if (luz_356 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco3=!estadofoco3;
        estadofoco5=!estadofoco5;
        estadofoco6=!estadofoco6;
        print_port(foco2, estadofoco3) ;
        print_port(foco4, estadofoco5) ;
        print_port(foco5, estadofoco6) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
   }

    //Luz # 1,3,5,6: Con confirmación --53
    if (luz_1356 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco3=!estadofoco3;
          estadofoco5=!estadofoco5;
          estadofoco6=!estadofoco6;
          print_port(foco0, estadofoco1) ;
          print_port(foco2, estadofoco3) ;
          print_port(foco4, estadofoco5) ;
          print_port(foco5, estadofoco6) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

    //Luz # 2,3,5,6: Con confirmación --54
    if (luz_2356 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco2=!estadofoco2;
          estadofoco3=!estadofoco3;
          estadofoco5=!estadofoco5;
          estadofoco6=!estadofoco6;
          print_port(foco1, estadofoco2) ;
          print_port(foco2, estadofoco3) ;
          print_port(foco4, estadofoco5) ;
          print_port(foco5, estadofoco6) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
   }


   //Luz # 1,2,3,5,6: Con confirmación --55
    if (luz_12356 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco2=!estadofoco2;
          estadofoco3=!estadofoco3;
          estadofoco5=!estadofoco5;
          estadofoco6=!estadofoco6;
          print_port(foco0, estadofoco1) ;
          print_port(foco1, estadofoco2) ;
          print_port(foco2, estadofoco3) ;
          print_port(foco4, estadofoco5) ;
          print_port(foco5, estadofoco6) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

   //Luz # 4,5,6: Con confirmación --56
    if (luz_456 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco4=!estadofoco4;
          estadofoco5=!estadofoco5;
          estadofoco6=!estadofoco6;
          print_port(foco3, estadofoco4) ;
          print_port(foco4, estadofoco5) ;
          print_port(foco5, estadofoco6) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

  //Luz # 1,4,5,6: Con confirmación --57
    if (luz_1456 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco4=!estadofoco4;
          estadofoco5=!estadofoco5;
          estadofoco6=!estadofoco6;
          print_port(foco0, estadofoco1) ;
          print_port(foco3, estadofoco4) ;
          print_port(foco4, estadofoco5) ;
          print_port(foco5, estadofoco6) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

  //Luz # 2,4,5,6: Con confirmación --58
    if (luz_2456 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco2=!estadofoco2;
          estadofoco4=!estadofoco4;
          estadofoco5=!estadofoco5;
          estadofoco6=!estadofoco6;
          print_port(foco1, estadofoco2) ;
          print_port(foco3, estadofoco4) ;
          print_port(foco4, estadofoco5) ;
          print_port(foco5, estadofoco6) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
   }

   //Luz # 1,2,4,5,6: Con confirmación --59
    if (luz_12456 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco2=!estadofoco2;
          estadofoco4=!estadofoco4;
          estadofoco5=!estadofoco5;
          estadofoco6=!estadofoco6;
          print_port(foco0, estadofoco1) ;
          print_port(foco1, estadofoco2) ;
          print_port(foco3, estadofoco4) ;
          print_port(foco4, estadofoco5) ;
          print_port(foco5, estadofoco6) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

   //Luz # 3,4,5,6: Con confirmación --60
    if (luz_3456 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco3=!estadofoco3;
          estadofoco4=!estadofoco4;
          estadofoco5=!estadofoco5;
          estadofoco6=!estadofoco6;
          print_port(foco2, estadofoco3) ;
          print_port(foco3, estadofoco4) ;
          print_port(foco4, estadofoco5) ;
          print_port(foco5, estadofoco6) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
   }

   /**********************************************************************/

    //Luz # 1,3,4,5,6: Con confirmación --61
    if (luz_13456 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco3=!estadofoco3;
          estadofoco4=!estadofoco4;
          estadofoco5=!estadofoco5;
          estadofoco6=!estadofoco6;
          print_port(foco0, estadofoco1) ;
          print_port(foco2, estadofoco3) ;
          print_port(foco3, estadofoco4) ;
          print_port(foco4, estadofoco5) ;
          print_port(foco5, estadofoco6) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

    //Luz # 2,3,4,5,6: Con confirmación --62
    if (luz_23456 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco2=!estadofoco2;
          estadofoco3=!estadofoco3;
          estadofoco4=!estadofoco4;
          estadofoco5=!estadofoco5;
          estadofoco6=!estadofoco6;
          print_port(foco1, estadofoco2) ;
          print_port(foco2, estadofoco3) ;
          print_port(foco3, estadofoco4) ;
          print_port(foco4, estadofoco5) ;
          print_port(foco5, estadofoco6) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }


  //Luz # 1,2,3,4,5,6: Con confirmación --63
    if (luz_123456 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco2=!estadofoco2;
          estadofoco3=!estadofoco3;
          estadofoco4=!estadofoco4;
          estadofoco5=!estadofoco5;
          estadofoco6=!estadofoco6;
          print_port(foco0, estadofoco1) ;
          print_port(foco1, estadofoco2) ;
          print_port(foco2, estadofoco3) ;
          print_port(foco3, estadofoco4) ;
          print_port(foco4, estadofoco5) ;
          print_port(foco5, estadofoco6) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
   }

   //Luz # 1,7: Con confirmación --65
    if (luz_17 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco7=!estadofoco7;
          print_port(foco0, estadofoco1) ;
          print_port(foco6, estadofoco7) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

  //Luz # 2,7: Con confirmación --66
    if (luz_27 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco2=!estadofoco2;
          estadofoco7=!estadofoco7;
          print_port(foco1, estadofoco2) ;
          print_port(foco6, estadofoco7) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
   }

   //Luz # 1,2,7: Con confirmación --67
    if (luz_127 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco2=!estadofoco2;
          estadofoco7=!estadofoco7;
          print_port(foco0, estadofoco1) ;
          print_port(foco1, estadofoco2) ;
          print_port(foco6, estadofoco7) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

  //Luz # 3,7: Con confirmación --68
    if (luz_37 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco3=!estadofoco3;
          estadofoco7=!estadofoco7;
          print_port(foco2, estadofoco3) ;
          print_port(foco6, estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

  //Luz # 1,3,7: Con confirmación --69
    if (luz_137 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco1=!estadofoco1;
        estadofoco3=!estadofoco3;
        estadofoco7=!estadofoco7;
        print_port(foco0, estadofoco1) ;
        print_port(foco2, estadofoco3) ;
        print_port(foco6, estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

  //Luz # 2,3,7: Con confirmación --70
    if (luz_237 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco2=!estadofoco2;
        estadofoco3=!estadofoco3;
        estadofoco7=!estadofoco7;
        print_port(foco1, estadofoco2) ;
        print_port(foco2, estadofoco3) ;
        print_port(foco6, estadofoco7) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

     /********************************************************************/

    //Luz # 1,2,3,7: Con confirmación --71
    if (luz_1237 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco2=!estadofoco2;
          estadofoco3=!estadofoco3;
          estadofoco7=!estadofoco7;
          print_port(foco0, estadofoco1) ;
          print_port(foco1, estadofoco2) ;
          print_port(foco2, estadofoco3) ;
          print_port(foco6, estadofoco7) ;
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

   //Luz # 4,7: Con confirmación --72
    if (luz_47 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco4=!estadofoco4;
          estadofoco7=!estadofoco7;
          print_port(foco3, estadofoco4) ;
          print_port(foco6, estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

     //Luz # 1,4,7: Con confirmación --73
    if (luz_147 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco4=!estadofoco4;
          estadofoco7=!estadofoco7;
          print_port(foco0, estadofoco1) ;
          print_port(foco3, estadofoco4) ;
          print_port(foco6, estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
   }

   //Luz # 2,4,7: Con confirmación --74
    if (luz_247 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco2=!estadofoco2;
          estadofoco4=!estadofoco4;
          estadofoco7=!estadofoco7;
          print_port(foco1, estadofoco2) ;
          print_port(foco3, estadofoco4) ;
          print_port(foco6, estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

  //Luz # 1,2,4,7: Con confirmación --75
    if (luz_1247 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco2=!estadofoco2;
          estadofoco4=!estadofoco4;
          estadofoco7=!estadofoco7;
          print_port(foco0, estadofoco1) ;
          print_port(foco1, estadofoco2) ;
          print_port(foco3, estadofoco4) ;
          print_port(foco6, estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

  //Luz # 3,4,7: Con confirmación --76
    if (luz_347 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
            estadofoco3=!estadofoco3;
            estadofoco4=!estadofoco4;
            estadofoco7=!estadofoco7;
            print_port(foco2, estadofoco3) ;
            print_port(foco3, estadofoco4) ;
            print_port(foco6, estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

  //Luz # 1,3,4,7: Con confirmación --77
    if (luz_1347 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco3=!estadofoco3;
          estadofoco4=!estadofoco4;
          estadofoco7=!estadofoco7;
          print_port(foco0, estadofoco1) ;
          print_port(foco2, estadofoco3) ;
          print_port(foco3, estadofoco4) ;
          print_port(foco6, estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

  //Luz # 2,3,4,7: Con confirmación --78
    if (luz_2347 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco2=!estadofoco2;
          estadofoco3=!estadofoco3;
          estadofoco4=!estadofoco4;
          estadofoco7=!estadofoco7;
          print_port(foco1, estadofoco2) ;
          print_port(foco2, estadofoco3) ;
          print_port(foco3, estadofoco4) ;
          print_port(foco6, estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

  //Luz # 1,2,3,4,7: Con confirmación --79
    if (luz_12347 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
            estadofoco1=!estadofoco1;
            estadofoco2=!estadofoco2;
            estadofoco3=!estadofoco3;
            estadofoco4=!estadofoco4;
            estadofoco7=!estadofoco7;
            print_port(foco0, estadofoco1) ;
            print_port(foco1, estadofoco2) ;
            print_port(foco2, estadofoco3) ;
            print_port(foco3, estadofoco4) ;
            print_port(foco6, estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

  //Luz # 5,7: Con confirmación --80
    if (luz_57 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco5=!estadofoco5;
          estadofoco7=!estadofoco7;
          print_port(foco4, estadofoco5) ;
          print_port(foco6, estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
   }

    //Luz # 1,5,7: Con confirmación --81
    if (luz_157 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco5=!estadofoco5;
          estadofoco7=!estadofoco7;
          print_port(foco0, estadofoco1) ;
          print_port(foco4, estadofoco5) ;
          print_port(foco6, estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

   /*****************************************************************************************************/

    //Luz # 2,5,7: Con confirmación --82
    if (luz_257 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco2=!estadofoco2;
          estadofoco5=!estadofoco5;
          estadofoco7=!estadofoco7;
          print_port(foco1, estadofoco2) ;
          print_port(foco4, estadofoco5) ;
          print_port(foco6, estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

  //Luz # 1,2,5,7: Con confirmación --83
    if (luz_1257 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco2=!estadofoco2;
          estadofoco5=!estadofoco5;
          estadofoco7=!estadofoco7;
          print_port(foco0, estadofoco1) ;
          print_port(foco1, estadofoco2) ;
          print_port(foco4, estadofoco5) ;
          print_port(foco6, estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

    //Luz # 3,5,7: Con confirmación --84
    if (luz_357 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco3=!estadofoco3;
          estadofoco5=!estadofoco5;
          estadofoco7=!estadofoco7;
          print_port(foco2, estadofoco3) ;
          print_port(foco4, estadofoco5) ;
          print_port(foco6, estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,3,5,7: Con confirmación --85
    if (luz_1357 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco3=!estadofoco3;
          estadofoco5=!estadofoco5;
          estadofoco7=!estadofoco7;
          print_port(foco0, estadofoco1) ;
          print_port(foco2, estadofoco3) ;
          print_port(foco4, estadofoco5) ;
          print_port(foco6, estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }


  //Luz # 2,3,5,7: Con confirmación --86
    if (luz_2357 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco2=!estadofoco2;
          estadofoco3=!estadofoco3;
          estadofoco5=!estadofoco5;
          estadofoco7=!estadofoco7;
          print_port(foco1, estadofoco2) ;
          print_port(foco2, estadofoco3) ;
          print_port(foco4, estadofoco5) ;
          print_port(foco6, estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

    //Luz # 1,2,3,5,7: Con confirmación --87
    if (luz_12357 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco2=!estadofoco2;
          estadofoco3=!estadofoco3;
          estadofoco5=!estadofoco5;
          estadofoco7=!estadofoco7;
          print_port(foco0, estadofoco1) ;
          print_port(foco1, estadofoco2) ;
          print_port(foco2, estadofoco3) ;
          print_port(foco4, estadofoco5) ;
          print_port(foco6, estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 4,5,7: Con confirmación --88
    if (luz_457 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco4=!estadofoco4;
          estadofoco5=!estadofoco5;
          estadofoco7=!estadofoco7;
          print_port(foco3, estadofoco4) ;
          print_port(foco4, estadofoco5) ;
          print_port(foco6, estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

    //Luz # 1,4,5,7: Con confirmación --89
    if (luz_1457 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco4=!estadofoco4;
          estadofoco5=!estadofoco5;
          estadofoco7=!estadofoco7;
          print_port(foco0, estadofoco1) ;
          print_port(foco3, estadofoco4) ;
          print_port(foco4, estadofoco5) ;
          print_port(foco6, estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 2,4,5,7: Con confirmación --90
    if (luz_2457 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco2=!estadofoco2;
          estadofoco4=!estadofoco4;
          estadofoco5=!estadofoco5;
          estadofoco7=!estadofoco7;
          print_port(foco1, estadofoco2) ;
          print_port(foco3, estadofoco4) ;
          print_port(foco4, estadofoco5) ;
          print_port(foco6, estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

    /*****************************************************************************************************/
   //Luz # 1,2,4,5,7: Con confirmación --91
    if (luz_12457 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco2=!estadofoco2;
          estadofoco4=!estadofoco4;
          estadofoco5=!estadofoco5;
          estadofoco7=!estadofoco7;
          print_port(foco0, estadofoco1) ;
          print_port(foco1, estadofoco2) ;
          print_port(foco3, estadofoco4) ;
          print_port(foco4, estadofoco5) ;
          print_port(foco6, estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 3,4,5,7: Con confirmación --92
    if (luz_3457 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco3=!estadofoco3;
          estadofoco4=!estadofoco4;
          estadofoco5=!estadofoco5;
          estadofoco7=!estadofoco7;
          print_port(foco2, estadofoco3) ;
          print_port(foco3, estadofoco4) ;
          print_port(foco4, estadofoco5) ;
          print_port(foco6, estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,3,4,5,7: Con confirmación --93
    if (luz_13457 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco3=!estadofoco3;
          estadofoco4=!estadofoco4;
          estadofoco5=!estadofoco5;
          estadofoco7=!estadofoco7;
          print_port(foco0, estadofoco1) ;
          print_port(foco2, estadofoco3) ;
          print_port(foco3, estadofoco4) ;
          print_port(foco4, estadofoco5) ;
          print_port(foco6, estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

    //Luz # 2,3,4,5,7: Con confirmación --94
    if (luz_23457 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco2=!estadofoco2;
          estadofoco3=!estadofoco3;
          estadofoco4=!estadofoco4;
          estadofoco5=!estadofoco5;
          estadofoco7=!estadofoco7;
          print_port(foco1, estadofoco2) ;
          print_port(foco2, estadofoco3) ;
          print_port(foco3, estadofoco4) ;
          print_port(foco4, estadofoco5) ;
          print_port(foco6, estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
   }

    //Luz # 1,2,3,4,5,7: Con confirmación --95
    if (luz_123457 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco2=!estadofoco2;
          estadofoco3=!estadofoco3;
          estadofoco4=!estadofoco4;
          estadofoco5=!estadofoco5;
          estadofoco7=!estadofoco7;
          print_port(foco0, estadofoco1) ;
          print_port(foco1, estadofoco2) ;
          print_port(foco2, estadofoco3) ;
          print_port(foco3, estadofoco4) ;
          print_port(foco4, estadofoco5) ;
          print_port(foco6, estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
   }

    //Luz # 6,7: Con confirmación --96
    if (luz_67 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco6=!estadofoco6;
          estadofoco7=!estadofoco7;
          print_port(foco5, estadofoco6) ;
          print_port(foco6, estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,6,7: Con confirmación --97
    if (luz_167 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco6=!estadofoco6;
          estadofoco7=!estadofoco7;
          print_port(foco0, estadofoco1) ;
          print_port(foco5, estadofoco6) ;
          print_port(foco6, estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 2,6,7: Con confirmación --98
    if (luz_267 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco2=!estadofoco2;
          estadofoco6=!estadofoco6;
          estadofoco7=!estadofoco7;
          print_port(foco1, estadofoco2) ;
          print_port(foco5, estadofoco6) ;
          print_port(foco6, estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,2,6,7: Con confirmación --99
    if (luz_1267 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco2=!estadofoco2;
          estadofoco6=!estadofoco6;
          estadofoco7=!estadofoco7;
          print_port(foco0, estadofoco1) ;
          print_port(foco1, estadofoco2) ;
          print_port(foco5, estadofoco6) ;
          print_port(foco6, estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 3,6,7: Con confirmación --100
    if (luz_367 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco3=!estadofoco3;
          estadofoco6=!estadofoco6;
          estadofoco7=!estadofoco7;
          print_port(foco2, estadofoco3) ;
          print_port(foco5, estadofoco6) ;
          print_port(foco6, estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }
  /*******************************************************************************************************/

    //Luz # 1,3,6,7: Con confirmación --101
    if (luz_1367 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco3=!estadofoco3;
          estadofoco6=!estadofoco6;
          estadofoco7=!estadofoco7;
          print_port(foco0, estadofoco1) ;
          print_port(foco2, estadofoco3) ;
          print_port(foco5, estadofoco6) ;
          print_port(foco6, estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 2,3,6,7: Con confirmación --102
    if (luz_2367 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco2=!estadofoco2;
          estadofoco3=!estadofoco3;
          estadofoco6=!estadofoco6;
          estadofoco7=!estadofoco7;
          print_port(foco1, estadofoco2) ;
          print_port(foco2, estadofoco3) ;
          print_port(foco5, estadofoco6) ;
          print_port(foco6, estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,2,3,6,7: Con confirmación --103
    if (luz_12367 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco2=!estadofoco2;
          estadofoco3=!estadofoco3;
          estadofoco6=!estadofoco6;
          estadofoco7=!estadofoco7;
          print_port(foco0, estadofoco1) ;
          print_port(foco1, estadofoco2) ;
          print_port(foco2, estadofoco3) ;
          print_port(foco5, estadofoco6) ;
          print_port(foco6, estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 4,6,7: Con confirmación --104
    if (luz_467 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco4=!estadofoco4;
          estadofoco6=!estadofoco6;
          estadofoco7=!estadofoco7;
          print_port(foco3, estadofoco4) ;
          print_port(foco5, estadofoco6) ;
          print_port(foco6, estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,4,6,7: Con confirmación --105
    if (luz_1467 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco4=!estadofoco4;
          estadofoco6=!estadofoco6;
          estadofoco7=!estadofoco7;
          print_port(foco0, estadofoco1) ;
          print_port(foco3, estadofoco4) ;
          print_port(foco5, estadofoco6) ;
          print_port(foco6, estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 2,4,6,7: Con confirmación --106
    if (luz_2467 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco2=!estadofoco2;
          estadofoco4=!estadofoco4;
          estadofoco6=!estadofoco6;
          estadofoco7=!estadofoco7;
          print_port(foco1, estadofoco2) ;
          print_port(foco3, estadofoco4) ;
          print_port(foco5, estadofoco6) ;
          print_port(foco6, estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,2,4,6,7: Con confirmación --107
    if (luz_12467 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco2=!estadofoco2;
          estadofoco4=!estadofoco4;
          estadofoco6=!estadofoco6;
          estadofoco7=!estadofoco7;
          print_port(foco0, estadofoco1) ;
          print_port(foco1, estadofoco2) ;
          print_port(foco3, estadofoco4) ;
          print_port(foco5, estadofoco6) ;
          print_port(foco6, estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
   }

   //Luz # 3,4,6,7: Con confirmación --108
    if (luz_3467 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco3=!estadofoco3;
          estadofoco4=!estadofoco4;
          estadofoco6=!estadofoco6;
          estadofoco7=!estadofoco7;
          print_port(foco2, estadofoco3) ;
          print_port(foco3, estadofoco4) ;
          print_port(foco5, estadofoco6) ;
          print_port(foco6, estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,3,4,6,7: Con confirmación --109
    if (luz_13467 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco3=!estadofoco3;
          estadofoco4=!estadofoco4;
          estadofoco6=!estadofoco6;
          estadofoco7=!estadofoco7;
          print_port(foco0, estadofoco1) ;
          print_port(foco2, estadofoco3) ;
          print_port(foco3, estadofoco4) ;
          print_port(foco5, estadofoco6) ;
          print_port(foco6, estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 2,3,4,6,7: Con confirmación --110
    if (luz_23467 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco2=!estadofoco2;
          estadofoco3=!estadofoco3;
          estadofoco4=!estadofoco4;
          estadofoco6=!estadofoco6;
          estadofoco7=!estadofoco7;
          print_port(foco1, estadofoco2) ;
          print_port(foco2, estadofoco3) ;
          print_port(foco3, estadofoco4) ;
          print_port(foco5, estadofoco6) ;
          print_port(foco6, estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

    /****************************************************************************************************/
    //Luz # 1,2,3,4,6,7: Con confirmación --111
    if (luz_123467 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco2=!estadofoco2;
          estadofoco3=!estadofoco3;
          estadofoco4=!estadofoco4;
          estadofoco6=!estadofoco6;
          estadofoco7=!estadofoco7;
          print_port(foco0, estadofoco1) ;
          print_port(foco1, estadofoco2) ;
          print_port(foco2, estadofoco3) ;
          print_port(foco3, estadofoco4) ;
          print_port(foco5, estadofoco6) ;
          print_port(foco6, estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

    //Luz # 5,6,7: Con confirmación --112
    if (luz_567 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco5=!estadofoco5;
          estadofoco6=!estadofoco6;
          estadofoco7=!estadofoco7;
          print_port(foco4, estadofoco5) ;
          print_port(foco5, estadofoco6) ;
          print_port(foco6, estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,5,6,7: Con confirmación --113
    if (luz_1567 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco5=!estadofoco5;
          estadofoco6=!estadofoco6;
          estadofoco7=!estadofoco7;
          print_port(foco0, estadofoco1) ;
          print_port(foco4, estadofoco5) ;
          print_port(foco5, estadofoco6) ;
          print_port(foco6, estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 2,5,6,7: Con confirmación --114
    if (luz_2567 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco2=!estadofoco2;
          estadofoco5=!estadofoco5;
          estadofoco6=!estadofoco6;
          estadofoco7=!estadofoco7;
          print_port(foco1, estadofoco2) ;
          print_port(foco4, estadofoco5) ;
          print_port(foco5, estadofoco6) ;
          print_port(foco6, estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,2,5,6,7: Con confirmación --115
    if (luz_12567 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco2=!estadofoco2;
          estadofoco5=!estadofoco5;
          estadofoco6=!estadofoco6;
          estadofoco7=!estadofoco7;
          print_port(foco0, estadofoco1) ;
          print_port(foco1, estadofoco2) ;
          print_port(foco4, estadofoco5) ;
          print_port(foco5, estadofoco6) ;
          print_port(foco6, estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
   }

   //Luz # 3,5,6,7: Con confirmación --116
    if (luz_3567 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco3=!estadofoco3;
          estadofoco5=!estadofoco5;
          estadofoco6=!estadofoco6;
          estadofoco7=!estadofoco7;
          print_port(foco2, estadofoco3) ;
          print_port(foco4, estadofoco5) ;
          print_port(foco5, estadofoco6) ;
          print_port(foco6, estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
   }

   //Luz # 1,3,5,6,7: Con confirmación --117
    if (luz_13567 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco3=!estadofoco3;
          estadofoco5=!estadofoco5;
          estadofoco6=!estadofoco6;
          estadofoco7=!estadofoco7;
          print_port(foco0, estadofoco1) ;
          print_port(foco2, estadofoco3) ;
          print_port(foco4, estadofoco5) ;
          print_port(foco5, estadofoco6) ;
          print_port(foco6, estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 2,3,5,6,7: Con confirmación --118
    if (luz_23567 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco2=!estadofoco2;
          estadofoco3=!estadofoco3;
          estadofoco5=!estadofoco5;
          estadofoco6=!estadofoco6;
          estadofoco7=!estadofoco7;
          print_port(foco1, estadofoco2) ;
          print_port(foco2, estadofoco3) ;
          print_port(foco4, estadofoco5) ;
          print_port(foco5, estadofoco6) ;
          print_port(foco6, estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,2,3,5,6,7: Con confirmación --119
    if (luz_123567 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco2=!estadofoco2;
          estadofoco3=!estadofoco3;
          estadofoco5=!estadofoco5;
          estadofoco6=!estadofoco6;
          estadofoco7=!estadofoco7;
          print_port(foco0, estadofoco1) ;
          print_port(foco1, estadofoco2) ;
          print_port(foco2, estadofoco3) ;
          print_port(foco4, estadofoco5) ;
          print_port(foco5, estadofoco6) ;
          print_port(foco6, estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
   }

   //Luz # 4,5,6,7: Con confirmación --120
    if (luz_4567 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco4=!estadofoco4;
          estadofoco5=!estadofoco5;
          estadofoco6=!estadofoco6;
          estadofoco7=!estadofoco7;
          print_port(foco3,estadofoco4);
          print_port(foco4,estadofoco5);
          print_port(foco5,estadofoco6);
          print_port(foco6,estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

   /*****************************************************************************************************/
   //Luz # 1,4,5,6,7: Con confirmación --121
    if (luz_14567 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco4=!estadofoco4;
          estadofoco5=!estadofoco5;
          estadofoco6=!estadofoco6;
          estadofoco7=!estadofoco7;
          print_port(foco0,estadofoco1);
          print_port(foco3,estadofoco4);
          print_port(foco4,estadofoco5);
          print_port(foco5,estadofoco6);
          print_port(foco6,estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
   }

    //Luz # 2,4,5,6,7: Con confirmación --122
    if (luz_24567 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco2=!estadofoco2;
          estadofoco4=!estadofoco4;
          estadofoco5=!estadofoco5;
          estadofoco6=!estadofoco6;
          estadofoco7=!estadofoco7;
          print_port(foco1,estadofoco2);
          print_port(foco3,estadofoco4);
          print_port(foco4,estadofoco5);
          print_port(foco5,estadofoco6);
          print_port(foco6,estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
   }

   //Luz # 1,2,4,5,6,7: Con confirmación --123
    if (luz_124567 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco2=!estadofoco2;
          estadofoco4=!estadofoco4;
          estadofoco5=!estadofoco5;
          estadofoco6=!estadofoco6;
          estadofoco7=!estadofoco7;
          print_port(foco1,estadofoco2);
          print_port(foco3,estadofoco4);
          print_port(foco4,estadofoco5);
          print_port(foco5,estadofoco6);
          print_port(foco6,estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
   }

    //Luz # 3,4,5,6,7: Con confirmación --124
    if (luz_34567 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco3=!estadofoco3;
          estadofoco4=!estadofoco4;
          estadofoco5=!estadofoco5;
          estadofoco6=!estadofoco6;
          estadofoco7=!estadofoco7;
          print_port(foco2,estadofoco3);
          print_port(foco3,estadofoco4);
          print_port(foco4,estadofoco5);
          print_port(foco5,estadofoco6);
          print_port(foco6,estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

    //Luz # 1,3,4,5,6,7: Con confirmación --125
    if (luz_134567 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco3=!estadofoco3;
          estadofoco4=!estadofoco4;
          estadofoco5=!estadofoco5;
          estadofoco6=!estadofoco6;
          estadofoco7=!estadofoco7;
          print_port(foco0,estadofoco1);
          print_port(foco2,estadofoco3);
          print_port(foco3,estadofoco4);
          print_port(foco4,estadofoco5);
          print_port(foco5,estadofoco6);
          print_port(foco6,estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 2,3,4,5,6,7: Con confirmación --126
    if (luz_234567 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco2=!estadofoco2;
          estadofoco3=!estadofoco3;
          estadofoco4=!estadofoco4;
          estadofoco5=!estadofoco5;
          estadofoco6=!estadofoco6;
          estadofoco7=!estadofoco7;
          print_port(foco1,estadofoco2);
          print_port(foco2,estadofoco3);
          print_port(foco3,estadofoco4);
          print_port(foco4,estadofoco5);
          print_port(foco5,estadofoco6);
          print_port(foco6,estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,2,3,4,5,6,7: Con confirmación --127
    if (luz_1234567 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco2=!estadofoco2;
          estadofoco3=!estadofoco3;
          estadofoco4=!estadofoco4;
          estadofoco5=!estadofoco5;
          estadofoco6=!estadofoco6;
          estadofoco7=!estadofoco7;
          print_port(foco0,estadofoco1);
          print_port(foco1,estadofoco2);
          print_port(foco2,estadofoco3);
          print_port(foco3,estadofoco4);
          print_port(foco4,estadofoco5);
          print_port(foco5,estadofoco6);
          print_port(foco6,estadofoco7) ;
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

    //Luz # 1,8: Con confirmación --129
    if (luz_18 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco8=!estadofoco8;
          print_port(foco0,estadofoco1) ;
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
   }

    //Luz # 2,8: Con confirmación --130
    if (luz_28 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco2=!estadofoco2;
          estadofoco8=!estadofoco8;
          print_port(foco1,estadofoco2) ;
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

   /*****************************************************************************************************/

    //Luz # 1,2,8: Con confirmación --131
    if (luz_128 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco2=!estadofoco2;
          estadofoco8=!estadofoco8;
          print_port(foco0,estadofoco1);
          print_port(foco1,estadofoco2);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 3,8: Con confirmación --132
    if (luz_38 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco3=!estadofoco3;
          estadofoco8=!estadofoco8;
          print_port(foco2,estadofoco3) ;
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,3,8: Con confirmación --133
    if (luz_138 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco3=!estadofoco3;
          estadofoco8=!estadofoco8;
          print_port(foco0,estadofoco1);
          print_port(foco2,estadofoco3);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
   }

   //Luz # 2,3,8: Con confirmación --134
    if (luz_238 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco2=!estadofoco2;
          estadofoco3=!estadofoco3;
          estadofoco8=!estadofoco8;
          print_port(foco1,estadofoco2) ;
          print_port(foco2,estadofoco3) ;
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,2,3,8: Con confirmación --135
    if (luz_1238 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco2=!estadofoco2;
          estadofoco3=!estadofoco3;
          estadofoco8=!estadofoco8;
          print_port(foco0,estadofoco1) ;
          print_port(foco1,estadofoco2) ;
          print_port(foco2,estadofoco3) ;
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 4,8: Con confirmación --136
    if (luz_48 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco4=!estadofoco4;
          estadofoco8=!estadofoco8;
          print_port(foco3,estadofoco4) ;
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,4,8: Con confirmación --137
    if (luz_148 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco4=!estadofoco4;
          estadofoco8=!estadofoco8;
          print_port(foco0,estadofoco1) ;
          print_port(foco3,estadofoco4) ;
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 2,4,8: Con confirmación --138
    if (luz_248 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco2=!estadofoco2;
          estadofoco4=!estadofoco4;
          estadofoco8=!estadofoco8;
          print_port(foco1,estadofoco2) ;
          print_port(foco3,estadofoco4) ;
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,2,4,8: Con confirmación --139
    if (luz_1248 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco2=!estadofoco2;
          estadofoco4=!estadofoco4;
          estadofoco8=!estadofoco8;
          print_port(foco0,estadofoco1);
          print_port(foco1,estadofoco2);
          print_port(foco3,estadofoco4);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 3,4,8: Con confirmación --140
    if (luz_348 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco3=!estadofoco3;
          estadofoco4=!estadofoco4;
          estadofoco8=!estadofoco8;
          print_port(foco2,estadofoco3) ;
          print_port(foco3,estadofoco4) ;
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

    /******************************************************************************************************/

     //Luz # 1,3,4,8: Con confirmación --141
    if (luz_1348 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco3=!estadofoco3;
          estadofoco4=!estadofoco4;
          estadofoco8=!estadofoco8;
          print_port(foco0,estadofoco1);
          print_port(foco2,estadofoco3);
          print_port(foco3,estadofoco4);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 2,3,4,8: Con confirmación --142
    if (luz_2348 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco2=!estadofoco2;
          estadofoco3=!estadofoco3;
          estadofoco4=!estadofoco4;
          estadofoco8=!estadofoco8;
          print_port(foco1,estadofoco2) ;
          print_port(foco2,estadofoco3) ;
          print_port(foco3,estadofoco4) ;
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,2,3,4,8: Con confirmación --143
    if (luz_12348 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco2=!estadofoco2;
          estadofoco3=!estadofoco3;
          estadofoco4=!estadofoco4;
          estadofoco8=!estadofoco8;
          print_port(foco0,estadofoco1);
          print_port(foco1,estadofoco2);
          print_port(foco2,estadofoco3);
          print_port(foco3,estadofoco4);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 5,8: Con confirmación --144
    if (luz_58 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco5=!estadofoco5;
          estadofoco8=!estadofoco8;
          print_port(foco4,estadofoco5) ;
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,5,8: Con confirmación --145
    if (luz_158 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco5=!estadofoco5;
          estadofoco8=!estadofoco8;
          print_port(foco0,estadofoco1) ;
          print_port(foco4,estadofoco5) ;
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 2,5,8: Con confirmación --146
    if (luz_258 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco2=!estadofoco2;
          estadofoco5=!estadofoco5;
          estadofoco8=!estadofoco8;
          print_port(foco1,estadofoco2);
          print_port(foco4,estadofoco5);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,2,5,8: Con confirmación --147
    if (luz_1258 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco2=!estadofoco2;
          estadofoco5=!estadofoco5;
          estadofoco8=!estadofoco8;
          print_port(foco0,estadofoco1);
          print_port(foco1,estadofoco2);
          print_port(foco4,estadofoco5);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 3,5,8: Con confirmación --148
    if (luz_358 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco3=!estadofoco3;
          estadofoco5=!estadofoco5;
          estadofoco8=!estadofoco8;
          print_port(foco2,estadofoco3);
          print_port(foco4,estadofoco5);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,3,5,8: Con confirmación --149
    if (luz_1358 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco3=!estadofoco3;
          estadofoco5=!estadofoco5;
          estadofoco8=!estadofoco8;
          print_port(foco0,estadofoco1);
          print_port(foco2,estadofoco3);
          print_port(foco4,estadofoco5);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 2,3,5,8: Con confirmación --150
    if (luz_2358 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco2=!estadofoco2;
          estadofoco3=!estadofoco3;
          estadofoco5=!estadofoco5;
          estadofoco8=!estadofoco8;
          print_port(foco1,estadofoco2);
          print_port(foco2,estadofoco3);
          print_port(foco4,estadofoco5);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

   /*****************************************************************************************************/

    //Luz # 1,2,3,5,8: Con confirmación --151
    if (luz_12358 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco2=!estadofoco2;
          estadofoco3=!estadofoco3;
          estadofoco5=!estadofoco5;
          estadofoco8=!estadofoco8;
          print_port(foco0,estadofoco1);
          print_port(foco1,estadofoco2);
          print_port(foco2,estadofoco3);
          print_port(foco4,estadofoco5);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 4,5,8: Con confirmación --152
    if (luz_458 >= 0){
    for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco4=!estadofoco4;
          estadofoco5=!estadofoco5;
          estadofoco8=!estadofoco8;
          print_port(foco3,estadofoco4);
          print_port(foco4,estadofoco5);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,4,5,8: Con confirmación --153
    if (luz_1458 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco4=!estadofoco4;
          estadofoco5=!estadofoco5;
          estadofoco8=!estadofoco8;
          print_port(foco0,estadofoco1);
          print_port(foco3,estadofoco4);
          print_port(foco4,estadofoco5);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 2,4,5,8: Con confirmación --154
    if (luz_2458 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco2=!estadofoco2;
          estadofoco4=!estadofoco4;
          estadofoco5=!estadofoco5;
          estadofoco8=!estadofoco8;
          print_port(foco1,estadofoco2) ;
          print_port(foco3,estadofoco4) ;
          print_port(foco4,estadofoco5) ;
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,2,4,5,8: Con confirmación --155
    if (luz_12458 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco2=!estadofoco2;
          estadofoco4=!estadofoco4;
          estadofoco5=!estadofoco5;
          estadofoco8=!estadofoco8;
          print_port(foco0,estadofoco1);
          print_port(foco1,estadofoco2);
          print_port(foco3,estadofoco4);
          print_port(foco4,estadofoco5);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

   //Luz # 3,4,5,8: Con confirmación --156
    if (luz_3458 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco3=!estadofoco3;
          estadofoco4=!estadofoco4;
          estadofoco5=!estadofoco5;
          estadofoco8=!estadofoco8;
          print_port(foco2,estadofoco3);
          print_port(foco3,estadofoco4);
          print_port(foco4,estadofoco5);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,3,4,5,8: Con confirmación --157
    if (luz_13458 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco3=!estadofoco3;
          estadofoco4=!estadofoco4;
          estadofoco5=!estadofoco5;
          estadofoco8=!estadofoco8;
          print_port(foco0,estadofoco1);
          print_port(foco2,estadofoco3);
          print_port(foco3,estadofoco4);
          print_port(foco4,estadofoco5);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 2,3,4,5,8: Con confirmación --158
    if (luz_23458 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco2=!estadofoco2;
          estadofoco3=!estadofoco3;
          estadofoco4=!estadofoco4;
          estadofoco5=!estadofoco5;
          estadofoco8=!estadofoco8;
          print_port(foco1,estadofoco2);
          print_port(foco2,estadofoco3);
          print_port(foco3,estadofoco4);
          print_port(foco4,estadofoco5);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,2,3,4,5,8: Con confirmación --159
    if (luz_123458 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco2=!estadofoco2;
          estadofoco3=!estadofoco3;
          estadofoco4=!estadofoco4;
          estadofoco5=!estadofoco5;
          estadofoco8=!estadofoco8;
          print_port(foco0,estadofoco1);
          print_port(foco1,estadofoco2);
          print_port(foco2,estadofoco3);
          print_port(foco3,estadofoco4);
          print_port(foco4,estadofoco5);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 6,8: Con confirmación --160
    if (luz_68 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco6=!estadofoco6;
          estadofoco8=!estadofoco8;
          print_port(foco5,estadofoco6) ;
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

   /****************************************************************************************************/

  //Luz # 1,6,8: Con confirmación --161
    if (luz_168 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco6=!estadofoco6;
          estadofoco8=!estadofoco8;
          print_port(foco0,estadofoco1);
          print_port(foco5,estadofoco6);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 2,6,8: Con confirmación --162
    if (luz_268 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco2=!estadofoco2;
          estadofoco6=!estadofoco6;
          estadofoco8=!estadofoco8;
          print_port(foco1,estadofoco2);
          print_port(foco5,estadofoco6);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,2,6,8: Con confirmación --163
    if (luz_1268 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco2=!estadofoco2;
          estadofoco6=!estadofoco6;
          estadofoco8=!estadofoco8;
          print_port(foco0,estadofoco1);
          print_port(foco1,estadofoco2);
          print_port(foco5,estadofoco6);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 3,6,8: Con confirmación --164
    if (luz_368 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco3=!estadofoco3;
          estadofoco6=!estadofoco6;
          estadofoco8=!estadofoco8;
          print_port(foco2,estadofoco3);
          print_port(foco5,estadofoco6);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,3,6,8: Con confirmación --165
    if (luz_1368 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco3=!estadofoco3;
          estadofoco6=!estadofoco6;
          estadofoco8=!estadofoco8;
          print_port(foco0,estadofoco1);
          print_port(foco2,estadofoco3);
          print_port(foco5,estadofoco6);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 2,3,6,8: Con confirmación --166
    if (luz_2368 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco2=!estadofoco2;
          estadofoco3=!estadofoco3;
          estadofoco6=!estadofoco6;
          estadofoco8=!estadofoco8;
          print_port(foco1,estadofoco2);
          print_port(foco2,estadofoco3);
          print_port(foco5,estadofoco6);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,2,3,6,8: Con confirmación --167
    if (luz_12368 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco2=!estadofoco2;
          estadofoco3=!estadofoco3;
          estadofoco6=!estadofoco6;
          estadofoco8=!estadofoco8;
          print_port(foco0,estadofoco1);
          print_port(foco1,estadofoco2);
          print_port(foco2,estadofoco3);
          print_port(foco5,estadofoco6);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 4,6,8: Con confirmación --168
    if (luz_468 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco4=!estadofoco4;
          estadofoco6=!estadofoco6;
          estadofoco8=!estadofoco8;
          print_port(foco3,estadofoco4) ;
          print_port(foco5,estadofoco6) ;
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,4,6,8: Con confirmación --169
    if (luz_1468 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco4=!estadofoco4;
          estadofoco6=!estadofoco6;
          estadofoco8=!estadofoco8;
          print_port(foco0,estadofoco1);
          print_port(foco3,estadofoco4);
          print_port(foco5,estadofoco6);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 2,4,6,8: Con confirmación --170
    if (luz_2468 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco2=!estadofoco2;
          estadofoco4=!estadofoco4;
          estadofoco6=!estadofoco6;
          estadofoco8=!estadofoco8;
          print_port(foco1,estadofoco2) ;
          print_port(foco3,estadofoco4) ;
          print_port(foco5,estadofoco6) ;
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

     /*************************************************************************************************/

    //Luz # 1,2,4,6,8: Con confirmación --171
    if (luz_12468 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco2=!estadofoco2;
          estadofoco4=!estadofoco4;
          estadofoco6=!estadofoco6;
          estadofoco8=!estadofoco8;
          print_port(foco0,estadofoco1);
          print_port(foco1,estadofoco2);
          print_port(foco3,estadofoco4);
          print_port(foco5,estadofoco6);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 3,4,6,8: Con confirmación --172
    if (luz_3468 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco3=!estadofoco3;
          estadofoco4=!estadofoco4;
          estadofoco6=!estadofoco6;
          estadofoco8=!estadofoco8;
          print_port(foco2,estadofoco3);
          print_port(foco3,estadofoco4);
          print_port(foco5,estadofoco6);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

    //Luz # 1,3,4,6,8: Con confirmación --173
    if (luz_13468 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco3=!estadofoco3;
          estadofoco4=!estadofoco4;
          estadofoco6=!estadofoco6;
          estadofoco8=!estadofoco8;
          print_port(foco0,estadofoco1) ;
          print_port(foco2,estadofoco3) ;
          print_port(foco3,estadofoco4) ;
          print_port(foco5,estadofoco6) ;
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 2,3,4,6,8: Con confirmación --174
    if (luz_23468 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco2=!estadofoco2;
          estadofoco3=!estadofoco3;
          estadofoco4=!estadofoco4;
          estadofoco6=!estadofoco6;
          estadofoco8=!estadofoco8;
          print_port(foco1,estadofoco2) ;
          print_port(foco2,estadofoco3) ;
          print_port(foco3,estadofoco4) ;
          print_port(foco5,estadofoco6) ;
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,2,3,4,6,8: Con confirmación --175
    if (luz_123468 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco2=!estadofoco2;
          estadofoco3=!estadofoco3;
          estadofoco4=!estadofoco4;
          estadofoco6=!estadofoco6;
          estadofoco8=!estadofoco8;
          print_port(foco0,estadofoco1);
          print_port(foco1,estadofoco2);
          print_port(foco2,estadofoco3);
          print_port(foco3,estadofoco4);
          print_port(foco5,estadofoco6);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 5,6,8: Con confirmación --176
    if (luz_568 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco5=!estadofoco5;
          estadofoco6=!estadofoco6;
          estadofoco8=!estadofoco8;
          print_port(foco4,estadofoco5);
          print_port(foco5,estadofoco6);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,5,6,8: Con confirmación --177
    if (luz_1568 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco5=!estadofoco5;
          estadofoco6=!estadofoco6;
          estadofoco8=!estadofoco8;
          print_port(foco0,estadofoco1);
          print_port(foco4,estadofoco5);
          print_port(foco5,estadofoco6);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 2,5,6,8: Con confirmación --178
    if (luz_2568 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco2=!estadofoco2;
          estadofoco5=!estadofoco5;
          estadofoco6=!estadofoco6;
          estadofoco8=!estadofoco8;
          print_port(foco1,estadofoco2);
          print_port(foco4,estadofoco5);
          print_port(foco5,estadofoco6);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,2,5,6,8: Con confirmación --179
    if (luz_12568 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco2=!estadofoco2;
          estadofoco5=!estadofoco5;
          estadofoco6=!estadofoco6;
          estadofoco8=!estadofoco8;
          print_port(foco0,estadofoco1);
          print_port(foco1,estadofoco2);
          print_port(foco4,estadofoco5);
          print_port(foco5,estadofoco6);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }


    //Luz # 3,5,6,8: Con confirmación --180
    if (luz_3568 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco3=!estadofoco3;
          estadofoco5=!estadofoco5;
          estadofoco6=!estadofoco6;
          estadofoco8=!estadofoco8;
          print_port(foco2,estadofoco3);
          print_port(foco4,estadofoco5);
          print_port(foco5,estadofoco6);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

    /****************************************************************************************************/

    //Luz # 1,3,5,6,8: Con confirmación --181
    if (luz_13568 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco3=!estadofoco3;
          estadofoco5=!estadofoco5;
          estadofoco6=!estadofoco6;
          estadofoco8=!estadofoco8;
          print_port(foco0,estadofoco1);
          print_port(foco2,estadofoco3);
          print_port(foco4,estadofoco5);
          print_port(foco5,estadofoco6);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 2,3,5,6,8: Con confirmación --182
    if (luz_23568 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco2=!estadofoco2;
          estadofoco3=!estadofoco3;
          estadofoco5=!estadofoco5;
          estadofoco6=!estadofoco6;
          estadofoco8=!estadofoco8;
          print_port(foco1,estadofoco2);
          print_port(foco2,estadofoco3);
          print_port(foco4,estadofoco5);
          print_port(foco5,estadofoco6);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

   //Luz # 1,2,3,5,6,8: Con confirmación --183
    if (luz_123568 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco2=!estadofoco2;
          estadofoco3=!estadofoco3;
          estadofoco5=!estadofoco5;
          estadofoco6=!estadofoco6;
          estadofoco8=!estadofoco8;
          print_port(foco0,estadofoco1);
          print_port(foco1,estadofoco2);
          print_port(foco2,estadofoco3);
          print_port(foco4,estadofoco5);
          print_port(foco5,estadofoco6);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 4,5,6,8: Con confirmación --184
    if (luz_4568 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco4=!estadofoco4;
          estadofoco5=!estadofoco5;
          estadofoco6=!estadofoco6;
          estadofoco8=!estadofoco8;
          print_port(foco3,estadofoco4);
          print_port(foco4,estadofoco5);
          print_port(foco5,estadofoco6);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,4,5,6,8: Con confirmación --185
    if (luz_14568 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco4=!estadofoco4;
          estadofoco5=!estadofoco5;
          estadofoco6=!estadofoco6;
          estadofoco8=!estadofoco8;
          print_port(foco0,estadofoco1);
          print_port(foco3,estadofoco4);
          print_port(foco4,estadofoco5);
          print_port(foco5,estadofoco6);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 2,4,5,6,8: Con confirmación --186
    if (luz_24568 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco2=!estadofoco2;
          estadofoco4=!estadofoco4;
          estadofoco5=!estadofoco5;
          estadofoco6=!estadofoco6;
          estadofoco8=!estadofoco8;
          print_port(foco1,estadofoco2);
          print_port(foco3,estadofoco4);
          print_port(foco4,estadofoco5);
          print_port(foco5,estadofoco6);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,2,4,5,6,8: Con confirmación --187
    if (luz_124568 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco2=!estadofoco2;
          estadofoco4=!estadofoco4;
          estadofoco5=!estadofoco5;
          estadofoco6=!estadofoco6;
          estadofoco8=!estadofoco8;
          print_port(foco0,estadofoco1);
          print_port(foco1,estadofoco2);
          print_port(foco3,estadofoco4);
          print_port(foco4,estadofoco5);
          print_port(foco5,estadofoco6);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
 }

  //Luz # 3,4,5,6,8: Con confirmación --188
    if (luz_34568 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco3=!estadofoco3;
          estadofoco4=!estadofoco4;
          estadofoco5=!estadofoco5;
          estadofoco6=!estadofoco6;
          estadofoco8=!estadofoco8;
          print_port(foco2,estadofoco3);
          print_port(foco3,estadofoco4);
          print_port(foco4,estadofoco5);
          print_port(foco5,estadofoco6);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,3,4,5,6,8: Con confirmación --189
    if (luz_134568 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco3=!estadofoco3;
          estadofoco4=!estadofoco4;
          estadofoco5=!estadofoco5;
          estadofoco6=!estadofoco6;
          estadofoco8=!estadofoco8;
          print_port(foco0,estadofoco1);
          print_port(foco2,estadofoco3);
          print_port(foco3,estadofoco4);
          print_port(foco4,estadofoco5);
          print_port(foco5,estadofoco6);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 2,3,4,5,6,8: Con confirmación --190
    if (luz_234568 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco2=!estadofoco2;
          estadofoco3=!estadofoco3;
          estadofoco4=!estadofoco4;
          estadofoco5=!estadofoco5;
          estadofoco6=!estadofoco6;
          estadofoco8=!estadofoco8;
          print_port(foco1,estadofoco2);
          print_port(foco2,estadofoco3);
          print_port(foco3,estadofoco4);
          print_port(foco4,estadofoco5);
          print_port(foco5,estadofoco6);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

   /*****************************************************************************************************/

    //Luz # 1,2,3,4,5,6,8: Con confirmación --191
    if (luz_1234568 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco2=!estadofoco2;
          estadofoco3=!estadofoco3;
          estadofoco4=!estadofoco4;
          estadofoco5=!estadofoco5;
          estadofoco6=!estadofoco6;
          estadofoco8=!estadofoco8;
          print_port(foco0,estadofoco1);
          print_port(foco1,estadofoco2);
          print_port(foco2,estadofoco3);
          print_port(foco3,estadofoco4);
          print_port(foco4,estadofoco5);
          print_port(foco5,estadofoco6);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 7,8: Con confirmación --192
    if (luz_78 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco7=!estadofoco7;
          estadofoco8=!estadofoco8;
          print_port(foco6,estadofoco7) ;
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,7,8: Con confirmación --193
    if (luz_178 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco7=!estadofoco7;
          estadofoco8=!estadofoco8;
          print_port(foco0,estadofoco1) ;
          print_port(foco6,estadofoco7) ;
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

    //Luz # 2,7,8: Con confirmación --194
    if (luz_278 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco2=!estadofoco2;
          estadofoco7=!estadofoco7;
          estadofoco8=!estadofoco8;
          print_port(foco1,estadofoco2);
          print_port(foco6,estadofoco7);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,2,7,8: Con confirmación --195
    if (luz_1278 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco2=!estadofoco2;
          estadofoco7=!estadofoco7;
          estadofoco8=!estadofoco8;
          print_port(foco0,estadofoco1) ;
          print_port(foco1,estadofoco2) ;
          print_port(foco6,estadofoco7) ;
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 3,7,8: Con confirmación --196
    if (luz_378 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco3=!estadofoco3;
          estadofoco7=!estadofoco7;
          estadofoco8=!estadofoco8;
          print_port(foco2,estadofoco3);
          print_port(foco6,estadofoco7);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,3,7,8: Con confirmación --197
    if (luz_1378 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco3=!estadofoco3;
          estadofoco7=!estadofoco7;
          estadofoco8=!estadofoco8;
          print_port(foco0,estadofoco1);
          print_port(foco2,estadofoco3);
          print_port(foco6,estadofoco7);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 2,3,7,8: Con confirmación --198
    if (luz_2378 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco2=!estadofoco2;
          estadofoco3=!estadofoco3;
          estadofoco7=!estadofoco7;
          estadofoco8=!estadofoco8;
          print_port(foco1,estadofoco2);
          print_port(foco2,estadofoco3);
          print_port(foco6,estadofoco7);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,2,3,7,8: Con confirmación --199
    if (luz_12378 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco2=!estadofoco2;
          estadofoco3=!estadofoco3;
          estadofoco7=!estadofoco7;
          estadofoco8=!estadofoco8;
          print_port(foco0,estadofoco1);
          print_port(foco1,estadofoco2);
          print_port(foco2,estadofoco3);
          print_port(foco6,estadofoco7);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 4,7,8: Con confirmación --200
    if (luz_478 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco4=!estadofoco4;
          estadofoco7=!estadofoco7;
          estadofoco8=!estadofoco8;
          print_port(foco3,estadofoco4) ;
          print_port(foco6,estadofoco7) ;
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

    /****************************************************************************************************/

    //Luz # 1,4,7,8: Con confirmación --201
    if (luz_1478 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco4=!estadofoco4;
          estadofoco7=!estadofoco7;
          estadofoco8=!estadofoco8;
          print_port(foco0,estadofoco1);
          print_port(foco3,estadofoco4);
          print_port(foco6,estadofoco7);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 2,4,7,8: Con confirmación --202
    if (luz_2478 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco2=!estadofoco2;
          estadofoco4=!estadofoco4;
          estadofoco7=!estadofoco7;
          estadofoco8=!estadofoco8;
          print_port(foco1,estadofoco2);
          print_port(foco3,estadofoco4);
          print_port(foco6,estadofoco7);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,2,4,7,8: Con confirmación --203
    if (luz_12478 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco2=!estadofoco2;
          estadofoco4=!estadofoco4;
          estadofoco7=!estadofoco7;
          estadofoco8=!estadofoco8;
          print_port(foco0,estadofoco1);
          print_port(foco1,estadofoco2);
          print_port(foco3,estadofoco4);
          print_port(foco6,estadofoco7);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 3,4,7,8: Con confirmación --204
    if (luz_3478 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco3=!estadofoco3;
          estadofoco4=!estadofoco4;
          estadofoco7=!estadofoco7;
          estadofoco8=!estadofoco8;
          print_port(foco2,estadofoco3);
          print_port(foco3,estadofoco4);
          print_port(foco6,estadofoco7);
          print_port(foco7,estadofoco8);

          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,3,4,7,8: Con confirmación --205
    if (luz_13478 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco3=!estadofoco3;
          estadofoco4=!estadofoco4;
          estadofoco7=!estadofoco7;
          estadofoco8=!estadofoco8;
          print_port(foco0,estadofoco1);
          print_port(foco2,estadofoco3);
          print_port(foco3,estadofoco4);
          print_port(foco6,estadofoco7);
          print_port(foco7,estadofoco8);

          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

    //Luz # 2,3,4,7,8: Con confirmación --206
    if (luz_23478 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco2=!estadofoco2;
          estadofoco3=!estadofoco3;
          estadofoco4=!estadofoco4;
          estadofoco7=!estadofoco7;
          estadofoco8=!estadofoco8;
          print_port(foco1,estadofoco2) ;
          print_port(foco2,estadofoco3) ;
          print_port(foco3,estadofoco4) ;
          print_port(foco6,estadofoco7) ;
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,2,3,4,7,8: Con confirmación --207
    if (luz_123478 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco1=!estadofoco1;
          estadofoco2=!estadofoco2;
          estadofoco3=!estadofoco3;
          estadofoco4=!estadofoco4;
          estadofoco7=!estadofoco7;
          estadofoco8=!estadofoco8;
          print_port(foco0,estadofoco1);
          print_port(foco1,estadofoco2);
          print_port(foco2,estadofoco3);
          print_port(foco3,estadofoco4);
          print_port(foco6,estadofoco7);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

   //Luz # 5,7,8: Con confirmación --208
    if (luz_578 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
          estadofoco5=!estadofoco5;
          estadofoco7=!estadofoco7;
          estadofoco8=!estadofoco8;
          print_port(foco4,estadofoco5);
          print_port(foco6,estadofoco7);
          print_port(foco7,estadofoco8);
          //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
          mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
          //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,5,7,8: Con confirmación --209
    if (luz_1578 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco1=!estadofoco1;
        estadofoco5=!estadofoco5;
        estadofoco7=!estadofoco7;
        estadofoco8=!estadofoco8;
        print_port(foco0,estadofoco1) ;
        print_port(foco4,estadofoco5) ;
        print_port(foco6,estadofoco7) ;
        print_port(foco7,estadofoco8);
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 2,5,7,8: Con confirmación --210
    if (luz_2578 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco2=!estadofoco2;
        estadofoco5=!estadofoco5;
        estadofoco7=!estadofoco7;
        estadofoco8=!estadofoco8;
        print_port(foco1,estadofoco2);
        print_port(foco4,estadofoco5);
        print_port(foco6,estadofoco7);
        print_port(foco7,estadofoco8);
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

     /**************************************************************************************************/

    //Luz # 1,2,5,7,8: Con confirmación --211
    if (luz_12578 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco1=!estadofoco1;
        estadofoco2=!estadofoco2;
        estadofoco5=!estadofoco5;
        estadofoco7=!estadofoco7;
        estadofoco8=!estadofoco8;
        print_port(foco0,estadofoco1);
        print_port(foco1,estadofoco2);
        print_port(foco4,estadofoco5);
        print_port(foco6,estadofoco7);
        print_port(foco7,estadofoco8);
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 3,5,7,8: Con confirmación --212
    if (luz_3578 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco3=!estadofoco3;
        estadofoco5=!estadofoco5;
        estadofoco7=!estadofoco7;
        estadofoco8=!estadofoco8;
        print_port(foco2,estadofoco3);
        print_port(foco4,estadofoco5);
        print_port(foco6,estadofoco7);
        print_port(foco7,estadofoco8);
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,3,5,7,8: Con confirmación --213
    if (luz_13578 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco1=!estadofoco1;
        estadofoco3=!estadofoco3;
        estadofoco5=!estadofoco5;
        estadofoco7=!estadofoco7;
        estadofoco8=!estadofoco8;
        print_port(foco0,estadofoco1);
        print_port(foco2,estadofoco3);
        print_port(foco4,estadofoco5);
        print_port(foco6,estadofoco7);
        print_port(foco7,estadofoco8);
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 2,3,5,7,8: Con confirmación --214
    if (luz_23578 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco2=!estadofoco2;
        estadofoco3=!estadofoco3;
        estadofoco5=!estadofoco5;
        estadofoco7=!estadofoco7;
        estadofoco8=!estadofoco8;
        print_port(foco1,estadofoco2);
        print_port(foco2,estadofoco3);
        print_port(foco4,estadofoco5);
        print_port(foco6,estadofoco7);
        print_port(foco7,estadofoco8);
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,2,3,5,7,8: Con confirmación --215
    if (luz_123578 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco1=!estadofoco1;
        estadofoco2=!estadofoco2;
        estadofoco3=!estadofoco3;
        estadofoco5=!estadofoco5;
        estadofoco7=!estadofoco7;
        estadofoco8=!estadofoco8;
        print_port(foco0,estadofoco1);
        print_port(foco1,estadofoco2);
        print_port(foco2,estadofoco3);
        print_port(foco4,estadofoco5);
        print_port(foco6,estadofoco7);
        print_port(foco7,estadofoco8);
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

    //Luz # 4,5,7,8: Con confirmación --216
    if (luz_4578 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco4=!estadofoco4;
        estadofoco5=!estadofoco5;
        estadofoco7=!estadofoco7;
        estadofoco8=!estadofoco8;
        print_port(foco3,estadofoco4);
        print_port(foco4,estadofoco5);
        print_port(foco6,estadofoco7);
        print_port(foco7,estadofoco8);
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,4,5,7,8: Con confirmación --217
    if (luz_14578 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco1=!estadofoco1;
        estadofoco4=!estadofoco4;
        estadofoco5=!estadofoco5;
        estadofoco7=!estadofoco7;
        estadofoco8=!estadofoco8;
        print_port(foco0,estadofoco1);
        print_port(foco3,estadofoco4);
        print_port(foco4,estadofoco5);
        print_port(foco6,estadofoco7);
        print_port(foco7,estadofoco8);
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

   //Luz # 2,4,5,7,8: Con confirmación --218
    if (luz_24578 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco2=!estadofoco2;
        estadofoco4=!estadofoco4;
        estadofoco5=!estadofoco5;
        estadofoco7=!estadofoco7;
        estadofoco8=!estadofoco8;
        print_port(foco1,estadofoco2) ;
        print_port(foco3,estadofoco4) ;
        print_port(foco4,estadofoco5) ;
        print_port(foco6,estadofoco7) ;
        print_port(foco7,estadofoco8);
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,2,4,5,7,8: Con confirmación --219
    if (luz_124578 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco1=!estadofoco1;
        estadofoco2=!estadofoco2;
        estadofoco4=!estadofoco4;
        estadofoco5=!estadofoco5;
        estadofoco7=!estadofoco7;
        estadofoco8=!estadofoco8;
        print_port(foco0,estadofoco1);
        print_port(foco1,estadofoco2);
        print_port(foco3,estadofoco4);
        print_port(foco4,estadofoco5);
        print_port(foco6,estadofoco7);
        print_port(foco7,estadofoco8);
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 3,4,5,7,8: Con confirmación --220
    if (luz_34578 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco3=!estadofoco3;
        estadofoco4=!estadofoco4;
        estadofoco5=!estadofoco5;
        estadofoco7=!estadofoco7;
        estadofoco8=!estadofoco8;
        print_port(foco2,estadofoco3);
        print_port(foco3,estadofoco4);
        print_port(foco4,estadofoco5);
        print_port(foco6,estadofoco7);
        print_port(foco7,estadofoco8);
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

   /***************************************************************************************************/
    //Luz # 1,3,4,5,7,8: Con confirmación --221
    if (luz_134578 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco1=!estadofoco1;
        estadofoco3=!estadofoco3;
        estadofoco4=!estadofoco4;
        estadofoco5=!estadofoco5;
        estadofoco7=!estadofoco7;
        estadofoco8=!estadofoco8;
        print_port(foco0,estadofoco1) ;
        print_port(foco2,estadofoco3) ;
        print_port(foco3,estadofoco4) ;
        print_port(foco4,estadofoco5) ;
        print_port(foco6,estadofoco7) ;
        print_port(foco7,estadofoco8);
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 2,3,4,5,7,8: Con confirmación --222
    if (luz_234578 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco2=!estadofoco2;
        estadofoco3=!estadofoco3;
        estadofoco4=!estadofoco4;
        estadofoco5=!estadofoco5;
        estadofoco7=!estadofoco7;
        estadofoco8=!estadofoco8;
        print_port(foco1,estadofoco2);
        print_port(foco2,estadofoco3);
        print_port(foco3,estadofoco4);
        print_port(foco4,estadofoco5);
        print_port(foco6,estadofoco7);
        print_port(foco7,estadofoco8);
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,2,3,4,5,7,8: Con confirmación --223
    if (luz_1234578 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco1=!estadofoco1;
        estadofoco2=!estadofoco2;
        estadofoco3=!estadofoco3;
        estadofoco4=!estadofoco4;
        estadofoco5=!estadofoco5;
        estadofoco7=!estadofoco7;
        estadofoco8=!estadofoco8;
        print_port(foco0,estadofoco1);
        print_port(foco1,estadofoco2);
        print_port(foco2,estadofoco3);
        print_port(foco3,estadofoco4);
        print_port(foco4,estadofoco5);
        print_port(foco6,estadofoco7);
        print_port(foco7,estadofoco8);
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 6,7,8: Con confirmación --224
    if (luz_678 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco6=!estadofoco6;
        estadofoco7=!estadofoco7;
        estadofoco8=!estadofoco8;
        print_port(foco5,estadofoco6);
        print_port(foco6,estadofoco7);
        print_port(foco7,estadofoco8);
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,6,7,8: Con confirmación --225
    if (luz_1678 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco1=!estadofoco1;
        estadofoco6=!estadofoco6;
        estadofoco7=!estadofoco7;
        estadofoco8=!estadofoco8;
        print_port(foco0,estadofoco1);
        print_port(foco5,estadofoco6);
        print_port(foco6,estadofoco7);
        print_port(foco7,estadofoco8);
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 2,6,7,8: Con confirmación --226
    if (luz_2678 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco2=!estadofoco2;
        estadofoco6=!estadofoco6;
        estadofoco7=!estadofoco7;
        estadofoco8=!estadofoco8;
        print_port(foco1,estadofoco2);
        print_port(foco5,estadofoco6);
        print_port(foco6,estadofoco7);
        print_port(foco7,estadofoco8);
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,2,6,7,8: Con confirmación --227
    if (luz_12678 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco1=!estadofoco1;
        estadofoco2=!estadofoco2;
        estadofoco6=!estadofoco6;
        estadofoco7=!estadofoco7;
        estadofoco8=!estadofoco8;
        print_port(foco0,estadofoco1);
        print_port(foco1,estadofoco2);
        print_port(foco5,estadofoco6);
        print_port(foco6,estadofoco7);
        print_port(foco7,estadofoco8);
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
 }

 //Luz # 3,6,7,8: Con confirmación --228
    if (luz_3678 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco3=!estadofoco3;
        estadofoco6=!estadofoco6;
        estadofoco7=!estadofoco7;
        estadofoco8=!estadofoco8;
        print_port(foco2,estadofoco3);
        print_port(foco5,estadofoco6);
        print_port(foco6,estadofoco7);
        print_port(foco7,estadofoco8);
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,3,6,7,8: Con confirmación --229
    if (luz_13678 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco1=!estadofoco1;
        estadofoco3=!estadofoco3;
        estadofoco6=!estadofoco6;
        estadofoco7=!estadofoco7;
        estadofoco8=!estadofoco8;
        print_port(foco0,estadofoco1);
        print_port(foco2,estadofoco3);
        print_port(foco5,estadofoco6);
        print_port(foco6,estadofoco7);
        print_port(foco7,estadofoco8);
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 2,3,6,7,8: Con confirmación --230
    if (luz_23678 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco2=!estadofoco2;
        estadofoco3=!estadofoco3;
        estadofoco6=!estadofoco6;
        estadofoco7=!estadofoco7;
        estadofoco8=!estadofoco8;
        print_port(foco1,estadofoco2);
        print_port(foco2,estadofoco3);
        print_port(foco5,estadofoco6);
        print_port(foco6,estadofoco7);
        print_port(foco7,estadofoco8);
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

    /***************************************************************************************************/

    //Luz # 1,2,3,6,7,8: Con confirmación --231
    if (luz_123678 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco1=!estadofoco1;
        estadofoco2=!estadofoco2;
        estadofoco3=!estadofoco3;
        estadofoco6=!estadofoco6;
        estadofoco7=!estadofoco7;
        estadofoco8=!estadofoco8;
        print_port(foco0,estadofoco1);
        print_port(foco1,estadofoco2);
        print_port(foco2,estadofoco3);
        print_port(foco5,estadofoco6);
        print_port(foco6,estadofoco7);
        print_port(foco7,estadofoco8);
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 4,6,7,8: Con confirmación --232
    if (luz_4678 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco4=!estadofoco4;
        estadofoco6=!estadofoco6;
        estadofoco7=!estadofoco7;
        estadofoco8=!estadofoco8;
        print_port(foco3,estadofoco4);
        print_port(foco5,estadofoco6);
        print_port(foco6,estadofoco7);
        print_port(foco7,estadofoco8);
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,4,6,7,8: Con confirmación --233
    if (luz_14678 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco1=!estadofoco1;
        estadofoco4=!estadofoco4;
        estadofoco6=!estadofoco6;
        estadofoco7=!estadofoco7;
        estadofoco8=!estadofoco8;
        print_port(foco0,estadofoco1);
        print_port(foco3,estadofoco4);
        print_port(foco5,estadofoco6);
        print_port(foco6,estadofoco7);
        print_port(foco7,estadofoco8);
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 2,4,6,7,8: Con confirmación --234
    if (luz_24678 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco2=!estadofoco2;
        estadofoco4=!estadofoco4;
        estadofoco6=!estadofoco6;
        estadofoco7=!estadofoco7;
        estadofoco8=!estadofoco8;
        print_port(foco1,estadofoco2);
        print_port(foco3,estadofoco4);
        print_port(foco5,estadofoco6);
        print_port(foco6,estadofoco7);
        print_port(foco7,estadofoco8);
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,2,4,6,7,8: Con confirmación --235
    if (luz_124678 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco1=!estadofoco1;
        estadofoco2=!estadofoco2;
        estadofoco4=!estadofoco4;
        estadofoco6=!estadofoco6;
        estadofoco7=!estadofoco7;
        estadofoco8=!estadofoco8;
        print_port(foco0,estadofoco1);
        print_port(foco1,estadofoco2);
        print_port(foco3,estadofoco4);
        print_port(foco5,estadofoco6);
        print_port(foco6,estadofoco7);
        print_port(foco7,estadofoco8);
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 3,4,6,7,8: Con confirmación --236
    if (luz_34678 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco3=!estadofoco3;
        estadofoco4=!estadofoco4;
        estadofoco6=!estadofoco6;
        estadofoco7=!estadofoco7;
        estadofoco8=!estadofoco8;
        print_port(foco2,estadofoco3) ;
        print_port(foco3,estadofoco4) ;
        print_port(foco5,estadofoco6) ;
        print_port(foco6,estadofoco7) ;
        print_port(foco7,estadofoco8);
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

    //Luz # 1,3,4,6,7,8: Con confirmación --237
    if (luz_134678 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco1=!estadofoco1;
        estadofoco3=!estadofoco3;
        estadofoco4=!estadofoco4;
        estadofoco6=!estadofoco6;
        estadofoco7=!estadofoco7;
        estadofoco8=!estadofoco8;
        print_port(foco0,estadofoco1);
        print_port(foco2,estadofoco3);
        print_port(foco3,estadofoco4);
        print_port(foco5,estadofoco6);
        print_port(foco6,estadofoco7);
        print_port(foco7,estadofoco8);
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

    //Luz # 2,3,4,6,7,8: Con confirmación --238
    if (luz_234678 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco2=!estadofoco2;
        estadofoco3=!estadofoco3;
        estadofoco4=!estadofoco4;
        estadofoco6=!estadofoco6;
        estadofoco7=!estadofoco7;
        estadofoco8=!estadofoco8;
        print_port(foco1,estadofoco2);
        print_port(foco2,estadofoco3);
        print_port(foco3,estadofoco4);
        print_port(foco5,estadofoco6);
        print_port(foco6,estadofoco7);
        print_port(foco7,estadofoco8);
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

    //Luz # 1,2,3,4,6,7,8: Con confirmación --239
    if (luz_1234678 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco1=!estadofoco1;
        estadofoco2=!estadofoco2;
        estadofoco3=!estadofoco3;
        estadofoco4=!estadofoco4;
        estadofoco6=!estadofoco6;
        estadofoco7=!estadofoco7;
        estadofoco8=!estadofoco8;
        print_port(foco0,estadofoco1);
        print_port(foco1,estadofoco2);
        print_port(foco2,estadofoco3);
        print_port(foco3,estadofoco4);
        print_port(foco5,estadofoco6);
        print_port(foco6,estadofoco7);
        print_port(foco7,estadofoco8);
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

    //Luz # 5,6,7,8: Con confirmación --240
    if (luz_5678 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco5=!estadofoco5;
        estadofoco6=!estadofoco6;
        estadofoco7=!estadofoco7;
        estadofoco8=!estadofoco8;
        print_port(foco4,estadofoco5);
        print_port(foco5,estadofoco6);
        print_port(foco6,estadofoco7);
        print_port(foco7,estadofoco8);
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

    /*****************************************************************************************************/

    //Luz # 1,5,6,7,8: Con confirmación --241
    if (luz_15678 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco1=!estadofoco1;
        estadofoco5=!estadofoco5;
        estadofoco6=!estadofoco6;
        estadofoco7=!estadofoco7;
        estadofoco8=!estadofoco8;
        print_port(foco0,estadofoco1);
        print_port(foco4,estadofoco5);
        print_port(foco5,estadofoco6);
        print_port(foco6,estadofoco7);
        print_port(foco7,estadofoco8);
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

   //Luz # 2,5,6,7,8: Con confirmación --242
    if (luz_25678 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco2=!estadofoco2;
        estadofoco5=!estadofoco5;
        estadofoco6=!estadofoco6;
        estadofoco7=!estadofoco7;
        estadofoco8=!estadofoco8;
        print_port(foco1,estadofoco2);
        print_port(foco4,estadofoco5);
        print_port(foco5,estadofoco6);
        print_port(foco6,estadofoco7);
        print_port(foco7,estadofoco8);
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,2,5,6,7,8: Con confirmación --243
    if (luz_125678 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco1=!estadofoco1;
        estadofoco2=!estadofoco2;
        estadofoco5=!estadofoco5;
        estadofoco6=!estadofoco6;
        estadofoco7=!estadofoco7;
        estadofoco8=!estadofoco8;
        print_port(foco0,estadofoco1);
        print_port(foco1,estadofoco2);
        print_port(foco4,estadofoco5);
        print_port(foco5,estadofoco6);
        print_port(foco6,estadofoco7);
        print_port(foco7,estadofoco8);
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 3,5,6,7,8: Con confirmación --244
    if (luz_35678 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco3=!estadofoco3;
        estadofoco5=!estadofoco5;
        estadofoco6=!estadofoco6;
        estadofoco7=!estadofoco7;
        estadofoco8=!estadofoco8;
        print_port(foco2,estadofoco3);
        print_port(foco4,estadofoco5);
        print_port(foco5,estadofoco6);
        print_port(foco6,estadofoco7);
        print_port(foco7,estadofoco8);
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,3,5,6,7,8: Con confirmación --245
    if (luz_135678 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco1=!estadofoco1;
        estadofoco3=!estadofoco3;
        estadofoco5=!estadofoco5;
        estadofoco6=!estadofoco6;
        estadofoco7=!estadofoco7;
        estadofoco8=!estadofoco8;
        print_port(foco0,estadofoco1);
        print_port(foco2,estadofoco3);
        print_port(foco4,estadofoco5);
        print_port(foco5,estadofoco6);
        print_port(foco6,estadofoco7);
        print_port(foco7,estadofoco8);
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 2,3,5,6,7,8: Con confirmación --246
    if (luz_235678 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco2=!estadofoco2;
        estadofoco3=!estadofoco3;
        estadofoco5=!estadofoco5;
        estadofoco6=!estadofoco6;
        estadofoco7=!estadofoco7;
        estadofoco8=!estadofoco8;
        print_port(foco1,estadofoco2);
        print_port(foco2,estadofoco3);
        print_port(foco4,estadofoco5);
        print_port(foco5,estadofoco6);
        print_port(foco6,estadofoco7);
        print_port(foco7,estadofoco8);
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 1,2,3,5,6,7,8: Con confirmación --247
    if (luz_1235678 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco1=!estadofoco1;
        estadofoco2=!estadofoco2;
        estadofoco3=!estadofoco3;
        estadofoco5=!estadofoco5;
        estadofoco6=!estadofoco6;
        estadofoco7=!estadofoco7;
        estadofoco8=!estadofoco8;
        print_port(foco0,estadofoco1);
        print_port(foco1,estadofoco2);
        print_port(foco2,estadofoco3);
        print_port(foco4,estadofoco5);
        print_port(foco5,estadofoco6);
        print_port(foco6,estadofoco7);
        print_port(foco7,estadofoco8);
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 4,5,6,7,8: Con confirmación --248
    if (luz_45678 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco4=!estadofoco4;
        estadofoco5=!estadofoco5;
        estadofoco6=!estadofoco6;
        estadofoco7=!estadofoco7;
        estadofoco8=!estadofoco8;
        print_port(foco3,estadofoco4);
        print_port(foco4,estadofoco5);
        print_port(foco5,estadofoco6);
        print_port(foco6,estadofoco7);
        print_port(foco7,estadofoco8);
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
 }

 //Luz # 1,4,5,6,7,8: Con confirmación --249
    if (luz_145678 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco1=!estadofoco1;
        estadofoco4=!estadofoco4;
        estadofoco5=!estadofoco5;
        estadofoco6=!estadofoco6;
        estadofoco7=!estadofoco7;
        estadofoco8=!estadofoco8;
        print_port(foco0,estadofoco1);
        print_port(foco3,estadofoco4);
        print_port(foco4,estadofoco5);
        print_port(foco5,estadofoco6);
        print_port(foco6,estadofoco7);
        print_port(foco7,estadofoco8);
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

  //Luz # 2,4,5,6,7,8: Con confirmación --250
    if (luz_245678 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco2=!estadofoco2;
        estadofoco4=!estadofoco4;
        estadofoco5=!estadofoco5;
        estadofoco6=!estadofoco6;
        estadofoco7=!estadofoco7;
        estadofoco8=!estadofoco8;
        print_port(foco1,estadofoco2);
        print_port(foco3,estadofoco4);
        print_port(foco4,estadofoco5);
        print_port(foco5,estadofoco6);
        print_port(foco6,estadofoco7);
        print_port(foco7,estadofoco8);
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                                 //Bórralo para la próxima vez
  }

    /***********************************************************************/

  //Luz # 1,2,4,5,6,7,8: Con confirmación --251
    if (luz_1245678 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco1=!estadofoco1;
        estadofoco2=!estadofoco2;
        estadofoco4=!estadofoco4;
        estadofoco5=!estadofoco5;
        estadofoco6=!estadofoco6;
        estadofoco7=!estadofoco7;
        estadofoco8=!estadofoco8;
        print_port(foco0,estadofoco1);
        print_port(foco1,estadofoco2);
        print_port(foco3,estadofoco4);
        print_port(foco4,estadofoco5);
        print_port(foco5,estadofoco6);
        print_port(foco6,estadofoco7);
        print_port(foco7,estadofoco8);
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

  //Luz # 3,4,5,6,7,8: Con confirmación --252
    if (luz_345678 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco3=!estadofoco3;
        estadofoco4=!estadofoco4;
        estadofoco5=!estadofoco5;
        estadofoco6=!estadofoco6;
        estadofoco7=!estadofoco7;
        estadofoco8=!estadofoco8;
        print_port(foco2,estadofoco3);
        print_port(foco3,estadofoco4);
        print_port(foco4,estadofoco5);
        print_port(foco5,estadofoco6);
        print_port(foco6,estadofoco7);
        print_port(foco7,estadofoco8);
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

  //Luz # 1,3,4,5,6,7,8: Con confirmación --253
    if (luz_1345678 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco1=!estadofoco1;
        estadofoco3=!estadofoco3;
        estadofoco4=!estadofoco4;
        estadofoco5=!estadofoco5;
        estadofoco6=!estadofoco6;
        estadofoco7=!estadofoco7;
        estadofoco8=!estadofoco8;
        print_port(foco0,estadofoco1);
        print_port(foco2,estadofoco3);
        print_port(foco3,estadofoco4);
        print_port(foco4,estadofoco5);
        print_port(foco5,estadofoco6);
        print_port(foco6,estadofoco7);
        print_port(foco7,estadofoco8);
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

  //Luz # 2,3,4,5,6,7,8: Con confirmación --254
    if (luz_2345678 >= 0){
      for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco2=!estadofoco2;
        estadofoco3=!estadofoco3;
        estadofoco4=!estadofoco4;
        estadofoco5=!estadofoco5;
        estadofoco6=!estadofoco6;
        estadofoco7=!estadofoco7;
        estadofoco8=!estadofoco8;
        print_port(foco1,estadofoco2);
        print_port(foco2,estadofoco3);
        print_port(foco3,estadofoco4);
        print_port(foco4,estadofoco5);
        print_port(foco5,estadofoco6);
        print_port(foco6,estadofoco7);
        print_port(foco7,estadofoco8);
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

  //Luz # 1,2,3,4,5,6,7,8: Con confirmación --255
    if (luz_12345678 >= 0){
     for(int i=0;i<size_Agenda;i++){
      int pos = mensaje.indexOf(agenda[i]);
      if (pos >= 0){
        estadofoco1=!estadofoco1;
        estadofoco2=!estadofoco2;
        estadofoco3=!estadofoco3;
        estadofoco4=!estadofoco4;
        estadofoco5=!estadofoco5;
        estadofoco6=!estadofoco6;
        estadofoco7=!estadofoco7;
        estadofoco8=!estadofoco8;
        print_port(foco0,estadofoco1);
        print_port(foco1,estadofoco2);
        print_port(foco2,estadofoco3);
        print_port(foco3,estadofoco4);
        print_port(foco4,estadofoco5);
        print_port(foco5,estadofoco6);
        print_port(foco6,estadofoco7);
        print_port(foco7,estadofoco8);
        //Serial.println("\nEnviando SMS: Contacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
        //Serial.println("\nCambio de estado luminaria #3 en el laboratorio.\nCON CONFIRMACION.");
        mensaje_sms2(agenda[i]);                 //Función para enviar SMS de respuesta a destinatario autorizado.
        //Serial.println("SMS Enviado.");
      }
    }
    mensaje = "" ;                               //Bórralo para la próxima vez
  }

  //verifico si hay red.
  //buscared();

    //(((((((((((((((((((((((((uFF...POR FIN, EL FINAL DE LA PELÍCULA)))))))))))))))))))))))))))))))))))))
    /*(((((((((((((((((((((((((((((((((((((((((finish))))))))))))))))))))))))))))))))))))))))))))))))))))*/
    /*(((((((((((((((((((((((((((((((((((((((((((TODO))))))))))))))))))))))))))))))))))))))))))))))))))))*/

}

  //FUNCION PARA IDENTIFICAR LLAMADA ENTRANTE Y ACTIVAR/DESACTIVAR TODAS LAS LUCES
  //SEGUN USUARIO AUTORIZADO.
  void identifica_llamada(){
    int finllamada = mensaje.indexOf("NO CARRIER");
    if (finllamada >= 0){
      for (int i = 0; i < size_Agenda; i++){
        int pos = mensaje.indexOf(agenda[i]);
        if (pos >= 0){
          cambioTodasLuces();
          //Serial.println("\nContacto Encontrado en la posicion: "+String(i+1)+", "+String(agenda[i]));
          //llamar(agenda[i]);              //Función para realizar llamada a destinatario autorizado.
          delay(2000);
          mensaje_sms2(agenda[i]);           //Función para enviar SMS del estado de las luces.
          status_luces_BT();
          Serial1.print("Waiting more...");
        }
      }
      mensaje = "" ;                         //Bórralo para la próxima vez
     }
  }

int enviarAT(char* ATcommand, char* resp_correcta, unsigned int tiempo){
  int x = 0;
  bool correcto = 0;
  char respuesta[150];
  unsigned long anterior;

  memset(respuesta, '\0', 150); // Inicializa el string
  delay(100);
  while ( SIM900.available() > 0) SIM900.read(); // Limpia el buffer de entrada
  SIM900.println(ATcommand); // Envia el comando AT
  x = 0;
  anterior = millis();
  // Espera una respuesta
  do {
    // si hay datos el buffer de entrada del UART lee y comprueba la respuesta
    if (SIM900.available() != 0)
    {
      //Comprueba que no haya desbordamiento en la capacidad del buffer
      if (x < 149) {
        respuesta[x] = SIM900.read();
        x++;
      }
      else Serial1.println(F("Desbordamiento!"));
      // Comprueba si la respuesta del modulo es la 1
      if (strstr(respuesta, resp_correcta) != NULL)
      {
        correcto = 1;
      }
    }
  }
  // Espera hasta tener una respuesta
  while ((correcto == 0) && ((millis() - anterior) < tiempo));
  Serial1.println(respuesta);

  return correcto;
}

int enviarAT1(String ATcommand1, char* resp_correcta, unsigned int tiempo){
  int x = 0;
  bool correcto = 0;
  char respuesta[150];
  unsigned long anterior;

  memset(respuesta, '\0', 150); // Inicializa el string
  delay(100);
  while ( SIM900.available() > 0) SIM900.read(); // Limpia el buffer de entrada
  SIM900.println(ATcommand1); // Envia el comando AT
  x = 0;
  anterior = millis();
  // Espera una respuesta
  do {
    // si hay datos el buffer de entrada del UART lee y comprueba la respuesta
    if (SIM900.available() != 0)
    {
      //Comprueba que no haya desbordamiento en la capacidad del buffer
      if (x < 149) {
        respuesta[x] = SIM900.read();
        x++;
      }
      else Serial1.println(F("Desbordamiento!"));
      // Comprueba si la respuesta del modulo es la 1
      if (strstr(respuesta, resp_correcta) != NULL)
      {
        correcto = 1;
      }
    }
  }
  // Espera hasta tener una respuesta
  while ((correcto == 0) && ((millis() - anterior) < tiempo));
  Serial1.println(respuesta);

  return correcto;
}

int enviarAT2(String ATcommand1, char* resp_correcta, unsigned int tiempo){
  int x = 0;
  bool correcto = 0;
  char respuesta[250];
  unsigned long anterior;

  memset(respuesta, '\0', 250); // Inicializa el string
  delay(100);
  while ( SIM900.available() > 0) SIM900.read(); // Limpia el buffer de entrada
  SIM900.println(ATcommand1); // Envia el comando AT
  x = 0;
  anterior = millis();
  // Espera una respuesta
  do {
    // si hay datos el buffer de entrada del UART lee y comprueba la respuesta
    if (SIM900.available() != 0)
    {
      //Comprueba que no haya desbordamiento en la capacidad del buffer
      if (x < 249) {
        respuesta[x] = SIM900.read();
        x++;
      }
      else Serial1.println(F("Desbordamiento!"));
      // Comprueba si la respuesta del modulo es la 1
      if (strstr(respuesta, resp_correcta) != NULL)
      {
        correcto = 1;
      }
    }
  }
  // Espera hasta tener una respuesta
  while ((correcto == 0) && ((millis() - anterior) < tiempo));
  Serial1.println(respuesta);

  return correcto;
}

void power_on(){
  int respuesta = 0;

  // Comprueba que el modulo SIM900 esta arrancado
  if (enviarAT("AT", "OK", 2000) == 0) //comprueba la conexion a la red
  {
    Serial1.println(F("Encendiendo el Modulo GSM-GPRS..."));

    pinMode(9, OUTPUT);
    print_port(9, HIGH);
    delay(1000);
    print_port(9, LOW);
    delay(1000);

    // Espera la respuesta del modulo SIM900
    while (respuesta == 0) {
      // Envia un comando AT cada 2 segundos y espera la respuesta
      respuesta = enviarAT("AT", "OK", 2000);
      SIM900.println(respuesta);
    }
  }
}

void power_off(){
  print_port(9, HIGH);
  delay(1000);
  print_port(9, LOW);
  delay(1000);
}

void reiniciar(){
  Serial1.println(F("Reiniciando..."));
  //Serial1.println("Reiniciando...");
  power_off();
  delay (5000);
  power_on();
}

void iniciar(){
  enviarAT("AT+CPIN=\"1234\"", "OK", 1000);
  Serial1.println(F("CONECTANDO CON LA RED...ESPERE..."));
  delay (5000);
  //espera hasta estar conectado a la red movil
  while ( enviarAT("AT+CREG?", "+CREG: 0,1", 1000) == 0 )
  {
  }
  Serial1.println(F("Conectado a la red."));
  enviarAT("AT+CLIP=1\r", "OK", 1000);         //Activamos la identificacion de llamadas
  enviarAT("AT+CMGF=1\r", "OK", 1000);         //Configura el modo texto para enviar o recibir mensajes
  enviarAT("AT+CNMI=2,2,0,0,0\r", "OK", 1000); //Configuramos el modulo para que nos muestre los SMS recibidos por comunicacion serie
  Serial1.println(F("Sistema Listo para Ejecutar Ordenes."));
}

//Funcion para reportar los parametros ambientales (Temperatura y humedad relativa)
void mensaje_sms1(String numDestino){
  float h = dht.readHumidity();
  float t = dht.readTemperature();
  float f = dht.readTemperature(true);
  delay(2000);

  // Check if any reads failed and exit early (to try again).
  if (isnan(h) || isnan(t) || isnan(f)) {
    Serial1.println(F("Failed to read from DHT sensor!"));
    return;
  }

  // Compute heat index in Fahrenheit (the default)
  float hif = dht.computeHeatIndex(f, h);
  // Compute heat index in Celsius (isFahreheit = false)
  float hic = dht.computeHeatIndex(t, h, false);

  String tramaSMS = "";
  String inicio = "i:";
  String separador = ",";
  String fin ="f";

  String finalSMS = " \x1A \r\n";

  tramaSMS += inicio;
  tramaSMS += t;
  tramaSMS += separador;
  tramaSMS += h;
  tramaSMS += fin;
  tramaSMS += finalSMS;

  if (enviarAT1("AT+CREG?", "+CREG: 0,1", 1000) == 1) //comprueba la conexion a la red
  {
    //Serial1.println("Enviando SMS...");
    Serial1.println(F("\nEnviando SMS...TEMPERATURA Y HUMEDAD RELATIVA."));
    Serial1.print(numDestino);
    Serial1.println();

    enviarAT1("AT+CMGF=1\r", "OK", 1000); //Comando AT para mandar un SMS
   String aquien = "AT+CMGS=";
     aquien +="\"";
     aquien += numDestino;
     aquien +="\"";
    sprintf(aux_str, string2char(aquien), tramaSMS.length());                //Numero al que vamos a enviar el mensaje
    //sprintf(aux_str, "AT+CMGS=\"61107065\"", MostrarPorPantalla.length()); //Numero al que vamos a enviar el mensaje
    //Texto del mensaje
    if (enviarAT1(aux_str, ">", 10000) == 1)
    {
      enviarAT1(tramaSMS, "OK", 10000);
    }
    //Serial1.println("SMS enviado");
    Serial1.println(F("SMS enviado"));
    //status_luces_BT();
    temp_rh_BT();
    Serial1.print("Waiting more...");
  }
  else
  {
    reiniciar();
    iniciar();
  }
}

//Función que devuelve estado de las luces
void mensaje_sms2(String numDestino){
  delay(1000);
  String txtSensor1 = "";String txtSensor2 = "";String txtSensor3 = "";String txtSensor4 = "";
  String txtSensor5 = "";String txtSensor6 = "";String txtSensor7 = "";String txtSensor8 = "";

  sensorValueA0 = 0; sensorValueA1 = 0; sensorValueA2 = 0; sensorValueA3 = 0;
  sensorValueA4 = 0; sensorValueA5 = 0; sensorValueA6 = 0; sensorValueA7 = 0;

  //Acá pondre el código fuente para tomar la información proporcionada por los sensores
  //que van a verificar el estado de las luces en el laboratorio de la universidad UTLA.
  /*int sensorValue = analogRead (A0);
  String stringOne = "Valor del sensor:";
  String stringThree = stringOne + sensorValue;
  Serial.println (stringThree); */

  sensorValueA0 = analogRead(analogInPin0);
  sensorValueA1 = analogRead(analogInPin1);
  sensorValueA2 = analogRead(analogInPin2);
  sensorValueA3 = analogRead(analogInPin3);
  sensorValueA4 = analogRead(analogInPin4);
  sensorValueA5 = analogRead(analogInPin5);
  sensorValueA6 = analogRead(analogInPin6);
  sensorValueA7 = analogRead(analogInPin7);

  Serial1.println();
  Serial1.println("A0:" + String(sensorValueA0) + ", A1:" + String(sensorValueA1) + ", A2:" + String(sensorValueA2) + ", A3:" + String(sensorValueA3) + ", A4:" + String(sensorValueA4) + ", A5:" + String(sensorValueA5) + ", A6:" + String(sensorValueA6) + ", A7:" + String(sensorValueA7) + " ");

  /*
   * SI SE DETECTAN LECTURAS BAJAS DE LUZ SIGNIFICA QUE HAY LUZ ENCENDIDA.
   * SI SE DETECTAN LECTURAS ALTAS DE LUZ SIGNIFICA QUE HAY OSCURIDAD.
   */

  if(sensorValueA0 <= levelValueSensibility){
      txtSensor1 = "1";
  }else{
      txtSensor1 = "0";
  }
  /*
  int datoSensorLuz1 = digitalRead(statuSensorLuz1);
  if (datoSensorLuz1 == 1){
      txtSensor1 = "1";
  }else{
      txtSensor1 = "0";
  }
  */

  String valor1 = "Luz 1 = ";
  String cadenaSensor1 = valor1 + txtSensor1;

  if(sensorValueA1 <= levelValueSensibility){
      txtSensor2 = "1";
  }else{
      txtSensor2 = "0";
  }
  /*
  int datoSensorLuz2 = digitalRead(statuSensorLuz2);
  if (datoSensorLuz2 == 1){
      txtSensor2 = "1";
  }else{
      txtSensor2 = "0";
  }
  */
  String valor2 = "Luz 2 = ";
  String cadenaSensor2 = valor2 + txtSensor2;

  if(sensorValueA2 <= levelValueSensibility){
      txtSensor3 = "1";
  }else{
      txtSensor3 = "0";
  }
  /*
  int datoSensorLuz3 = digitalRead(statuSensorLuz3);
  if (datoSensorLuz3 == 1){
      txtSensor3 = "1";
  }else{
      txtSensor3 = "0";
  }
  */
  String valor3 = "Luz 3 = ";
  String cadenaSensor3 = valor3 + txtSensor3;

  if(sensorValueA3 <= levelValueSensibility){
      txtSensor4 = "1";
  }else{
      txtSensor4 = "0";
  }
  /*
  int datoSensorLuz4 = digitalRead(statuSensorLuz4);
  if (datoSensorLuz4 == 1){
      txtSensor4 = "1";
  }else{
      txtSensor4 = "0";
  }
  */
  String valor4 = "Luz 4 = ";
  String cadenaSensor4 = valor4 + txtSensor4;

  if(sensorValueA4 <= levelValueSensibility){
      txtSensor5 = "1";
  }else{
      txtSensor5 = "0";
  }
  /*
  int datoSensorLuz5 = digitalRead(statuSensorLuz5);
   if (datoSensorLuz5 == 1){
      txtSensor5 = "1";
  }else{
      txtSensor5 = "0";
  }
  */
  String valor5 = "Luz 5 = ";
  String cadenaSensor5 = valor5 + txtSensor5;

  if(sensorValueA5 <= levelValueSensibility){
      txtSensor6 = "1";
  }else{
      txtSensor6 = "0";
  }
  /*
  int datoSensorLuz6 = digitalRead(statuSensorLuz6);
  if (datoSensorLuz6 == 1){
      txtSensor6 = "1";
  }else{
      txtSensor6 = "0";
  }
  */
  String valor6 = "Luz 6 = ";
  String cadenaSensor6 = valor6 + txtSensor6;

  if(sensorValueA6 <= levelValueSensibility){
      txtSensor7 = "1";
  }else{
      txtSensor7 = "0";
  }
  /*
  int datoSensorLuz7 = digitalRead(statuSensorLuz7);
  if (datoSensorLuz7 == 1){
      txtSensor7 = "1";
  }else{
      txtSensor7 = "0";
  }
  */
  String valor7 = "Luz 7 = ";
  String cadenaSensor7 = valor7 + txtSensor7;

  if(sensorValueA7 <= levelValueSensibility){
      txtSensor8 = "1";
  }else{
      txtSensor8 = "0";
  }
  /*
  int datoSensorLuz8 = digitalRead(statuSensorLuz8);
  if (datoSensorLuz8 == 1){
      txtSensor8 = "1";
  }else{
      txtSensor8 = "0";
  }
  */
  String valor8 = "Luz 8 = ";
  String cadenaSensor8 = valor8 + txtSensor8;

  String informacion = "Estado Actual Luces.\n";
  String saltoLinea ="\n";
  String por ="\nCopyright UTLA,2018";
  String finalSMS = " \x1A \r\n";

  informacion += cadenaSensor1;
  informacion += saltoLinea;
  informacion += cadenaSensor2;
  informacion += saltoLinea;
  informacion += cadenaSensor3;
  informacion += saltoLinea;
  informacion += cadenaSensor4;
  informacion += saltoLinea;
  informacion += cadenaSensor5;
  informacion += saltoLinea;
  informacion += cadenaSensor6;
  informacion += saltoLinea;
  informacion += cadenaSensor7;
  informacion += saltoLinea;
  informacion += cadenaSensor8;
  //informacion += saltoLinea;
  informacion += por;
  informacion += finalSMS;

  //Serial1.println("Lecturas actuales de los sensores de luces\n" );
  //Serial1.println(cadenaSensor1 + " " + cadenaSensor2 + " " + cadenaSensor3 + " " + cadenaSensor4 + " " + cadenaSensor5 + " " + cadenaSensor6 + " " + cadenaSensor7 + " " + cadenaSensor7 + " " + cadenaSensor8);
  //Serial1.println(informacion);
  delay(2);

  if (enviarAT2("AT+CREG?", "+CREG: 0,1", 1000) == 1) //comprueba la conexion a la red
  {
    //Serial1.println("Enviando SMS...");
    Serial1.println(F("\nEnviando SMS...ESTADO DE LUMINARIAS."));
    Serial1.print(numDestino);
    Serial1.println();

     enviarAT2("AT+CMGF=1\r", "OK", 1000);                            //Comando AT para mandar un SMS
     String aquien = "AT+CMGS=";
     aquien +="\"";
     aquien += numDestino;
     aquien +="\"";
    sprintf(aux_str, string2char(aquien), informacion.length());      //Numero al que vamos a enviar el mensaje
    //sprintf(aux_str, "AT+CMGS=\"61107065\"", informacion.length()); //Numero al que vamos a enviar el mensaje
    //Texto del mensaje
    if (enviarAT2(aux_str, ">", 10000) == 1)
    {
      enviarAT2(informacion, "OK", 10000);
    }
     //Serial1.println("SMS enviado");
     Serial1.println(F("SMS enviado"));
     status_luces_BT();
     Serial1.print("Waiting more...");
  }else{
    reiniciar();
    iniciar();
  }
}

void llamar(String numDestino){
  if (enviarAT("AT+CREG?", "+CREG: 0,1", 1000) == 1) //Comprueba la conexion a la red
  {
    Serial1.println(F("\nRealizando llamada..."));
    Serial1.print(numDestino);
    Serial1.println();
    String chevere = "ATD ";
    chevere += numDestino;
    chevere += ";";

    //Linea original
    //enviarAT("ATD 61107065;", "OK", 1000);
    enviarAT(string2char(chevere), "OK", 1000);     //En esta línea tengo el error.

    delay(20000); // Espera 20 segundos mientras realiza la llamada
    enviarAT("ATH", "OK", 1000); // Cuelga la llamada
    Serial1.println(F("Llamada finalizada"));
    //status_luces_BT();
    //Serial1.print("Waiting more...");
  }
  else
  {
    reiniciar();
    iniciar();
  }
}

//Funcion que notifica que el sistema ya esta listo para recibir ordenes desde la App Android.
//ESTA FUNCIÓN LA ELIMINE ....ESTOY CON MIS DUDAS.
/*
void mensaje_sms(String numDestino){
  if (enviarAT("AT+CREG?", "+CREG: 0,1", 1000) == 1)           //comprueba la conexion a la red
  {
    Serial.println("\nEnviando SMS...");
    Serial.print(numDestino);
    Serial.println();
    //string2char("");                                         //No fue necesaria esta línea.
    enviarAT("AT+CMGF=1\r", "OK", 1000);                       //Comando AT para mandar un SMS
     String aquien = "AT+CMGS=";
     aquien +="\"";
     aquien += numDestino;
     aquien +="\"";
    //Linea original
    //sprintf(aux_str, "AT+CMGS=\"61107065\"", strlen(sms));   //Numero al que vamos a enviar el mensaje
    sprintf(aux_str, string2char(aquien), strlen(sms));        //Numero al que vamos a enviar el mensaje

    //Texto del mensaje
    if (enviarAT(aux_str, ">", 10000) == 1)
    {
      enviarAT(sms, "OK", 10000);
    }
    Serial.println("SMS enviado");
  }else{
    reiniciar();
    iniciar();
  }
}
*/

//Funcion que notifica que el sistema ya esta listo para recibir ordenes desde la App Android.
void mensaje_universal(String numDestino, char* texto){
  //if (enviarAT2("AT+CREG?", "+CREG: 0,1", 1000) == 1) //comprueba la conexion a la red
  if (enviarAT2("AT+CREG?", "+CREG: 0,1", 1000) == 1)           //comprueba la conexion a la red
  {
    Serial1.println(F("\nEnviando SMS...SISTEMA LISTO, SALUDO."));
    Serial1.print(numDestino);
    Serial1.println();
    //string2char("");                                          //No fue necesaria esta línea.
    enviarAT2("AT+CMGF=1\r", "OK", 1000);                       //Comando AT para mandar un SMS
     String aquien = "AT+CMGS=";
     aquien +="\"";
     aquien += numDestino;
     aquien +="\"";
    //Linea original
    //sprintf(aux_str, "AT+CMGS=\"61107065\"", strlen(sms));   //Numero al que vamos a enviar el mensaje
    sprintf(aux_str, string2char(aquien), strlen(texto));      //Numero al que vamos a enviar el mensaje

    //Texto del mensaje
    if (enviarAT2(aux_str, ">", 10000) == 1)
    {
      enviarAT2(texto, "OK", 10000);               //Esta línea hace que se envie el mensaje.
    }
    Serial1.println(F("SMS enviado"));
  }else{
    reiniciar();
    iniciar();
  }
}

//Arduino String to Char*
  //URL: https://coderwall.com/p/zfmwsg/arduino-string-to-char
char* string2char(String command){
    if(command.length()!=0){
        char *p = const_cast<char*>(command.c_str());
        return p;
    }
}

void print_port(int carga, int estado){
  digitalWrite(carga, estado) ;
}

void printerFTDI(String cadena){
  //Serial1.println(cadena);
  Serial1.println(cadena);
}

//Versión FINAL. 2019-2020.