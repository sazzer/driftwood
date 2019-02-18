FROM node:8.15.0-alpine
WORKDIR /opt/driftwood

RUN apk add --no-cache openssl

ENV DOCKERIZE_VERSION v0.6.1
RUN wget https://github.com/jwilder/dockerize/releases/download/$DOCKERIZE_VERSION/dockerize-alpine-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && tar -C /usr/local/bin -xzvf dockerize-alpine-linux-amd64-$DOCKERIZE_VERSION.tar.gz \
    && rm dockerize-alpine-linux-amd64-$DOCKERIZE_VERSION.tar.gz

COPY package.json .
COPY yarn.lock .

ENV CI=1
RUN yarn install

COPY src src
COPY features features
RUN mkdir screenshots

CMD dockerize -timeout 30s -wait http://driftwood-ui -wait http://driftwood-service:8080/actuator/health -wait http://chrome:4444 yarn start
