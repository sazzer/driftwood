GO111MODULE=on
GOCMD=go
OUTPUT=output

# Generic targets
all: lint test build

clean: 
	rm -rf ${OUTPUT}

build: driftwood

docker: lint test
	${GOCMD} mod vendor
	docker build -t uk.co.grahamcox.driftwood.service/driftwood-service:latest .
	rm -rf vendor

run: get-deps lint test
	${GOCMD} run cmd/driftwood/driftwood.go

test: get-deps
	${GOCMD} test -v ./...

lint: get-deps
	${GOCMD} get github.com/mgechev/revive
	${GOCMD} fmt ./...
	revive -config revive.toml -formatter stylish ./...
	${GOCMD} vet ./...

get-deps:
	${GOCMD} get

# Individual build targets
driftwood: get-deps
	mkdir -p ${OUTPUT}
	${GOCMD} build -o ${OUTPUT}/driftwood cmd/driftwood/driftwood.go
