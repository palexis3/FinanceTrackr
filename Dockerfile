FROM openjdk:17-bullseye

EXPOSE 8080:8080
RUN mkdir /app

COPY ./build/libs/finance-trackr-0.0.1-all.jar /app/
WORKDIR /app
ENTRYPOINT ["java","-jar","/app/finance-trackr-0.0.1-all.jar"]