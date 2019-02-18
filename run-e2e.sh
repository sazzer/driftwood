#!/bin/sh

docker-compose -p driftwood-e2e -f docker-compose.test.yml up --abort-on-container-exit --exit-code-from driftwood-e2e && echo Success || echo Failure
