FROM openjdk:11

EXPOSE 3000
RUN mkdir /app

COPY ./build/libs/finance-trackr-0.0.1-all.jar /app/finance-trackr-0.0.1-all.jar
WORKDIR /app
ENTRYPOINT ["java","-jar","/app/finance-trackr-0.0.1-all.jar"]