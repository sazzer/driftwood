package main

import (
	"os"

	log "github.com/sirupsen/logrus"

	"github.com/sazzer/driftwood/cmd/driftwood/driftwood"
)

func configureLogging(cfg driftwood.Config) {
	log.SetOutput(os.Stdout)
	if cfg.Debug {
		log.SetLevel(log.DebugLevel)
	} else {
		log.SetLevel(log.InfoLevel)
	}

}
