cd backend

./gradlew build

docker build -f src/main/docker/Dockerfile.jvm -t mtmd/quarkus-backend .

cd ..

docker-compose -f all-docker-compose.yml up
