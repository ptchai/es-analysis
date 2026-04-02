#!/bin/bash


FILE="/app/application.yml"
if [[ -f "$FILE" ]]; then
    java -jar app.jar --spring.config.location=file:/app/application.yml
else
    java -jar app.jar
fi

