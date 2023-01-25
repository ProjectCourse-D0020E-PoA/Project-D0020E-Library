FROM maven:3.8.1-jdk-8 AS build
COPY src /usr/src/app/src
COPY pom.xml /usr/src/app  
RUN mvn -f /usr/src/app/pom.xml clean assembly:assembly

FROM gcr.io/distroless/java

# Move build files
COPY --from=build /usr/src/app/target/Project-D0020E-1.0-SNAPSHOT-jar-with-dependencies.jar /usr/app/Project-D0020E-1.0-SNAPSHOT-jar-with-dependencies.jar
#RUN java -jar /usr/app/Project-D0020E-1.0-SNAPSHOT-jar-with-dependencies.jar

EXPOSE 888
ENTRYPOINT ["java","-jar","/usr/app/Project-D0020E-1.0-SNAPSHOT-jar-with-dependencies.jar"]