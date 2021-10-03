FROM openjdk:11

EXPOSE 8888

COPY /src/main/resources/clientCert.jks /
 
ARG JAR_FILE=target/apiGateway-0.0.1-SNAPSHOT.jar

ADD ${JAR_FILE} apiGateway.jar
 
ENTRYPOINT ["java", "-jar", "apiGateway.jar"]

HEALTHCHECK --interval=30s --timeout=3s \
CMD curl -f https://localhost:8888/actuator/health | grep UP || exit 1