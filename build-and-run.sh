#!/bin/bash
set -e

cd backend

# Feel free to run the native image, but be warned, it takes longer
./gradlew build
# ./gradlew build -Dquarkus.package.type=native
docker build -f src/main/docker/Dockerfile.jvm -t mtmd/quarkus-backend .
# docker build -f src/main/docker/Dockerfile.native -t mtmd/quarkus-backend .

# todo frontend

docker-compose -f all-docker-compose.yml up
