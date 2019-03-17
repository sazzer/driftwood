package main

import "github.com/sirupsen/logrus"

func main() {
	cfg := loadConfig()

	configureLogging(cfg)

	if cfg.Database == "" {
		db := dbWrapper{}
		db.launchDb()
		defer db.stopDb()

		cfg.Database = db.getConnectionURL()
		logrus.WithField("database", cfg.Database).Debug("Connecting to docker database")
	}

	server := buildServer(cfg)

	server.Start(cfg.Port)

}
