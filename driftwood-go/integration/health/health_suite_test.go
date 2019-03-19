package health_test

import (
	"os"
	"testing"

	. "github.com/onsi/ginkgo"
	. "github.com/onsi/gomega"
	"github.com/sirupsen/logrus"

	"github.com/sazzer/driftwood/cmd/driftwood/driftwood"
	"github.com/sazzer/driftwood/internal/server"
)

var db driftwood.DbWrapper
var serverWrapper server.Server

func TestHealth(t *testing.T) {
	RegisterFailHandler(Fail)
	RunSpecs(t, "Health Suite")
}

var _ = BeforeSuite(func() {
	logrus.SetOutput(os.Stdout)
	logrus.SetLevel(logrus.DebugLevel)

	cfg := driftwood.Config{}
	db = driftwood.DbWrapper{}
	db.LaunchDb()
	cfg.Database = db.GetConnectionURL()

	serverWrapper = driftwood.BuildServer(cfg)
})

var _ = AfterSuite(func() {
	db.StopDb()
})
