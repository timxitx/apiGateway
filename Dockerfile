FROM openjdk:11

EXPOSE 8080
 
ARG JAR_FILE=target/apiGateway-0.0.1-SNAPSHOT.jar

ADD ${JAR_FILE} apiGateway.jar

COPY /src/main/resources/clientCert.jks /
 
ENTRYPOINT ["java", "-jar", "apiGateway.jar"]

HEALTHCHECK --interval=30s --timeout=3s \
CMD curl -f https://localhost:8888/asctuator/health | grep UP || exit 1