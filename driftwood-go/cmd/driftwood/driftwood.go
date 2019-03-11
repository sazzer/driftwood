package main

import (
	"context"
	"github.com/heetch/confita"
	log "github.com/sirupsen/logrus"
	"os"
)

// Config represents the environment configuration for the application
type Config struct {
	// Debug represents whether we are executing in Debug mode or not
	Debug bool `config:"debug"`
}

func main() {
	cfg := Config{
		Debug: false,
	}

	err := confita.NewLoader().Load(context.Background(), &cfg)
	if err != nil {
		panic(err)
	}

	log.SetOutput(os.Stdout)
	if cfg.Debug {
		log.SetLevel(log.DebugLevel)
	} else {
		log.SetLevel(log.InfoLevel)
	}

	log.WithFields(log.Fields{
		"animal": "walrus",
		"size":   10,
	}).Debug("A group of walrus emerges from the ocean")

}
