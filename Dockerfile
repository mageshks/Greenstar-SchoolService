FROM openjdk:8-jdk-alpine
MAINTAINER Mageshwaran <mageshwaran.k.s@cognizant.com>
ADD target/greenstarapp-school-service.jar greenstarapp-school-service.jar
ENTRYPOINT ["java", "-jar", "/greenstarapp-school-service.jar"]
EXPOSE 2620