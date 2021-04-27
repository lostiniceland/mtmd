
./gradlew :backend-quarkus:build

cd backend-quarkus
docker build -f src/main/docker/Dockerfile.jvm -t mtmd/quarkus-backend .
cd ..

./gradlew :frontend-vaadin:clean :frontend-vaadin:vaadinBuildFrontend :frontend-vaadin:jibDockerBuild

docker-compose -f all-docker-compose.yml up
