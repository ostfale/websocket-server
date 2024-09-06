# Reproduce WS Connection Problem
+ clone : websocket-client and websocket-server project

## websocket-client
+ build image using paketo: `mvn clean package`

## websocket-server
+ Execute `WebSocketIntegrationTest`
+ to see client container log: 
  + set breakpoint in last test of integration test
  + open container log
