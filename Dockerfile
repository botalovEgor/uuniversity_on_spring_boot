FROM openjdk:16-jdk-alpine3.12
RUN mkdir -p /usr/src/app/
RUN apk add --no-cache bash
WORKDIR /usr/src/app/
COPY . /usr/src/app/
ENV spring.profiles.active prod
EXPOSE 8081
EXPOSE 5432
CMD ["java", "-jar", "university.jar"]
