package http

import (
	"github.com/labstack/echo"
	"github.com/sazzer/driftwood/internal/characters/attributes"
	"github.com/sazzer/driftwood/internal/server"
)

// NewHandlerRegistrationFunc returns the function to register all the handlers for working with Attributes
func NewHandlerRegistrationFunc(service attributes.Retriever) server.HandlerRegistrationFunc {
	return func(e *echo.Echo) {
		RegisterGetByIDHandler(e, service)
	}
}
