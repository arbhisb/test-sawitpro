FROM eclipse-temurin:11.0.16_8-jdk-alpine
VOLUME /tmp
COPY build/libs/*.jar app.jar
COPY public.key public.key
COPY private.key private.key
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app.jar"]
