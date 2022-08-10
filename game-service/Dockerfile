FROM adoptopenjdk/openjdk16:ubi
RUN mkdir /opt/app
ADD target/game-service.jar /opt/app/game-service.jar
ARG REGION_ARG=us-east-1
ARG ACCESS_ARG
ARG SECRET_ARG
ENV AWS_REGION=$REGION_ARG
ENV AWS_ACCESS_KEY=$ACCESS_ARG
ENV AWS_SECRET_KEY=$SECRET_ARG
ENV SPRING_PROFILES_ACTIVE=prod
EXPOSE 50051
CMD ["java", "-jar", "/opt/app/game-service.jar"]