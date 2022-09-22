FROM openjdk:11 as build

EXPOSE 8080:8080
RUN mkdir /app

COPY ./build/libs/finance-trackr-0.0.1-all.jar /app/finance-trackr-0.0.1-all.jar
WORKDIR /app
ENTRYPOINT ["java","-jar","/app/finance-trackr-0.0.1-all.jar"]