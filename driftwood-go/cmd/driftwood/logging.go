package main

import (
	"os"

	log "github.com/sirupsen/logrus"
)

func configureLogging(cfg Config) {
	log.SetOutput(os.Stdout)
	if cfg.Debug {
		log.SetLevel(log.DebugLevel)
	} else {
		log.SetLevel(log.InfoLevel)
	}

}
