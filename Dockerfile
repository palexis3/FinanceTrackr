FROM openjdk:17-bullseye
EXPOSE 8080:8080
RUN mkdir -p /app
COPY ./build/libs/finance-trackr-0.0.1-all.jar /app/finance-trackr-0.0.1-all.jar
ENTRYPOINT ["java","-jar","/app/finance-trackr-0.0.1-all.jar"]