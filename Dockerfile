# VERSION 1.0

# the base image is a trusted ubuntu build with java 7 (https://index.docker.io/u/dockerfile/java/)
FROM openjdk:8u151-jre-alpine

# install curl to support docker healthcheck
RUN apk --no-cache add curl

# we need this because the workdir is modified in dockerfile/java
WORKDIR /

# copy the locally built fat-jar to the image
ADD target/scala-2.12/scala-workshop-assembly-1.0.jar /app/server.jar

# copy script entrypoint.sh to docker container
ADD entrypoint.sh /entrypoint.sh
RUN chmod +x /entrypoint.sh

# run the (java) server as the daemon user
USER daemon

# run the server when a container based on this image is being run
ENTRYPOINT ["/entrypoint.sh"]

# the server binds to 8080 - expose that port
EXPOSE 8080

# health check
HEALTHCHECK --interval=1m --timeout=3s CMD curl -f http://localhost:8080/ping || exit 1

