#!/usr/bin/env bash

cd $(dirname $0)

VERSION=1.0.0
echo VERSION: $VERSION

mvn clean install
docker build -t adesso/route-service:${VERSION} .
docker stop route-service
docker rm route-service
docker run -d --name route-service --net=hackathon -p 8092:8080 adesso/route-service:${VERSION}
docker logs route-service -f
