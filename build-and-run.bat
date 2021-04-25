
./gradlew :backend-quarkus:build

cd backend-quarkus
docker build -f src/main/docker/Dockerfile.jvm -t mtmd/quarkus-backend .
cd ..

docker-compose -f all-docker-compose.yml up
