#! /bin/bash

function snowow() {
  if [[ "$1" == "build" ]]
  then
    # remove output file
    rm -r snowow-engine/src/main/java/com/vau/snowow/engine/outputs
    # Install dependencies and run server
    ./mvnw install && ./mvnw spring-boot:run -pl application
  elif [[ "$1" == "install" ]]
  then
    ./mvnw install
  elif [[ "$1" == "clean" ]]
  then
    rm -r snowow-engine/src/main/java/com/vau/snowow/engine/outputs
  fi
}