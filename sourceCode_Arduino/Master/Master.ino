#include <stdint.h>
#include <SPI.h>
#include <LoRa.h>
#include <WiFi.h>
#include <HTTPClient.h>
#include <ArduinoJson.h>

byte MasterNode = 0xAB;
byte Node1 = 0xAA;
byte Node2 = 0xBB;

String SenderNode = "";
// String outgoing;              // outgoing message
String incoming = "";
byte msgCount = 0; // count of outgoing messages

// Tracks the time since last event fired
unsigned long int previoussecs = 0;
unsigned long int currentsecs = 0;
unsigned long int currentMillis = 0;

const unsigned long eventInterval_HTTP = 5000;
unsigned long previousTime_HTTP = 0;

int timeInterval = 5;

int Secs = 0;

#define ledR 25
#define ledB 26

// Configuration Parameter of Arduino
int txPower = 14;              // from 0 to 20, default is 14
int spreadingFactor = 9;       // from 7 to 12, default is 12
long signalBandwidth = 125E3;  // 7.8E3, 10.4E3, 15.6E3, 20.8E3, 31.25E3,41.7E3,62.5E3,125E3,250E3,500e3, default is 125E3
int codingRateDenominator = 5; // Numerator is 4, and denominator from 5 to 8, default is 5
int preambleLength = 8;        // from 2 to 20, default is 8
String payload = "";           // you can change the payload

#define SS 15
#define RST 4
#define DI0 2
#define BAND 433E6

#define WIFI_SSID "Hanh"
#define WIFI_PASSWORD "0905128744"

// Server Name
String serverName = "http://leducnhat.xyz/api/v1/user";
void initWiFi()
{
  WiFi.mode(WIFI_STA);
  WiFi.begin(WIFI_SSID, WIFI_PASSWORD);
  Serial.print("Connecting to WiFi .. ");
  while (WiFi.status() != WL_CONNECTED)
  {
    Serial.print('.');
    delay(1000);
  }
  Serial.print(WiFi.localIP());
  Serial.print(" RSSI: ");
  Serial.println(WiFi.RSSI());
}
void setup()
{
  pinMode(ledR, OUTPUT);
  pinMode(ledB, OUTPUT);
  Serial.begin(115200); // initialize serial
  initWiFi();
  while (!Serial)
    ;

  Serial.println("LoRa Receiver");
  Serial.print("SetFrequency : ");
  Serial.print(BAND);
  Serial.println("Hz");
  Serial.print("SetSpreadingFactor : SF");
  Serial.println(spreadingFactor);
  SPI.begin();
  LoRa.setPins(SS, RST, DI0);
  if (!LoRa.begin(BAND))
  {
    Serial.println("Starting LoRa failed!");
    while (1)
      ;
  }
  LoRa.setTxPower(txPower, 1);
  LoRa.setSpreadingFactor(spreadingFactor);
  LoRa.setSignalBandwidth(signalBandwidth);
  LoRa.setCodingRate4(codingRateDenominator);
  LoRa.setPreambleLength(preambleLength);

  // Serial.println("LoRa init succeeded.");
}

void loop()
{
  SendPayloadMaster_Slave();
}
// Data exchange between Master Node and Slave Node
void SendPayloadMaster_Slave()
{
  currentMillis = millis();
  currentsecs = currentMillis / 1000;
  if ((unsigned long)(currentsecs - previoussecs) >= timeInterval)
  {
    Secs = Secs + 1;
    if (Secs >= 5)
    {
      Secs = 0;
    }
    if ((Secs >= 1) && (Secs <= 2))
    {
      Serial.println("-------------*****************-----------");
      Serial.println("NODE1");
      int dataController_Node1 = ReceivePayloarFromServer_ControlDevice_Node1();
      // randomInterval = random(3, 5);
      // randomInterval = 2;
      String messageNode1 = messageNode1 + "34" + "," + dataController_Node1;
      sendMessage(messageNode1, MasterNode, Node1);
      Serial.print("Sender payload to slave 34 with SECS: ");
      Serial.print(Secs);
      // Serial.print(" & Interval:  ");
      // Serial.print(randomInterval);
      Serial.print(" & Status Device 1:  ");
      Serial.println(dataController_Node1);
    }
    if ((Secs >= 3) && (Secs <= 4))
    {
      Serial.println("-------------*****************-----------");
      Serial.println("NODE2");
      int dataController_Node2 = ReceivePayloarFromServer_ControlDevice_Node2();
      // randomInterval = random(3, 5);
      // randomInterval = 10;
      String messageNode2 = messageNode2 + "55" + "," + dataController_Node2;
      sendMessage(messageNode2, MasterNode, Node2);
      Serial.print("Sender payload to slave 55 with SECS: ");
      Serial.print(Secs);
      // Serial.print(" & Interval:  ");
      // Serial.print(randomInterval);
      Serial.print(" & Status Device 2:  ");
      Serial.println(dataController_Node2);
    }
    previoussecs = currentsecs;
  }
  // parse for a packet, and call onReceive with the result:
  onReceive(LoRa.parsePacket());
}
// Send payload from MasterNode to Slave Node
void sendMessage(String outgoing, byte MasterNode, byte otherNode)
{
  LoRa.beginPacket();            // start packet
  LoRa.write(otherNode);         // add destination address
  LoRa.write(MasterNode);        // add sender address
  LoRa.write(msgCount);          // add message ID
  LoRa.write(outgoing.length()); // add payload length
  LoRa.print(outgoing);          // add payload
  LoRa.endPacket();              // finish packet and send it
  msgCount++;                    // increment message ID
}
// Receiver payload from Slave Node
void onReceive(int packetSize)
{
  int led;
  String nodeName = "";
  String printNodeName = "";
  /*
   *Read value RRSI of packet
   */
  int8_t RSSI_Value = LoRa.packetRssi();
  if (packetSize == 0)
    return;
  int recipient = LoRa.read(); // recipient address
  byte sender = LoRa.read();   // sender address

  if (sender == Node1)
  { // address Node Sender
    SenderNode = "NodeTX1:";
    led = ledR;
    nodeName = "Node1";
  }
  if (sender == Node2)
  { // address Node Sender
    SenderNode = "NodeTX2:";
    led = ledB;
    nodeName = "Node2";
  }

  byte incomingMsgId = LoRa.read();  // incoming msg ID
  byte incomingLength = LoRa.read(); // incoming msg length

  String incoming = "";

  while (LoRa.available())
  {
    incoming += (char)LoRa.read();
  }

  if (incomingLength != incoming.length())
  { // check length for error
    // Serial.println("error: message length does not match length");
    ;
    return; // skip rest of function
  }
  String temperature = getValue(incoming, ',', 0);
  String humidity = getValue(incoming, ',', 1);
  String soilidAnalog = getValue(incoming, ',', 2);
  String lightAnalog = getValue(incoming, ',', 3);
  Serial.print("PAYLOAD: Temperature = ");
  Serial.print(temperature);
  Serial.print(", humidity = ");
  Serial.print(humidity);
  Serial.print(", soilidAnalog = ");
  Serial.print(soilidAnalog);
  Serial.print(", lightAnalog = ");
  Serial.print(lightAnalog);
  Serial.print(", RSSI_Value = ");
  Serial.print(RSSI_Value);
  if (nodeName == "Node1")
  {
    /* code */
    printNodeName = "t3SMV2rmCjmOg6GLTa76";
  }
  else if (nodeName == "Node2")
  {
    printNodeName = "HzcIq70nUYXoP6DdRc3x";
  }
  Serial.print(", PrintNodeName:  ");
  Serial.println(printNodeName);
  Serial.println("******Sender Payload to Server******");
  ledBlink(led);
  SenderPayloadtoServer(printNodeName, temperature, humidity, soilidAnalog, lightAnalog, RSSI_Value);
}
// SEND payload from Master to Network Server
void SenderPayloadtoServer(String printNodeName, String temperature, String humidity, String soilidAnalog, String lightAnalog, int8_t RSSI_Value)
{
  /* Updates frequently */
  unsigned long currentTime_HTTP = millis();

  /* This is the event */
  if (currentTime_HTTP - previousTime_HTTP >= eventInterval_HTTP)
  {
    WiFiClient client;
    HTTPClient http;
    String serverPath = serverName + "/data/token-key";
    // Your Domain name with URL path or IP address with path
    http.begin(client, serverPath.c_str());
    http.addHeader("Content-Type", "application/json");
    String bodystr = "{\"tokenKey\": \"";
    bodystr.concat("9fowBuKSpK6uw214o0maTP7gya3MkuWO");
    bodystr.concat("\",\"temperature\":\"");
    bodystr.concat(temperature);
    bodystr.concat("\",\"temperatureMeasure\":\"");
    bodystr.concat("CELSIUS");
    bodystr.concat("\",\"humidity\":\"");
    bodystr.concat(humidity);
    bodystr.concat("\",\"humidityMeasure\":\"");
    bodystr.concat("PERCENT");
    bodystr.concat("\",\"lightIntensity\":\"");
    bodystr.concat(lightAnalog);
    bodystr.concat("\",\"lightIntensityMeasure\":\"");
    bodystr.concat("LUX");
    bodystr.concat("\",\"rssiIntensity\":\"");
    bodystr.concat(RSSI_Value);
    bodystr.concat("\",\"rssiIntensityMeasure\":\"");
    bodystr.concat("DECIBEL");
    bodystr.concat("\",\"lightIntensityMeasure\":\"");
    bodystr.concat("LUX");
    bodystr.concat("\",\"soilMoisture\":\"");
    bodystr.concat(soilidAnalog);
    bodystr.concat("\",\"soilMoistureMeasure\":\"");
    bodystr.concat("PERCENT");
    bodystr.concat("\",\"nodeId\":\"");
    bodystr.concat(printNodeName);
    bodystr.concat("\"}");
    // Send HTTP GET request
    int httpResponseCode = http.POST(bodystr.c_str());

    if (httpResponseCode > 0)
    {
      Serial.print("HTTP Response code: ");
      Serial.println(httpResponseCode);
      String payload = http.getString();
      // Serial.println(payload);
    }
    else
    {
      Serial.print("Error code: ");
      Serial.println(httpResponseCode);
      initWiFi();
    }
    http.end();
    previousTime_HTTP = currentTime_HTTP;
  }
}
// Reciver Payload control Device from Network Server to Master Node
int ReceivePayloarFromServer_ControlDevice_Node1()
{
  StaticJsonDocument<1024> doc;
  WiFiClient client;
  HTTPClient http;

  String serverPath = serverName + "/led/token-key/state/t3SMV2rmCjmOg6GLTa76";
  http.begin(client, serverPath.c_str());
  http.addHeader("Content-Type", "application/json");

  int httpResponseCode = http.GET();
  if (httpResponseCode > 0)
  {
    // Serial.print("HTTP Response code: ");
    // Serial.println(httpResponseCode);
    String payload = http.getString();
    // Serial.println(payload);

    DeserializationError error = deserializeJson(doc, payload);

    if (error)
    {
      Serial.print("DeserializeJson() failed: ");
      Serial.println(error.c_str());
      initWiFi();
    }

    Serial.print("Status Receiver: ");
    String data = doc["data"].as<String>();
    Serial.println(data);
    if (data == "ON")
    {
      return 1;
    }
    else if (data == "OFF")
    {
      /* code */
      return 0;
    }
  }
  else
  {
    Serial.print("Error code: ");
    Serial.println(httpResponseCode);
    initWiFi();
  }
  http.end();
}

// Reciver Payload control Device from Network Server to Master Node
int ReceivePayloarFromServer_ControlDevice_Node2()
{ /* Updates frequently */
  StaticJsonDocument<1024> doc;
  WiFiClient client;
  HTTPClient http;

  String serverPath = serverName + "/led/token-key/state/HzcIq70nUYXoP6DdRc3x";
  http.begin(client, serverPath.c_str());
  http.addHeader("Content-Type", "application/json");

  int httpResponseCode = http.GET();
  if (httpResponseCode > 0)
  {
    // Serial.print("HTTP Response code: ");
    // Serial.println(httpResponseCode);
    String payload = http.getString();
    // Serial.println(payload);

    DeserializationError error = deserializeJson(doc, payload);

    if (error)
    {
      Serial.print("DeserializeJson() failed: ");
      Serial.println(error.c_str());
      initWiFi();
    }

    Serial.print("Status Receiver: ");
    String data = doc["data"].as<String>();
    Serial.println(data);
    if (data == "ON")
    {
      return 1;
    }
    else if (data == "OFF")
    {
      /* code */
      return 0;
    }
  }
  else
  {
    Serial.print("Error code: ");
    Serial.println(httpResponseCode);
    initWiFi();
  }
  http.end();
}

// Decode Values of Payload
String getValue(String data, char separator, int index)
{
  int found = 0;
  int strIndex[] = {0, -1};
  int maxIndex = data.length() - 1;

  for (int i = 0; i <= maxIndex && found <= index; i++)
  {
    if (data.charAt(i) == separator || i == maxIndex)
    {
      found++;
      strIndex[0] = strIndex[1] + 1;
      strIndex[1] = (i == maxIndex) ? i + 1 : i;
    }
  }
  return found > index ? data.substring(strIndex[0], strIndex[1]) : "";
}
void ledBlink(int led)
{
  digitalWrite(led, 1);
  delay(200);
  digitalWrite(led, 0);
  delay(200);
}
