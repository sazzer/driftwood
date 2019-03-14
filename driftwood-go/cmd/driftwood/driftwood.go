package main

import (
	"context"
	"os"

	"github.com/heetch/confita"
	log "github.com/sirupsen/logrus"

	attributesDao "github.com/sazzer/driftwood/internal/characters/attributes/dao"
	attributesHttp "github.com/sazzer/driftwood/internal/characters/attributes/http"
	attributesService "github.com/sazzer/driftwood/internal/characters/attributes/service"
	"github.com/sazzer/driftwood/internal/server"
)

// Config represents the environment configuration for the application
type Config struct {
	// Debug represents whether we are executing in Debug mode or not
	Debug bool `config:"debug"`
	Port  int  `config:"port"`
}

func main() {
	cfg := Config{
		Debug: false,
		Port:  3000,
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

	attributesDao := attributesDao.New(nil)
	attributesService := attributesService.New(attributesDao)
	attributesHandlers := attributesHttp.NewHandlerRegistrationFunc(attributesService)

	server := server.New()
	server.RegisterHandlers(attributesHandlers)
	server.Start(cfg.Port)
}
