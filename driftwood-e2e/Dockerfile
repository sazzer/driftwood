FROM adoptopenjdk/openjdk11:jdk-11.0.1.13-alpine-slim

RUN apk add --no-cache openssl gettext

ENV DOCKERIZE_VERSION v0.6.1
RUN wget https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-alpine-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && tar -C /usr/local/bin -xzvf dockerize-alpine-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && rm dockerize-alpine-linux-amd64-$DOCKERIZE_VERSION.tar.gz

ARG JAR_FILE
RUN echo JAR_FILE=${JAR_FILE}

WORKDIR /opt/driftwood
ADD ${JAR_FILE} /opt/driftwood/e2e.jar
ADD src/main/docker/application.properties.template /opt/driftwood/application.properties.template
ADD src/main/docker/start.sh /opt/driftwood/start.sh
RUN unzip /opt/driftwood/e2e.jar

CMD dockerize -timeout 30s -wait http://driftwood-ui -wait http://driftwood-service:8080/actuator/health -wait http://chrome:4444 /opt/driftwood/start.sh 
