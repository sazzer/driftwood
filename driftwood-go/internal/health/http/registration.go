package http

import (
	"github.com/labstack/echo"
	"github.com/sazzer/driftwood/internal/health"
	"github.com/sazzer/driftwood/internal/server"
)

// NewHandlerRegistrationFunc returns the function to register all the handlers for working with Attributes
func NewHandlerRegistrationFunc(checks map[string]health.Healthchecker) server.HandlerRegistrationFunc {
	return func(e *echo.Echo) {
		RegisterHealthHandler(e, checks)
	}
}
