#include <ESP8266WiFi.h>
#include <math.h>
#include <OneWire.h>
#include <DallasTemperature.h>
#include <ArduinoHttpClient.h>


#define ONE_WIRE_BUS 5
OneWire oneWire(ONE_WIRE_BUS);
DallasTemperature DS18B20(&oneWire);
char temperatureCString[7];
char temperatureFString[7];

const char* ssid     = "Klan S 2.4";
const char* password = "000";

char serverAddress[] = "000.000.000.000";  // server address
int port = 8080;

WiFiClient wifi;
HttpClient client = HttpClient(wifi, serverAddress, port);

void blinkLed() {
  digitalWrite(2, LOW);
  delay(200);
  digitalWrite(2, HIGH);
  delay(200);
}

void blinkLedFast() {
  digitalWrite(2, LOW);
  delay(100);
  digitalWrite(2, HIGH);
  delay(100);
}

void getTemperature() {
  float tempC;
  float tempF;
  do {
    DS18B20.requestTemperatures();
    tempC = DS18B20.getTempCByIndex(0);
    dtostrf(tempC, 2, 2, temperatureCString);
    tempF = DS18B20.getTempFByIndex(0);
    dtostrf(tempF, 3, 2, temperatureFString);
    delay(100);
  } while (tempC == 85.0 || tempC == (-127.0));
}


void setup() {
  pinMode(2, OUTPUT);
  Serial.begin(115200);
  delay(10);
  Serial.println();
  Serial.println();
  Serial.print("Connecting to ");
  Serial.println(ssid);
  WiFi.setAutoReconnect(true);
  WiFi.begin(ssid, password);

  while (WiFi.status() != WL_CONNECTED) {
    blinkLed();
  }
  Serial.println(WiFi.localIP());
}

int value = 0;
int lastWiFiStatus = 0;

void loop() {
  //CONNECTION STATUS MONITORING FOR DEBUG
  Serial.println(WiFi.status());
  //TEMPERATURE REFRESH
  getTemperature();
  delay(1000);

  //POST QUERY TO CLOUD
  String contentType = "application/x-www-form-urlencoded";
  String postData = "temperature=" + (String)temperatureCString;
  client.post("/saveStoveTemperature", contentType, postData);

  Serial.println("data to post " + postData);

  int statusCode = client.responseStatusCode();
  String response = client.responseBody();

  if(statusCode == 200) blinkLedFast();

  Serial.print("Status code: ");
  Serial.println(statusCode);
  Serial.print("Response: ");
  Serial.println(response);
  delay(10000);

}
