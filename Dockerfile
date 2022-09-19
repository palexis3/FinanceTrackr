FROM openjdk:11
EXPOSE 8080:8080
RUN mkdir /app
COPY ./build/libs/*.jar /app/FinanceTrackr-all.jar
ENTRYPOINT ["java","-jar","/app/FinanceTrackr-all.jar"]