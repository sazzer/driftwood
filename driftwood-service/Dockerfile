FROM adoptopenjdk/openjdk11:jdk-11.0.1.13-alpine-slim

RUN apk add curl gettext

ENV PORT=8080

ENV DRIFTWOOD_PG_URL=

ENV DRIFTWOOD_GOOGLE_CLIENTID=
ENV DRIFTWOOD_GOOGLE_CLIENTSECRET=

ARG JAR_FILE
RUN echo JAR_FILE=${JAR_FILE}

ADD ${JAR_FILE} /opt/driftwood/service.jar
ADD src/main/docker/application.properties.template /opt/driftwood/application.properties.template
ADD src/main/docker/start.sh /opt/driftwood/start.sh

WORKDIR /opt/driftwood
CMD /opt/driftwood/start.sh

HEALTHCHECK CMD curl -f http://localhost:8080/actuator/health || exit 1
