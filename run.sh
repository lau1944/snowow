#! /bin/bash

function snowow() {
  if [[ $1 == "build" ]]
  then
    # Install dependencies and run server
    ./mvnw install && ./mvnw spring-boot:run -pl application
  elif [[ "$1" == "install" ]]
  then
    ./mvnw install
  fi
}