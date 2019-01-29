FROM node:8.15.0-alpine
WORKDIR /opt/driftwood

COPY package.json .
COPY yarn.lock .

ENV CI=1
RUN yarn install

COPY src src
COPY features features

CMD yarn start
