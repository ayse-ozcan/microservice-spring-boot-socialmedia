## SPRING BOOT DOCKER
#### MongoDB
 ```
docker run -d -e MONGO_INITDB_ROOT_USERNAME=mongoadmin -e MONGO_INITDB_ROOT_PASSWORD=secret -p 27017:27017 mongo
```
- MongoDB Compass Download (GUI)
```
  https://www.mongodb.com/try/download/compass
```
#### PostgreSQL
```
docker run -d --name some-postgres -e POSTGRES_PASSWORD=secret -e PGDATA=/var/lib/postgresql/data/pgdata -v /custom/mount:/var/lib/postgresql/data -p 5432:5432 postgres
```
#### RabbitMQ
```
docker run -d name some-rabbit -e RABBITMQ_DEFAULT_USER=user -e RABBITMQ_DEFAULT_PASS=secret -p 5672:5672 -p 15672:15672 rabbitmq:3-management
```
#### Zipkin
```
docker run -d -p 9411:9411 openzipkin/zipkin
```
### A Basic Dockerfile
```
FROM amazoncorretto:17
ARG JAR_FILE=target/*.jar
COPY ${JAR_FILE} application.jar
ENTRYPOINT ["java", "-Xmx2048M", "-jar", "/application.jar"]
```
#### Build an image from a Dockerfile
```
docker build --build-arg JAR_FILE=config-server-git/build/libs/config-server-git-v.0.0.1.jar -t ayseozcan/config-server-git:v.1.0 .
```

