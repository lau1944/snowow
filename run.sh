#! /bin/bash

function snowow() {
  if [[ $1 == "build" ]]
  then
    # remove output file
    rm /snowow-engine/src/main/java/com/vau/snowow/engine
    # Install dependencies and run server
    ./mvnw install && ./mvnw spring-boot:run -pl application
  elif [[ "$1" == "install" ]]
  then
    ./mvnw install
  elif [[ "$2" == "clean" ]]
  then
    rm /snowow-engine/src/main/java/com/vau/snowow/engine/outputs
  fi
}