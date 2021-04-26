#!/bin/bash
set -e

# Feel free to run the native image, but be warned, it takes longer
./gradlew :backend-quarkus:build
# ./gradlew :backend-quarkus:build -Dquarkus.package.type=native
cd backend-quarkus
docker build -f src/main/docker/Dockerfile.jvm -t mtmd/quarkus-backend .
# docker build -f src/main/docker/Dockerfile.native -t mtmd/quarkus-backend .
cd ..

# Container not yet working due to Vaadin complexities
#./gradlew :frontend-vaadin:jibDockerBuild

docker-compose -f all-docker-compose.yml up --detach

./gradlew :frontend-vaadin:bootRun

