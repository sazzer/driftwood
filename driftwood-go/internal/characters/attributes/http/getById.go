package http

import (
	"net/http"

	"github.com/labstack/echo"

	"github.com/sazzer/driftwood/internal/characters/attributes"
	"github.com/sazzer/driftwood/internal/problem"
)

// RegisterGetByIDHandler creates the HTTP Handler for getting an Attribute by ID
func RegisterGetByIDHandler(e *echo.Echo, service attributes.Retriever) {
	e.GET("/api/attributes/:id", func(c echo.Context) error {
		attributeID := c.Param("id")
		attribute := service.GetByID(attributes.AttributeID(attributeID))

		if attribute == nil {
			c.Response().Header().Set(echo.HeaderContentType, "application/problem+json")
			return c.JSON(http.StatusNotFound, problem.Problem{
				Status: http.StatusNotFound,
				Type:   "tag:2018,grahamcox_co_uk:attributes/problems/not-found",
				Title:  "The requested Attribute could not be found",
			})
		}
		return c.String(http.StatusOK, "Hello, World!")
	})
}
