package main

import (
	"context"
	"fmt"
	"github.com/heetch/confita"
	"github.com/labstack/echo"
	"github.com/labstack/echo/middleware"
	log "github.com/sirupsen/logrus"
	"net/http"
	"os"
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

	log.WithFields(log.Fields{
		"animal": "walrus",
		"size":   10,
	}).Debug("A group of walrus emerges from the ocean")

	e := echo.New()
	e.Use(middleware.Logger())

	e.GET("/", func(c echo.Context) error {
		return c.String(http.StatusOK, "Hello, World!")
	})
	log.Debug(e.Start(fmt.Sprintf(":%d", cfg.Port)))
}
