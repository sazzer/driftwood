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
		attribute, err := service.GetByID(attributes.AttributeID(attributeID))

		if err != nil {
			c.Response().Header().Set(echo.HeaderContentType, "application/problem+json")
			switch err.(type) {
			case attributes.UnknownAttributeError:
				return c.JSON(http.StatusNotFound, problem.Problem{
					Status: http.StatusNotFound,
					Type:   "tag:2018,grahamcox_co_uk:attributes/problems/not-found",
					Title:  "The requested Attribute could not be found",
				})
			default:
				return c.JSON(http.StatusNotFound, problem.Problem{
					Status: http.StatusInternalServerError,
					Type:   "tag:2018,grahamcox_co_uk:attributes/problems/unexpected-error",
					Title:  "An unexpected error occurred",
				})
			}
		}

		return c.JSON(http.StatusOK, Attribute{
			ID:          string(attribute.ID),
			Name:        attribute.Name,
			Description: attribute.Description,
		})
	})
}
