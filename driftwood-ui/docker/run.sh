#!/bin/sh

envsubst < /driftwood/html/config.js.template > /driftwood/html/config.js

nginx -g 'daemon off;'
