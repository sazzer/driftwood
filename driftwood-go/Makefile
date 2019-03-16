GO111MODULE=on
GOCMD=go
OUTPUT=output

# Generic targets
all: lint test build

clean: 
	rm -rf ${OUTPUT}
	rm -rf vendor

build: driftwood

docker: clean lint test
	${GOCMD} mod vendor
	docker build -t uk.co.grahamcox.driftwood.service/driftwood-service:latest .
	rm -rf vendor

run: get-deps generate lint test
	${GOCMD} run cmd/driftwood/*.go

test: get-deps generate
	${GOCMD} test ./...

cover: get-deps generate
	${GOCMD} test -covermode=count -coverprofile=${OUTPUT}/coverage.out ./...

cover-report: cover
	${GOCMD} tool cover -html=${OUTPUT}/coverage.out

lint: get-deps
	${GOCMD} get github.com/mgechev/revive
	${GOCMD} fmt ./...
	revive -config revive.toml -formatter stylish ./...
	${GOCMD} vet ./...

get-deps:
	${GOCMD} get

generate:
	${GOCMD} generate ./...

# Individual build targets
driftwood: get-deps generate
	mkdir -p ${OUTPUT}
	${GOCMD} build -o ${OUTPUT}/driftwood cmd/driftwood/*.go
