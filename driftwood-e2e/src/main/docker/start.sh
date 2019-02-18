#!/bin/sh

if [ -f /opt/driftwood/application.properties.template ]; then
    envsubst < /opt/driftwood/application.properties.template > /opt/driftwood/application.properties
fi

java org.springframework.boot.loader.JarLauncher
