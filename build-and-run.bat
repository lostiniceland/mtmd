
./gradlew :backend-quarkus:build

cd backend-quarkus
docker build -f src/main/docker/Dockerfile.jvm -t mtmd/quarkus-backend .
cd ..

# Container not yet working due to Vaadin complexities
#./gradlew :frontend-vaadin:jibDockerBuild

docker-compose -f all-docker-compose.yml up --detach

./gradlew :frontend-vaadin:bootRun

