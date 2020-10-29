FROM openjdk:8-jdk-alpine
VOLUME /tmp
ADD target/catalog-service-0.0.1-SNAPSHOT.jar product-app.jar
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","product-app.jar"]
