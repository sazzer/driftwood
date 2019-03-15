package main

import (
	"context"

	"github.com/heetch/confita"
)

// Config represents the environment configuration for the application
type Config struct {
	// Debug represents whether we are executing in Debug mode or not
	Debug bool `config:"debug"`
	Port  int  `config:"port"`
}

func loadConfig() Config {
	cfg := Config{
		Debug: false,
		Port:  3000,
	}

	err := confita.NewLoader().Load(context.Background(), &cfg)
	if err != nil {
		panic(err)
	}

	return cfg
}
