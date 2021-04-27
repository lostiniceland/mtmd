#!/bin/bash
set -e

# Feel free to run the native image, but be warned, it takes longer
./gradlew :backend-quarkus:build
# ./gradlew :backend-quarkus:build -Dquarkus.package.type=native
cd backend-quarkus
docker build -f src/main/docker/Dockerfile.jvm -t mtmd/quarkus-backend .
# docker build -f src/main/docker/Dockerfile.native -t mtmd/quarkus-backend .
cd ..

./gradlew :frontend-vaadin:clean :frontend-vaadin:jibDockerBuild

docker-compose -f all-docker-compose.yml up
