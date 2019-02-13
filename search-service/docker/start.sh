#!/usr/bin/env bash
cd ..
./gradlew clean build
cd -
docker-compose up --build search-service
