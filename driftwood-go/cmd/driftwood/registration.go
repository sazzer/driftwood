package main

import (
	attributesDao "github.com/sazzer/driftwood/internal/characters/attributes/dao"
	attributesHttp "github.com/sazzer/driftwood/internal/characters/attributes/http"
	attributesService "github.com/sazzer/driftwood/internal/characters/attributes/service"
	health "github.com/sazzer/driftwood/internal/health"
	healthHttp "github.com/sazzer/driftwood/internal/health/http"
	"github.com/sazzer/driftwood/internal/server"
)

func buildServer() server.Server {
	healthchecks := make(map[string]health.Healthchecker)

	healthchecks["simple"] = simpleHealth{}

	attributesDao := attributesDao.New(nil)
	attributesService := attributesService.New(attributesDao)
	attributesHandlers := attributesHttp.NewHandlerRegistrationFunc(attributesService)

	healthHandlers := healthHttp.NewHandlerRegistrationFunc(healthchecks)

	server := server.New()
	server.RegisterHandlers(attributesHandlers)
	server.RegisterHandlers(healthHandlers)

	return server
}
