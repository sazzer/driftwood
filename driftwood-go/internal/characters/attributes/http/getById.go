package http

import (
	"net/http"

	"github.com/labstack/echo"

	"github.com/sazzer/driftwood/internal/characters/attributes"
)

// NewGetByIDHandler creates the HTTP Handler for getting an Attribute by ID
func NewGetByIDHandler(service attributes.Retriever) echo.HandlerFunc {
	return func(c echo.Context) error {
		return c.String(http.StatusOK, "Hello, World!")

	}
}
