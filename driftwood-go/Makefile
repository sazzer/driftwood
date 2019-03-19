GO111MODULE=on
GOCMD=go
OUTPUT=output

# Generic targets
all: lint test build

clean: 
	rm -rf ${OUTPUT}
	rm -rf vendor
	packr clean

build: driftwood

docker: clean lint test
	${GOCMD} mod vendor
	docker build -t uk.co.grahamcox.driftwood.service/driftwood-service:latest .
	rm -rf vendor

run: get-deps generate lint test
	DEBUG=1 ${GOCMD} run -tags docker github.com/sazzer/driftwood/cmd/driftwood

test: get-deps generate
	${GOCMD} test github.com/sazzer/driftwood/internal/...

integration: get-deps generate
	ginkgo -tags docker integration/...

cover: get-deps generate
	mkdir -p output
	${GOCMD} test -covermode=count -coverprofile=${OUTPUT}/coverage.out github.com/sazzer/driftwood/internal/...

cover-report: cover
	${GOCMD} tool cover -html=${OUTPUT}/coverage.out

lint: get-deps generate
	${GOCMD} get github.com/mgechev/revive
	${GOCMD} fmt ./...
	revive -config revive.toml -formatter stylish ./...
	${GOCMD} vet ./...

get-deps:
	${GOCMD} get
	go get github.com/gobuffalo/packr/packr

generate:
	${GOCMD} generate ./...
	packr

# Individual build targets
driftwood: get-deps generate
	mkdir -p ${OUTPUT}
	${GOCMD} build -o ${OUTPUT}/driftwood github.com/sazzer/driftwood/cmd/driftwood
