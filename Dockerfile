FROM openjdk:18-alpine
LABEL maintainer="item987"
COPY target/eCommerce-demo-0.0.1-SNAPSHOT.jar eCommerce-demo-0.0.1-SNAPSHOT.jar
ENTRYPOINT ["java","-jar","/eCommerce-demo-0.0.1-SNAPSHOT.jar"]