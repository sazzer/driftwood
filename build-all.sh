#!/bin/sh

set -e

cd driftwood-service
mvn clean install -Pdocker
cd ..

cd driftwood-ui
yarn lint
CI=true yarn test --ci --all
yarn docker
cd ..

cd driftwood-e2e
mvn clean install -Pdocker
cd ..
