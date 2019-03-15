package http

import (
	"net/http"

	"github.com/labstack/echo"

	"github.com/sazzer/driftwood/internal/health"
)

// RegisterHealthHandler creates the HTTP Handler for getting the system health
func RegisterHealthHandler(e *echo.Echo, checks map[string]health.Healthchecker) {
	e.GET("/health", func(c echo.Context) error {
		errors := make(map[string]string)
		status := "ok"
		statusCode := http.StatusOK

		for k, v := range checks {
			checkStatus := v.CheckHealth()
			if checkStatus != nil {
				status = "fail"
				statusCode = http.StatusServiceUnavailable
				errors[k] = checkStatus.Error()
			}
		}

		return c.JSON(statusCode, Health{
			Status: status,
			Errors: errors,
		})
	})
}
