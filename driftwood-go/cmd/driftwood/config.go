package main

import (
	"context"

	"github.com/heetch/confita"

	"github.com/sazzer/driftwood/cmd/driftwood/driftwood"
)

func loadConfig() driftwood.Config {
	cfg := driftwood.Config{
		Debug: false,
		Port:  3000,
	}

	err := confita.NewLoader().Load(context.Background(), &cfg)
	if err != nil {
		panic(err)
	}

	return cfg
}
