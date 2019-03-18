package driftwood

import (
	"database/sql"

	// Load the Postgres drivers
	_ "github.com/lib/pq"

	attributesDao "github.com/sazzer/driftwood/internal/characters/attributes/dao"
	attributesHttp "github.com/sazzer/driftwood/internal/characters/attributes/http"
	attributesService "github.com/sazzer/driftwood/internal/characters/attributes/service"
	"github.com/sazzer/driftwood/internal/database"
	health "github.com/sazzer/driftwood/internal/health"
	healthHttp "github.com/sazzer/driftwood/internal/health/http"
	"github.com/sazzer/driftwood/internal/server"
)

// BuildServer will construct the actual server to work with
func BuildServer(cfg Config) server.Server {
	db, err := sql.Open("postgres", cfg.Database)
	if err != nil {
		panic(err)
	}

	err = migrateDb(db)
	if err != nil {
		panic(err)
	}

	database := database.New(db)
	healthchecks := make(map[string]health.Healthchecker)

	healthchecks["database"] = database

	attributesDao := attributesDao.New(&database)
	attributesService := attributesService.New(attributesDao)
	attributesHandlers := attributesHttp.NewHandlerRegistrationFunc(attributesService)

	healthHandlers := healthHttp.NewHandlerRegistrationFunc(healthchecks)

	server := server.New()
	server.RegisterHandlers(attributesHandlers)
	server.RegisterHandlers(healthHandlers)

	return server
}
