# build stage
FROM golang:alpine AS build-env

RUN apk add --no-cache git mercurial

ADD . /src/github.com/sazzer/driftwood
WORKDIR /src/github.com/sazzer/driftwood

ENV GO111MODULE=on
ENV GOPATH=/src
ENV GOFLAGS=-mod=vendor

RUN go install /src/github.com/sazzer/driftwood/cmd/driftwood/driftwood.go

# final stage
FROM alpine

ENV PORT=3000

WORKDIR /app
COPY --from=build-env /src/bin/driftwood /app/
ENTRYPOINT ./driftwood

