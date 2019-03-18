package main

import (
	"github.com/sirupsen/logrus"

	"github.com/sazzer/driftwood/cmd/driftwood/driftwood"
)

func main() {
	cfg := loadConfig()

	configureLogging(cfg)

	if cfg.Database == "" {
		db := driftwood.DbWrapper{}
		db.LaunchDb()
		defer db.StopDb()

		cfg.Database = db.GetConnectionURL()
		logrus.WithField("database", cfg.Database).Debug("Connecting to docker database")
	}

	server := driftwood.BuildServer(cfg)

	server.Start(cfg.Port)

}
