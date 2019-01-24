FROM cypress/base:10
WORKDIR /opt/driftwood

COPY package.json .
COPY yarn.lock .

ENV CI=1
RUN yarn install

RUN yarn run cypress verify

COPY cypress cypress
COPY cypress.json .

CMD yarn run cypress run
